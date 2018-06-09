package com.test.example.code.wf.compnent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

import com.test.example.code.wf.model.WfMessage;
import com.test.example.code.wf.model.WfRule;
import com.test.example.code.wf.utils.WfMessageUtils;
import com.test.example.core.exception.ServiceException;
import com.test.example.core.utils.testStringUtils;
import com.test.example.core.utils.SqlConverUtils;

/**
 * 
 * sql公用组件.
 * 
 * @author chenxiangrong
 */
@Component
public class WorkflowSqlCompnent {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 执行sql判断.
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public WfMessage getSqlEngRule(WfRule wfRule, Map<String, Object> param) throws ServiceException {

		Map<String, Object> sqlPTmp = this.parseSql(wfRule.getExpression(), param);
		String sqlQuery = (String) sqlPTmp.get("sql");
		final List<Object> sqlParam = (List<Object>) sqlPTmp.get("param");
		PreparedStatementSetter paramSet = createSqlPreparedStatementSetter(sqlParam);
		Long count = 0L;
		if (paramSet != null) {
			@SuppressWarnings("rawtypes")
			List<Long> results = jdbcTemplate.query(sqlQuery, paramSet, new SingleColumnRowMapper(Long.class));
			count = DataAccessUtils.requiredSingleResult(results);
		} else {
			count = jdbcTemplate.queryForLong(sqlQuery);
		}
		boolean engResult = (count > 0);
		WfMessage wfMessage = new WfMessage(wfRule.getId(), engResult, wfRule.getMsgZhCn(), wfRule.getMsgEnUs(),
				wfRule.getMsgZhTw());
		WfMessageUtils.builderMessage(wfMessage, param);
		return wfMessage;
	}

	/**
	 * 执行sql操作.
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public int executeSql(String sql, Map<String, Object> param) throws ServiceException {
		Map<String, Object> sqlPTmp = this.parseSql(sql, param);
		String sqlQuery = (String) sqlPTmp.get("sql");
		final List<Object> sqlParam = (List<Object>) sqlPTmp.get("param");
		PreparedStatementSetter paramSet = createSqlPreparedStatementSetter(sqlParam);
		int result = 0;
		if (paramSet != null) {
			result = this.jdbcTemplate.update(sqlQuery, paramSet);
		} else {
			result = this.jdbcTemplate.update(sqlQuery);
		}
		return result;
	}

	/**
	 * 执行存储过程操作.
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public void executeProcSql(String sql, Map<String, Object> param) throws ServiceException {

		if (sql.indexOf("[synchronized]") > -1) { // 判断该存储过程是否需要同步
			sql = sql.replace("[synchronized]", " ");
			synchronized (this) {
				String sqlStr = SqlConverUtils.strConvert(sql, param);
				this.jdbcTemplate.execute("call " + sqlStr);
			}
		} else {
			String sqlStr = SqlConverUtils.strConvert(sql, param);
			this.jdbcTemplate.execute("call " + sqlStr);
		}

	}

	/**
	 * 解析SQL语言，拆分SQL参数.
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> parseSql(String sql, Map<String, Object> param) throws ServiceException {

		List<Object> sqlParam = new ArrayList<Object>();
		Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);
		Matcher m = p.matcher(sql);
		String key;
		String paramKey;

		while (m.find()) {
			key = m.group();
			paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
			sql = testStringUtils.regexReplaceString(sql, "\\[@" + paramKey + "@\\]", "?");
			//如果map中没有值那么给-1
			sqlParam.add(param.get(paramKey)==null ? "-1":param.get(paramKey));

		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sql", sql);
		result.put("param", sqlParam);
		return result;
	}

	/**
	 * 创建SQL需要执行的SqlPreparedStatementSetter对象.
	 * 
	 * @param sqlParam
	 * @return
	 */
	private PreparedStatementSetter createSqlPreparedStatementSetter(final List<Object> sqlParam) {
		PreparedStatementSetter paramSet = null;
		if (!CollectionUtils.isEmpty(sqlParam)) {
			paramSet = new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					for (int i = 0; i < sqlParam.size(); i++) {
						Object pobj = sqlParam.get(i);
						ps.setObject(i + 1, pobj);
					}
				}
			};
		}
		return paramSet;
	}

	/**
	 * 执行SQL获取参数.
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSqlParam(String sql, Map<String, Object> param) throws ServiceException {

		Map<String, Object> sqlPTmp = this.parseSql(sql, param);
		String sqlQuery = (String) sqlPTmp.get("sql");
		final List<Object> sqlParam = (List<Object>) sqlPTmp.get("param");
		PreparedStatementSetter paramSet = createSqlPreparedStatementSetter(sqlParam);
		Map<String, Object> mapResult = null;
		if (paramSet != null) {
			List<Map<String, Object>> results = this.jdbcTemplate.query(sqlQuery, paramSet, new ColumnMapRowMapper());
			mapResult = DataAccessUtils.requiredSingleResult(results);
		} else {
			mapResult = this.jdbcTemplate.queryForMap(sqlQuery);
		}
		return mapResult;
	}

	/**
	 * 执行SQL获取参数.
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Object getSqlParamValue(String sql, Map<String, Object> param) throws ServiceException {

		Map<String, Object> sqlPTmp = this.parseSql(sql, param);
		String sqlQuery = (String) sqlPTmp.get("sql");
		final List<Object> sqlParam = (List<Object>) sqlPTmp.get("param");
		PreparedStatementSetter paramSet = createSqlPreparedStatementSetter(sqlParam);
		Map<String, Object> mapResult = null;
		if (paramSet != null) {
			List<Map<String, Object>> results = this.jdbcTemplate.query(sql, paramSet, new ColumnMapRowMapper());
			mapResult = DataAccessUtils.requiredSingleResult(results);
		} else {
			mapResult = this.jdbcTemplate.queryForMap(sqlQuery);
		}
		return mapResult;
	}
}