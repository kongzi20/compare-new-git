package com.iris.egrant.code.wf.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.iris.egrant.code.wf.model.WfRule;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;
import com.iris.egrant.core.exception.DaoException;

/**
 * 申请书工作流-触发条件的数据库操作类.
 * 
 * @author Mao JianGuo
 * @since 2012-09-12
 */
@Repository
public class WfRuleDao extends SimpleHibernateDao<WfRule, Long> {
	/**
	 * 查找已选条件.
	 * 
	 * @param validatorIds
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<WfRule> getEngRulesByIds(List<Long> engRuleIds) throws DaoException {
		String hql = "from WfRule t where t.id in(:engRuleIds)";
		return super.createQuery(hql).setParameterList("engRuleIds", engRuleIds).list();
	}

	/**
	 * 批量删除.
	 * 
	 * @param conditionIds
	 * @throws DaoException
	 */
	public void deleteEngRulesByIds(List<Long> engRuleIds) throws DaoException {
		String hql = "delete from WfRule t where t.id in(:engRuleIds)";
		super.createQuery(hql).setParameterList("engRuleIds", engRuleIds).executeUpdate();
	}

	public void delRule(Long id) {
		String hql = "delete from WfRule t where t.id=:ruleId";
		super.createQuery(hql).setParameter("ruleId", id).executeUpdate();
	}

	/**
	 * 根据wfId和lineId查找规则..
	 * 
	 * @param wfId
	 * @param lineId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public List<Object> findWfRuleByWfIdAndLineId(Long wfId, String lineId) {
		StringBuffer sql = new StringBuffer(1024);
		sql.append(" select id,rule_tmp_id,rule_type,expression,exp_type,msg_zh_cn,msg_en_us,msg_zh_tw,rule_desc,name from (");
		sql.append(" (select r.id,r.rule_tmp_id,r.rule_type,r.expression,r.exp_type,r.msg_zh_cn,r.msg_en_us,r.msg_zh_tw,r.rule_desc,r.name from wf_lines l ");
		sql.append(" left join wf_action a on l.action_id=a.id ");
		sql.append(" left join wf_rule r on a.condition_ids=r.id ");
		sql.append(" where l.wf_id=? and l.line_id=?) ");
		sql.append(" union ");
		sql.append(" (select r.id,r.rule_tmp_id,r.rule_type,r.expression,r.exp_type,r.msg_zh_cn,r.msg_en_us,r.msg_zh_tw,r.rule_desc,r.name from wf_lines l ");
		sql.append(" left join wf_action a on l.action_id=a.id ");
		sql.append(" left join wf_rule r on a.validate_ids=r.id ");
		sql.append(" where l.wf_id=? and l.line_id=?) ");
		sql.append(" union ");
		sql.append(" (select r.id,r.rule_tmp_id,r.rule_type,r.expression,r.exp_type,r.msg_zh_cn,r.msg_en_us,r.msg_zh_tw,r.rule_desc,r.name from wf_lines l ");
		sql.append(" left join wf_action a on l.action_id=a.id ");
		sql.append(" left join wf_rule r on a.event_ids=r.id ");
		sql.append(" where l.wf_id=? and l.line_id=?)) ");
		return this.getSession().createSQLQuery(sql.toString()).setParameter(0, wfId).setParameter(1, lineId)
				.setParameter(2, wfId).setParameter(3, lineId).setParameter(4, wfId).setParameter(5, lineId).list();

	}

	@SuppressWarnings("unchecked")
	public List<WfRule> findWfRuleByIds(Long[] alist) {
		String hql = "from WfRule t where t.id in(:alist)";
		return this.createQuery(hql).setParameterList("alist", alist).list();
	}

	public void addWfRule(WfRule wfRule) {
		this.getSession().saveOrUpdate(wfRule);
	}

	public void updateWfRule(WfRule wfRule) {
		String sql = "update wf_rule set expression=? where id=?";
		this.getSession().createSQLQuery(sql).setParameter(0, wfRule.getExpression()).setParameter(1, wfRule.getId())
				.executeUpdate();
	}
}
