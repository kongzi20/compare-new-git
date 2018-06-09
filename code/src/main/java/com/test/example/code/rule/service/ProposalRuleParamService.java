package com.test.example.code.rule.service;

import java.util.List;
import java.util.Map;

import com.test.example.code.rule.model.ProposalRuleParam;
import com.test.example.core.exception.ServiceException;


public interface ProposalRuleParamService {

	/**
	 * 通过规则id查询规则参数列表
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<ProposalRuleParam> getPrParamsByRuleId(Long id) throws Exception;
	
	/**
	 * 更新参数的值
	 * @param idArr
	 * @param params
	 */
	public void updateParams(String[] idArr, Map<String, Object> params);
	
	/**
	 * 得到所有参数的值
	 * @param param 原有参数
	 * @param paramList 需要获取值的参数
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> parseEngRuleParam(Map<String, Object> param,
			List<ProposalRuleParam> paramList) throws ServiceException;	

}
