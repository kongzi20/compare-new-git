package com.iris.egrant.code.compare.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.iris.egrant.code.compare.model.CompareResult;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;
import com.iris.egrant.core.utils.CollectionUtils;

 

@Repository
public class CompareResultDao extends SimpleHibernateDao<CompareResult, Long> {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private static int count = 0;
	/**
	 * 获取待处理对比任务列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CompareResult> getCompareTask(long startId, int pageSize) {
		String sql = "from CompareResult t where t.status = 0 and id > ? order by t.id asc";
		return getSession().createQuery(sql).setParameter(0, startId).setMaxResults(pageSize).list();
	}

	/**
	 * 获取待处理对比任务列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCompareTaskForMultithread(long startId, long endId, int pageSize) {
		String sql = "select t.rn, t.result_id from compare_temp_tbl t where t.rn>?  and t.rn<=? order by t.rn asc ";
		return getSession().createSQLQuery(sql).setParameter(0, startId).setParameter(1, endId).setMaxResults(pageSize).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.list();
	}

	public int getProcessCount() {
		String sql = "select count(1) from COMPARE_RESULT t where t.status = 0";
		return queryForInt(sql);
	}

	/**
	 * 获取id列表
	 * 
	 * @return
	 */
	public Long getResultIds() {
		String sql = "select count(id)as total from compare_result t where  status = 0 ";
		return Long.valueOf(getSession().createSQLQuery(sql).uniqueResult().toString());
	}

	/**
	 * 初始化比对，将待指定长度的待比对记录相关信息放入临时表中
	 * 
	 * @param size
	 */
	public Long initCompareData(int size) {
		String sql = "delete from compare_temp_tbl t where 1=1";
		jdbcTemplate.update(sql);
		sql = "insert into compare_temp_tbl(rn, result_id, source_id, target_id, data_type) select * from "
				+ "(select rownum as rn, t.id, t.source_id, t.target_id, t.data_type from compare_result t where status in (0, 3) order by t.id asc) "
				+ "where rn>0 and rn<=?";
		jdbcTemplate.update(sql, new Object[] { size });
		sql = "select count(0) as total from compare_temp_tbl t";
		return jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 比对结束，清空临时表
	 */
	public void emptyCompareTempTbl() {
		jdbcTemplate.update("delete from compare_temp_tbl");
	}

	/**
	 * 修改对比结果
	 * 
	 * @param result
	 */
	public void update(CompareResult result) {
		String sql = "update COMPARE_RESULT  set RESULT = ?,STATUS = ?,ERR_MSG = ? where ID = ?";
		update(sql, new Object[] { result.getCompareResult(), result.getStatus(), result.getErrMsg(), result.getId() });
	}

	/**
	 * wk add 2014-5-14 从prp_compare_result中获取所有的比较结果
	 * 
	 * @param result
	 */
	public Map<String, Object> getAllCompareResult(final Long prpCode1, final Long prpCode2) {
		String countSql = "select count(1) from prp_compare_result where (prp_code1 = ? and prp_code2 = ?) or (prp_code2 = ? and prp_code1 = ?) ";
		Long num = jdbcTemplate.queryForLong(countSql,new Object[] { prpCode1, prpCode2, prpCode1, prpCode2 });
		String sql = "select * from prp_compare_result where (prp_code1 = ? and prp_code2 = ?) or (prp_code2 = ? and prp_code1 = ?) ";
		if(num==0){
			sql = "select * from PRP_COMPARE_RESULT_SNAPSHOT_JL where prp_code1 = ?  and prp_code2 = ?";
			return jdbcTemplate.queryForMap(sql, new Object[] { prpCode1 , prpCode2});
		}
		return jdbcTemplate.queryForMap(sql, new Object[] { prpCode1, prpCode2, prpCode1, prpCode2 });
	}

	/**
	 * 获取proposal_extend.compare_prp_familiar_cnt=-1即比较中的prpCode not exists 避免死锁
	 * 
	 * @return
	 */
	public List<Map<String, Object>> findComparingPrp() {
		String sql = "select decode(t.compare_rule_familiar_cnt,-1,'2','') as type,\n"
				+ "         to_char(t.prp_code) as prp_code from proposal_extend t\n"
				+ "where t.compare_rule_familiar_cnt = '-1' and not exists (select 1 from prp_rule_check_result prcr where prcr.prp_code = t.prp_code and prcr.vallidate_type = 0 and prcr.is_restore = 0)\n"
				+ "union all\n" + "select decode(t.compare_prp_familiar_cnt,-1,'3','') as type,\n" + "         to_char(t.prp_code) as prp_code from proposal_extend t\n"
				+ "where t.compare_prp_familiar_cnt = '-1' and not exists (select 1 from compare_result where status = 0)";
		List<Map<String, Object>> compareList = jdbcTemplate.queryForList(sql);
		count++;
		// 隔一个小时重新同步数量，避免并发引起的疑似项目数量不一致问题
		if (CollectionUtils.isEmpty(compareList) && count >= 60) {
			sql = "select to_char(pe.prp_code) as prp_code, '3' as type\n"
					+ "from proposal_extend pe\n"
					+ "left join proposal p on pe.prp_code = p.prp_code\n"
					+ "where pe.compare_prp_familiar_cnt = 0\n"
					+ "and exists (select 1 from prp_compare_result t where (t.prp_code1 = pe.prp_code or t.prp_code2 = pe.prp_code))\n"
					+ "and p.prj_stat_year = (select app.value from application_setting app where app.key = 'stat_year') and not exists (select 1 from compare_result where status = 0)";
			compareList = jdbcTemplate.queryForList(sql);
			count = 0;
		}
		return compareList;
	}

	/**
	 * 获取proposal_extend.compare_prp_familiar_cnt=-1即比较中的prpCode not exists 避免死锁
	 * 
	 * @return
	 */
	public List<Map<String, Object>> findComparingPrp(final Long prpCode) {
		String sql = "select decode(t.compare_rule_familiar_cnt,-1,'2','')||decode(t.compare_prp_familiar_cnt,-1,'3','') as type, \n"
				+ "to_char(t.prp_code) as prp_code from proposal_extend t \n"
				+ "where t.prp_code = ? and (t.compare_rule_familiar_cnt = '-1' and not exists (select 1 from prp_rule_check_result prcr where prcr.prp_code = t.prp_code and prcr.vallidate_type = 0 and prcr.is_restore = 0) \n"
				+ "or (t.compare_prp_familiar_cnt = '-1' and not exists (select 1 from compare_result where status = 0)))";
		return jdbcTemplate.queryForList(sql, prpCode);
	}

	public List<Map<String, Object>> getCompareTaskList() {
		String sql = "" + "select t.*,s1.content as source_content,s2.content as target_content from compare_temp_tbl t "
							+" inner join compare_list s1 on s1.id = t.source_id "
							+" inner join compare_list s2 on s2.id = t.target_id ";
		return super.queryForList(sql);
	}

}
