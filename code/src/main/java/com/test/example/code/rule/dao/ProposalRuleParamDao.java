package com.test.example.code.rule.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.test.example.code.rule.model.ProposalRuleParam;
import com.test.example.code.wf.model.WfRuleParam;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;
import com.test.example.core.exception.DaoException;

 	

@Repository
public class ProposalRuleParamDao extends SimpleHibernateDao<ProposalRuleParam, Long> {

	/**
	 * 获取参数列表.
	 * 
	 * @param conditionId
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<ProposalRuleParam> getParamsById(Long ruleId){
		String hql = "from ProposalRuleParam t where  t.ruleId=? order by id asc";
		return super.createQuery(hql, ruleId).list();
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
