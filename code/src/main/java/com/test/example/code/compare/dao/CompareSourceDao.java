package com.test.example.code.compare.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.hibernate.jdbc.Work;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.springframework.stereotype.Repository;

import com.test.example.code.compare.model.CompareSource;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;
import com.test.example.core.exception.DaoException;
 
@Repository
public class CompareSourceDao extends SimpleHibernateDao<CompareSource, Long> {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private static int i = 0;

	/**
	 * 根据keyCode+type获取compareSource
	 * 
	 * @param keyCode
	 * @param type
	 * @return
	 */
	public List<CompareSource> getCompareSource(Long keyCode, Integer type) {
		String queryString = "from CompareSource where keyCode=? and type= ? and status=0 ";
		return super.createQuery(queryString, keyCode, type).list();
	}

	/**
	 * 获取keyCode+type
	 * 
	 * @param startId
	 * @param endId
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public List getKeyCode(int startId, int fetchSize) {

		// String sql =
		// "select key_code,type from compare_source where status=0    group by key_code,type order by key_code ";
		String sql = "select key_code,type from compare_source where status=0   order by key_code ";
		List<Object> paramList = new ArrayList<Object>();
		return super.createSqlQuery(sql, paramList).setFirstResult(startId).setFetchSize(fetchSize)
				.setMaxResults(fetchSize).list();

	}

	public List<CompareSource> getCompareSourceList(int startId, int fetchSize) {
		String hql = "from CompareSource where status=0 order by keyCode";
		return super.createQuery(hql).setFirstResult(startId).setFetchSize(fetchSize).setMaxResults(fetchSize).list();
	}

	public void updateCompareSource(CompareSource cs) {
		super.save(cs);
		// String sql="update compare_source set content=?,status=?,err_msg=?,complete_date=? where id=?";
		// List<Object> paramsList=new ArrayList<Object>();
		// paramsList.add(cs.getContent());
		// paramsList.add(cs.getStatus());
		// paramsList.add(cs.getErrMsg());
		// paramsList.add(new Date());
		// paramsList.add(cs.getId());
		// super.createSqlQuery(sql, null).executeUpdate();
	}

	/**
	 * 相似度数据来源来源于DB内容
	 * 
	 * @param compareSource
	 * @return
	 * @throws DaoException
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getDBContent(final Long keyCode, final Integer type, final Integer dataType, final String xpath, final String path)  {

		String cotent = (String) jdbcTemplate.execute(

		new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				final String storedProc = "{call proc_compare_mergecontent(?,?,?,?,?,?)}";// 调用的sql

				final CallableStatement cs = con.prepareCall(storedProc);
				i = i + 1;
				// System.out.println();

				cs.setLong(1, keyCode);// 设置输入参数keyCode
				cs.setInt(2, type);// 设置输入参数type
				cs.setInt(3, dataType);// 设置输入参数dataType

				cs.setString(4, xpath);
				cs.setString(5, path);
				cs.registerOutParameter(6, OracleTypes.CLOB);// 注册输出参数的类型
				/*// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"========================" + i + "-------------" + keyCode + "," + type + ","
						+ dataType + "," + xpath + "," + path);*/
				return cs;
			}

		}, new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				return cs.getString(6);// 获取输出参数的值
			}

		});

		return cotent;
	}

	/***
	 * 获取合并内容之后调用实施的存储过程
	 * 
	 * @param keyCode
	 *            业务主键
	 * @param type
	 *            业务类型
	 * @param content
	 *            合并后的内容
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void callExec(final Long keyCode, final Integer type, final String content) throws DaoException {
		// System.out.println("keycode:"+keyCode);
		// System.out.println("type:"+type);
		// System.out.println("content:"+content);
		jdbcTemplate.execute("{call add_compare_list_result(?,?,?)}", new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(java.sql.CallableStatement stat) throws SQLException,
					DataAccessException {
				stat.setLong(1, keyCode);
				stat.setInt(2, type);
				stat.setString(3, content);
				stat.execute();
				return null;
			}
		});
		
		
	}
	

	/***
	 * wk add 2014-5-12
	 * 根据dataType初始化表compare_list和表compare_result数据
	 * 
	 * @param keyCode
	 *            业务主键
	 * @param type
	 *            业务类型
	 * @param content
	 *            合并后的内容
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void callExecByDataType(final Long keyCode, final Integer type, final Integer dataType, final String content) throws DaoException {
		// System.out.println("keycode:"+keyCode);
		// System.out.println("type:"+type);
		// System.out.println("dataType:"+dataType);
		// System.out.println("content:"+content);
		jdbcTemplate.execute("{call init_compare_list_result(?,?,?,?)}", new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(java.sql.CallableStatement stat) throws SQLException,
					DataAccessException {
				stat.setLong(1, keyCode);
				stat.setInt(2, type);
				stat.setInt(3, dataType);
				stat.setString(4, content);
				stat.execute();
				return null;
			}
		});
		
		
	}
	
	/**
	 *  初始化 compare_result
	 * @author add by cg
	 * @param keyCode
	 * @param type
	 * @param dataType
	 * @param content
	 * @param array
	 * @throws DaoException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initCompareResult(Long keyCode, Integer type, Integer dataType, String content, Object[] array) throws DaoException {
		super.getSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
				connection = cp30NativeJdbcExtractor.getNativeConnection(connection) ;
				ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor( "KEY_CODE_TABLE_TYPE", connection  );
				ARRAY vArray = new ARRAY(tabDesc, connection, array);
				CallableStatement   cstmt = connection.prepareCall("call init_compare_list_result_2(?,?,?,?,?)");
				cstmt.setLong(1, keyCode);
				cstmt.setInt(2, type);
				cstmt.setInt(3, dataType);
				cstmt.setString(4, content);
				cstmt.setArray(5, vArray);
				cstmt.execute();
			}
		});
	}

	/**
	 * 获取compare_content_setting列表
	 * 
	 * @param type
	 * @param formCode
	 * @return
	 */
	public List<Map<String, Object>> getContentSettingList(Integer type, Long formCode) {
		String sql = "select xpath,data_type,form_code from compare_content_setting where form_code=? and type=? order by seq_no";
		return super.queryForList(sql, new Object[] { formCode, type });

	}


}