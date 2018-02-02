package com.iris.egrant.code.rule.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.iris.egrant.code.rule.model.RuleTemplate;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;

@Repository
public class RuleTemplateDao extends SimpleHibernateDao<RuleTemplate, Long> {

	public List<RuleTemplate> getAllRuleTemps() {
		String hql = "from RuleTemplate w order by w.id";
		List<RuleTemplate> ruleList = super.find(hql);
		return ruleList;
	}
	
}
