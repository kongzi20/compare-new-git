package com.iris.egrant.code.wf.compnent;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import com.iris.egrant.code.wf.model.WfMessage;
import com.iris.egrant.code.wf.model.WfRule;
import com.iris.egrant.code.wf.utils.WfMessageUtils;
import com.iris.egrant.core.exception.ServiceException;
import com.iris.egrant.core.utils.IrisStringUtils;



/**
 * el表达式公用组件.
 * 
 * @author chenxiangrong
 */
@Component
public class WorkFlowElCompnent {
	public WfMessage getElEngRule(WfRule wfRule, Map<String, Object> param) throws ServiceException {
		if(IrisStringUtils.isNullOrBlank(wfRule.getExpression())){
			return null;
		}
		String el = buildEl(wfRule.getExpression(), param);
		Boolean elResult = (Boolean) getElResult(el);
		WfMessage wfMessage = new WfMessage(wfRule.getId(), elResult, wfRule.getMsgZhCn(), wfRule.getMsgEnUs(),
				wfRule.getMsgZhTw());
		return WfMessageUtils.builderMessage(wfMessage, param);
	}

	public void getElAction(String el, Map<String, Object> param) throws ServiceException {
		el = buildEl(el, param);
		getElResult(el);
	}

	public Object getElResult(String el) throws ServiceException {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(el);
		Object result = exp.getValue();
		return result;
	}

	private String buildEl(String el, Map<String, Object> param) throws ServiceException {
		Pattern pattern = Pattern.compile("\\[@[\\w]+@\\]");
		Matcher m = pattern.matcher(el);
		String key;
		String paramKey;
		while (m.find()) {
			key = m.group().toLowerCase();
			paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
			el = IrisStringUtils.regexReplaceString(el, "\\[@" + paramKey + "@\\]", param.get(paramKey));
		}
		return el;
	}
}
