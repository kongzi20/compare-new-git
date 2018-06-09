package com.test.example.code.rule.service;

import java.util.List;
import com.test.example.code.rule.model.PrpRuleCheckResult;

/**
 * 规则比对事务分离：包含事务
 * @author wk
 *
 */
public interface ProposalRuleCheckTransService {
	/**
	 * save
	 * @param prpRuleCheckResult
	 */
	public void savePrpRuleCheckResult (PrpRuleCheckResult prpRuleCheckResult);
	
	/**
	 * save
	 * @param prpRuleCheckResult
	 */
	public void deletePrpRuleCheckResult (PrpRuleCheckResult prpRuleCheckResult);

	/**
	 * 更新对应项目的规则违法情况
	 * 检查不通过或者正在检查中则数目+1
	 * @param prpCode
	 */
	public void updatePrpExtendCnt(Long prpCode, String type);

	public List<PrpRuleCheckResult> getPrpRuleCheckList(int size);
	
	
}