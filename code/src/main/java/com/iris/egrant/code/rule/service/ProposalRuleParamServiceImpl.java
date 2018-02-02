package com.iris.egrant.code.rule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.egrant.code.rule.dao.ProposalRuleParamDao;
import com.iris.egrant.code.rule.model.ProposalRuleParam;
import com.iris.egrant.code.wf.compnent.WorkFlowBeanCompnent;
import com.iris.egrant.code.wf.compnent.WorkFlowElCompnent;
import com.iris.egrant.code.wf.compnent.WorkFlowHqlCompnent;
import com.iris.egrant.code.wf.compnent.WorkflowSqlCompnent;
import com.iris.egrant.code.wf.constant.WfExpConstant;
import com.iris.egrant.core.exception.ServiceException;
import com.iris.egrant.core.utils.IrisStringUtils;

@Service("proposalRuleParamService")
@Transactional(rollbackFor = Exception.class)
public class ProposalRuleParamServiceImpl implements ProposalRuleParamService {

	@Autowired
	private ProposalRuleParamDao proposalRuleParamDao;
	
	@Resource(name = "workFlowElCompnent")
	private WorkFlowElCompnent workFlowElCompnent;
	@Resource(name = "workflowSqlCompnent")
	private WorkflowSqlCompnent workflowSqlCompnent;
	@Resource(name = "workFlowBeanCompnent")
	private WorkFlowBeanCompnent workFlowBeanCompnent;
	@Resource(name = "workFlowHqlCompnent")
	private WorkFlowHqlCompnent workFlowHqlCompnent;	
		
	
	@Override
	public List<ProposalRuleParam> getPrParamsByRuleId(Long id)
			throws Exception {
		return this.proposalRuleParamDao.getParamsById(id);
	}
	
	@Override
	public void updateParams(String[] idArr, Map<String, Object> params) {
		for (String idStr : idArr) {
			ProposalRuleParam ruleParam = this.proposalRuleParamDao.get(NumberUtils.toLong(idStr));
			ruleParam.setUserCustomValue(IrisStringUtils.toString(params.get("expression_" + idStr)));
			ruleParam.setUserCustomValueName(IrisStringUtils.toString(params.get("valueName_" + idStr)));
			this.proposalRuleParamDao.save(ruleParam);
		}
	}	
	
    /***
     * 得到所有参数的值 封装到map
     */
	@Override
	public Map<String, Object> parseEngRuleParam(Map<String, Object> param,
			List<ProposalRuleParam> paramList)  throws ServiceException {
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
			String paramName, String paramType, String paramExp) throws ServiceException{
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
