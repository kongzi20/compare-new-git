package com.test.example.code.rule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.rule.model.ProposalRuleParam;
import com.test.example.code.wf.compnent.WorkFlowBeanCompnent;
import com.test.example.code.wf.compnent.WorkFlowElCompnent;
import com.test.example.code.wf.compnent.WorkFlowHqlCompnent;
import com.test.example.code.wf.compnent.WorkflowSqlCompnent;
import com.test.example.code.wf.constant.WfExpConstant;
import com.test.example.code.wf.dao.WfActionDao;
import com.test.example.code.wf.dao.WfRuleDao;
import com.test.example.code.wf.dao.WfRuleParamDao;
import com.test.example.core.exception.ServiceException;


@Service("pcheckExpService")
@Transactional(rollbackFor = Exception.class)
public class PcheckExpServiceImpl implements PcheckExpService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private WfActionDao wfActionDao;
	@Autowired
	private WfRuleDao wfEngRuleDao;
	@Autowired
	private WfRuleParamDao wfEngParamDao;

	@Resource(name = "workFlowElCompnent")
	private WorkFlowElCompnent workFlowElCompnent;
	@Resource(name = "workflowSqlCompnent")
	private WorkflowSqlCompnent workflowSqlCompnent;
	@Resource(name = "workFlowBeanCompnent")
	private WorkFlowBeanCompnent workFlowBeanCompnent;
	@Resource(name = "workFlowHqlCompnent")
	private WorkFlowHqlCompnent workFlowHqlCompnent;

	public Map<String, Object> parseEngRuleParam(Map<String, Object> param,
			List<ProposalRuleParam> paramList) throws ServiceException {
		Map<String, Object> copyMap = new HashMap<String, Object>();
		copyMap.putAll(param);
		ProposalRuleParam proposalRuleParam = null;
		for (int i = 0; i < paramList.size(); i++) {
			proposalRuleParam = paramList.get(i);
			buildParamMap(
					copyMap,
					proposalRuleParam.getName(),
					proposalRuleParam.getSysParamType(),
					proposalRuleParam.getUserCustomValue() == null ? proposalRuleParam
							.getSysParamValue() : proposalRuleParam
							.getUserCustomValue());
		}
		return copyMap;
	}

	private Map<String, Object> buildParamMap(Map<String, Object> param,
			String paramName, String paramType, String paramExp)
			throws ServiceException {
		switch (Integer.valueOf(paramType)) {
		case WfExpConstant.WF_EXP_CONSTANT:// 常量
			param.put(paramName, paramExp);
			break;
		case WfExpConstant.WF_EXP_EL:// 表达式
			param.put(paramName, workFlowElCompnent.getElResult(paramExp));
			break;
		case WfExpConstant.WF_EXP_SQL:// sql
			param.put(
					paramName,
					workflowSqlCompnent.getSqlParam(paramExp, param).get(
							paramName.toUpperCase()));
			break;
		case WfExpConstant.WF_EXP_BEAN:// bean
			param.putAll(workFlowBeanCompnent.getBeanParam(paramExp, param));
			break;
		case WfExpConstant.WF_EXP_HQL:// HQL
			param.putAll(workFlowHqlCompnent.getSqlParam(paramExp, param));
		default:
			break;
		}
		return param;
	}
}