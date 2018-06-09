package com.test.example.code.rule.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.test.example.code.rule.model.RuleTemplate;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;

@Repository
public class RuleTemplateDao extends SimpleHibernateDao<RuleTemplate, Long> {

	public List<RuleTemplate> getAllRuleTemps() {
		String hql = "from RuleTemplate w order by w.id";
		List<RuleTemplate> ruleList = super.find(hql);
		return ruleList;
	}
	
}
