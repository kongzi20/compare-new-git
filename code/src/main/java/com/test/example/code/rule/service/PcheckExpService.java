package com.test.example.code.rule.service;

import java.util.List;
import java.util.Map;

import com.test.example.code.rule.model.ProposalRuleParam;
import com.test.example.core.exception.ServiceException;


public interface PcheckExpService  {
	

	 public Map<String, Object> parseEngRuleParam(Map<String, Object> param, List<ProposalRuleParam> paramList) throws ServiceException;
}