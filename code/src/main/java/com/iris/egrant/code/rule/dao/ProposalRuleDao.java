package com.iris.egrant.code.rule.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iris.egrant.code.grantsetting.dao.GrantSettingDao;
import com.iris.egrant.code.grantsetting.model.GrantSetting;
import com.iris.egrant.code.rule.model.ProposalRule;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;

 

@Repository
public class ProposalRuleDao extends SimpleHibernateDao<ProposalRule, Long> {

/*	@Autowired
	private ProposalRuleMybatisDao proposalRuleMybatisDao;*/

	@Autowired
	private GrantSettingDao grantSettingDao;

	public void addRule(ProposalRule rule) {
		this.getSession().saveOrUpdate(rule);
	}

	public List<ProposalRule> findRuleByStatus(String status) {
		return this.findBy("status", status);
	}

	/*public PageContainer getPreviewRuleList(ConditionContainer c) {
		String queryStr = "RuleSetting.getPrpPreviewRuleList";
		String countStr = "RuleSetting.getPrpPreviewRuleListCount";
		return proposalRuleMybatisDao.getSearchPage(queryStr, countStr, c);
	}*/

	/**
	 * 通过validate_stage查找规则列表
	 * 
	 * @param validateStage
	 * @param area_no为了区分区级业务
	 * @param amt_grade为了区分级次
	 * @param prp_type为了区分申请类别
	 * @return
	 */
	public List<ProposalRule> findRuleByValidateStage(String validateStage, String grantCode) {
		GrantSetting gs = grantSettingDao.get(Long.parseLong(grantCode));
		String area_no = gs.getAreaNo();
		String amt_grade = gs.getAmtGrade();
		String prp_type = gs.getPrpType();
		List<ProposalRule> proposalRules = null;
		if (StringUtils.isBlank(area_no)) {
			proposalRules = this.find("from ProposalRule pr where  pr.areaNo is null and pr.amtGrade=" + amt_grade + " and  pr.prpType =" + prp_type
					+ " and   pr.validateStage like '%" + validateStage + "%' and (pr.grantCode is null or (pr.grantCode = " + grantCode
					+ " and pr.ruleGroup = '02')) and pr.status = '1' and nvl(pr.grantCodeExcept,'0') not like '%" + grantCode + "%'");
		} else {
			proposalRules = this.find("from ProposalRule pr where pr.areaNo=" + area_no + "  and pr.amtGrade=" + amt_grade + " and  pr.prpType =" + prp_type
					+ " and   pr.validateStage like '%" + validateStage + "%' and (pr.grantCode is null or (pr.grantCode = " + grantCode
					+ " and pr.ruleGroup = '02')) and pr.status = '1' and nvl(pr.grantCodeExcept,'0') not like '%" + grantCode + "%'");
		}
		return proposalRules;
	}

	/**
	 * 通过grantCode和ruleGroup来查询
	 */
	public ProposalRule findRuleByGrantCodeAndRuleGroup(Long grantCode, String ruleGroup) {
		List<ProposalRule> proposalRules = this.find(" from ProposalRule pr where pr.grantCode = " + grantCode + " and pr.ruleGroup = " + ruleGroup + " ");
		if (proposalRules.size() > 0) {
			return proposalRules.get(0);
		}
		return null;
	}

	public String getAmtGrade(String grantCode) {
		GrantSetting gs = grantSettingDao.get(Long.parseLong(grantCode));
		return gs.getAmtGrade();
	}
}
