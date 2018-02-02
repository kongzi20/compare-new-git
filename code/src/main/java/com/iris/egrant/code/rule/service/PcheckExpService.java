package com.iris.egrant.code.rule.service;

import java.util.List;
import java.util.Map;

import com.iris.egrant.code.rule.model.ProposalRuleParam;
import com.iris.egrant.core.exception.ServiceException;


public interface PcheckExpService  {
	

	 public Map<String, Object> parseEngRuleParam(Map<String, Object> param, List<ProposalRuleParam> paramList) throws ServiceException;
}