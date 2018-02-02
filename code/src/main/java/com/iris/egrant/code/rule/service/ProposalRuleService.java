package com.iris.egrant.code.rule.service;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import com.iris.egrant.code.rule.model.ProposalRule;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.exception.ServiceException;

 
public interface ProposalRuleService {

	/**
	 * 查找列表
	 * @param c
	 * @return
	 * @throws Exception
	 */
	 // public PageContainer getRuleList(ConditionContainer c) throws Exception;
	
	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ProposalRule getRuleById(Long id) throws Exception;
	
	/**
	 * 通过xpath和grandcode来查找ProposalRule
	 * @param grandCode
	 * @param xpath
	 * @return
	 * @throws Exception
	 */
	public ProposalRule getRuleByGrandCodeAndXpath(Long grandCode,String xpath) throws Exception;
	
	/**
	 * 替换
	 * @param rule
	 * @return
	 * @throws Exception
	 */
	public ProposalRule replaceParam(ProposalRule rule) throws Exception;
	/**
	 * 保存规则
	 * @param id
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProposalRule saveCommonRule(String id, Map<String, Object> params)
			throws Exception;
	
	/**
	 * ajax替换
	 * @param message
	 * @param idArr
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String ajaxChangeMessage(String message,String[] idArr,Map<String,Object> params) throws Exception;
	
	/**
	 * 更新rule
	 * @param rule
	 * @throws Exception
	 */
	public void updateRule(ProposalRule rule) throws Exception;

	public ProposalRule saveCustomRule(Map<String, Object> map) throws NumberFormatException, DaoException;
	public Map<String, Object> getPrpLoad(Map<String, Object> params) throws ServiceException,
			UnknownHostException ;

/*	public PageContainer getCustomRuleByGrantCode(
			ConditionContainer conditionContainer);*/
	
	/**
	 * 查询预览列表
	 * @param c
	 * @return
	 * @throws Exception
	 */
	 // public PageContainer getPreviewRuleByGrantCode(ConditionContainer c) throws Exception;
	
	/**
	 * 通过grantCode查选规则信息
	 * @param grantCode
	 * @return
	 * @throws Exception
	 */
	public List<ProposalRule> getRuleListByGrantCode(Long grantCode) throws Exception;

	/**
	 * 获取某个prp_code的预览规则列表
	 * @param c
	 * @return
	 */
 // 	public PageContainer getPreviewRuleList(ConditionContainer c) throws Exception;
	
	/**
	 * 删除规则计算结果
	 * @param id
	 */
	public void deleteRuleCheckResults(Long id);

	/**
	 *往prp_rule_check_result添加记录
	 * @param id
	 * @param ruleType 
	 */
	public void addRuleCheckResults(Long id, String ruleType);
	
	/**
	 * 通过validate_stage来查找规则列表
	 * @param validateStage
	 * @return
	 * @throws Exception
	 */
	public List<ProposalRule> getProposalRulesByValidateStage(String validateStage,String grantCode) throws ServiceException;
	
	/**
	 * 通过参数和规则列表来验证
	 * @param params
	 * @param proposalRules
	 * @return
	 * @throws Exception
	 */
	public List<String> validate(Map<String,Object> params,List<ProposalRule> proposalRules) throws ServiceException;
	
	/**
	 * 通过codes来得到
	 * @param codes
	 * @return
	 * @throws ServiceException
	 */
	public List<String> findValidateStageNames(String codes) throws ServiceException;

	public List getRuleSettings(Long formCode);	
	
}
