package com.test.example.code.rule.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.test.example.code.compare.constant.AppSettingConstants;
import com.test.example.code.compare.service.AppSettingContext;
import com.test.example.code.rule.model.PrpRuleCheckResult;
import com.test.example.code.rule.model.PrpRuleCheckResultKey;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;



/**
 * 申请规则比对结果dao
 * 
 */
@Repository
public class PrpRuleCheckResultDao extends SimpleHibernateDao<PrpRuleCheckResult, PrpRuleCheckResultKey> {

	/*public PageContainer getList(ConditionContainer conditionContainer) {
		PageQueryHanlder pageQuery = new PageQueryHanlder();
		String sql = "select prcr.*, '规则名暂未定' as rule_name,cd1.zh_cn_caption as vallidate_name," + " cd2.zh_cn_caption as check_result,psn.zh_name as confirm_psn_name "
				+ " from prp_rule_check_result prcr" + " left join const_dictionary cd1 on cd1.category = 'vallidate_type' and cd1.code = prcr.vallidate_type"
				+ " left join const_dictionary cd2 on cd2.category = 'fit_status' and cd2.code = prcr.status" + " left join person psn on psn.psn_code = prcr.confirm_psn"
				+ " where prcr.rule_code <> 0 and prcr.prp_code = :prpCode ";
		pageQuery.setQlMode(PageQueryHanlder.MODEL_SQL);
		pageQuery.addQlSegment(sql);
		pageQuery.addParamInMap("prpCode", conditionContainer.getConditions().get("prpCode"));
		return pageQuery.loadOut(this, conditionContainer);
	}*/

	@SuppressWarnings("unchecked")
	public Map<String, Object> getSimplePrpInfo(Long prpCode) {
		String sql = "select p.prp_code,p.grant_name,gs.zh_cn_grant_name as grant_name,o.org_code,o.name as org_name,"
				+ "p.stat_year,p.zh_title from proposal p left join grant_setting gs on gs.grant_code = p.grant_code left join "
				+ "organization o on o.org_code = p.org_code where p.prp_code = ?";
		List<Map<String, Object>> list = this.getSession().createSQLQuery(sql).setLong(0, prpCode).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/*
	 * 规则计算结果重置
	 */
	public void ruleResultReset(String ruleId) {
		int year = Integer.valueOf(AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR));
		String sql = "update prp_rule_check_result prc set prc.is_restore = '0', prc.status='' where (prc.rule_code,prc.prp_code) in " + " (select pr.rule_code,pr.prp_code from"
				+ " prp_rule_check_result pr inner join proposal p " + " on p.prp_code = pr.prp_code" + " where pr.rule_code=?  and p.status not in ('08', '09') and  p.prj_stat_year="
				+ year + ")";
		super.update(sql, new Object[] { ruleId });
	}

	/*
	 * 规则计算结果状态查询
	 */
	public int getCalStatus(String ruleId) {
		String sql = "select count(1) " + " from prp_rule_check_result prc" + " where nvl(prc.is_restore, 0) = '0'" + " and prc.rule_code = ?";
		return this.queryForInt(sql, new Object[]{ruleId});
	}

	@SuppressWarnings("deprecation")
	public void addRuleResult(Long ruleId, String ruleType) {
		super.getSession().doWork(new Work() {
			
					@Override
					public void execute(Connection connection) throws SQLException {
				try {
					String procString = "{call ADD_PRP_RULE_CHECK_RESULT(?,?)}";
					CallableStatement cs = connection.prepareCall(procString);
					cs.setLong(1, ruleId);
					cs.setString(2, ruleType);
					cs.execute();
				} catch (Exception e) {
					logger.error("执行 ADD_PRP_RULE_CHECK_RESULT出错！" + e + "ruleId：" + ruleId + ",ruleType:" + ruleType);
				}
			}
	});
	}

	@SuppressWarnings("deprecation")
	public void updatePrpExtendCnt(Long prpCode, String type) {
		super.getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					String procString = "{call sp_update_prpextend_check_cnt(?,?)}";
					CallableStatement cs = connection.prepareCall(procString);
					cs.setLong(1, prpCode);
					cs.setString(2, type);
					cs.execute();
				} catch (Exception e) {
					logger.error("执行 sp_update_prpextend_check_cnt出错！" + e + "prpCode：" + prpCode);
				}
				
			}
		});
	}

	/**
	 * 同步行业运行数据
	 * 
	 * @throws Exception
	 */
	public void statDataSyncTask() throws Exception {
		super.getSession().doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					String procString = "{call STAT_INIT.STAT_INIT()}";
					CallableStatement cs = connection.prepareCall(procString);
					cs.execute();
				} catch (Exception e) {
					logger.error("执行 STAT_INIT.STAT_INIT出错！" + e);
					throw e;
				}
			}
		});

	}

	@SuppressWarnings("unchecked")
	public List<PrpRuleCheckResult> getPrpRuleCheckList(int size) {
		String hql = "select pr from PrpRuleCheckResult pr where pr.isRestore = 0 and nvl(pr.status, -1) != 2";
		List<PrpRuleCheckResult> list = super.createQuery(hql, new ArrayList<Object>()).setMaxResults(size).list();
		return list;
	}
	
	/**
	 * add by cg  规则计算调度 使用
	 * @param startKeyCode
	 * @param endKeyCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PrpRuleCheckResult> getPrpRuleCheckResultList(Long startKeyCode, Long endKeyCode) {
		String hql = "select pr from PrpRuleCheckResult pr where pr.isRestore = 0 and nvl(pr.status, -1) != 2 and  pr.pkey.prp_code >= :startKeyCode and pr.pkey.prp_code <= :endKeyCode";
		List<PrpRuleCheckResult> list = super.createQuery(hql)
				.setParameter("startKeyCode", startKeyCode)
				.setParameter("endKeyCode", endKeyCode)
				.setFetchSize(2000)
				.list();
		return list;
	}
	
	public void updateIsRestore() throws Exception {
		super.getSession().doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					String procString = "{call UPDATEISRESTORE.UPDATEISRESTORE()}";
					CallableStatement cs =  connection.prepareCall(procString);
					cs.execute();
				} catch (Exception e) {
					logger.error("执行 UPDATEISRESTORE.UPDATEISRESTORE出错！" + e);
					throw e;
				}
				
			}
		});
	}
}
