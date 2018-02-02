package com.iris.egrant.code.wf.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.iris.egrant.code.wf.model.WfRuleParam;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;
import com.iris.egrant.core.exception.DaoException;
/**
 * 工作流参数信息Dao.
 * 
 * @author chenxiangrong
 * @since 2012-09-13
 */
@Repository
public class WfRuleParamDao extends SimpleHibernateDao<WfRuleParam, Long> {
	public void deleteParamsById(Long engRuleId){
		String hql = "delete from WfRuleParam t where t.ruleId=?";
		super.createQuery(hql, engRuleId).executeUpdate();
	}

	/**
	 * 获取参数列表.
	 * 
	 * @param conditionId
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<WfRuleParam> getEngParamsById(Long engRuleId) throws DaoException {
		String hql = "from WfRuleParam t where  t.ruleId=?";
		return super.createQuery(hql, engRuleId).list();
	}

	/**
	 * 删除参数.
	 * 
	 * @param engRuleIds
	 * @throws DaoException
	 */
	public void deleteEngParamsByIds(List<Long> engRuleIds) throws DaoException {
		String hql = "delete from WfRuleParam t where t.ruleId in(:engRuleIds)";
		super.createQuery(hql).setParameterList("engRuleIds", engRuleIds).executeUpdate();
	}

	/**
	 * 保存参数
	 * 
	 * @param wfRuleParam
	 */
	public void add(WfRuleParam wfRuleParam) {
		this.getSession().save(wfRuleParam);
	}
	
}
