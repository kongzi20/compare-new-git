package com.test.example.code.rule.dao;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import com.test.example.code.rule.model.PcheckMessage;
import com.test.example.code.rule.model.ProposalRule;
import com.test.example.code.rule.util.PcheckMessageUtils;
import com.test.example.core.exception.ServiceException;
import com.test.example.core.utils.DateFormator;
import com.test.example.core.utils.DateUtils;
import com.test.example.core.utils.testStringUtils;



/**
 * el表达式公用组件.
 * 
 * @author chenxiangrong
 */
@Component
public class ElCompnent {
	public PcheckMessage getElEngRule(ProposalRule pRule, Map<String, Object> param) throws ServiceException {
		if(testStringUtils.isNullOrBlank(pRule.getExpressionDetail())){
			return null;
		}
		String el = buildEl(pRule.getExpressionDetail(), param);
		System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"========================================================="+pRule.getExpressionDetail());
		System.out.println(el);
		Boolean elResult = (Boolean) getElResult(el);
		System.out.println(elResult);
		System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========================================================");
		PcheckMessage wfMessage = new PcheckMessage(pRule.getId(), elResult, pRule.getMessage(),pRule.getMsgZhCnCmp(),pRule.getFaverMsg(),pRule.getRealityMsg(),pRule.getRuleDesc());
		if(param.get("key_codes")!=null){
			wfMessage.setKeyCodes(param.get("key_codes").toString());
		}
		if(param.get("keycodes") != null){
			wfMessage.setKeyCodes(param.get("keycodes").toString());
		}
		if(param.get("KEY_CODES")!=null){
			wfMessage.setKeyCodes(param.get("KEY_CODES").toString());
		}
		if(param.get("KEYCODES")!=null){
			wfMessage.setKeyCodes(param.get("KEYCODES").toString());
		}		
		return PcheckMessageUtils.builderMessage(wfMessage, param);
	}

	/**
	 * 得到el表达式的值
	 * @param pRule
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	public Boolean getElResult(ProposalRule pRule,Map<String,Object> param) throws ServiceException{
		if(testStringUtils.isNullOrBlank(pRule.getExpressionDetail())){
			return null;
		}
		String el = buildEl(pRule.getExpressionDetail(), param);
		System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"========================================================="+pRule.getExpressionDetail());
		System.out.println(el);
		Boolean elResult = (Boolean) getElResult(el);
		System.out.println(elResult);
		System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========================================================");
		return elResult;
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

	public String buildEl(String el, Map<String, Object> param) throws ServiceException {
		//如果含有两个@@符号，则先将[@@ @@]表达式中的值计算出来。
//		if(el.indexOf("@@") != -1) el =  buildTwoEl(el);	
		if(StringUtils.isNotBlank(el)){
			Pattern pattern = Pattern.compile("\\[@[\\w]+@\\]");
			Matcher m = pattern.matcher(el);
			String key;
			String paramKey;
			while (m.find()) {
				key = m.group().toLowerCase();
				paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
				if(param.get(paramKey) == null || !param.containsKey(paramKey)){
					el = testStringUtils.regexReplaceString(el, "\\[@" + paramKey + "@\\]", "-9999999999");
				}else{
					el = testStringUtils.regexReplaceString(el, "\\[@" + paramKey + "@\\]", param.get(paramKey));
				}
			}
			//计算[@@ @@]中表达式的值
			el = buildTwoEl(el);			
		}
		return el;
	}
	
	/**
	 * 假如有表达式用[@@ @@]括起来 ，那么先求值再替换表达式
	 * @param el
	 * @param param
	 * @return
	 */
	private String buildTwoEl(String el){
		if(el.indexOf("@@") != -1){
		    String elscript =el.substring(el.indexOf("[@@")+3,el.indexOf("@@]")); 
		    String elscript0 =el.substring(0,el.indexOf("[@@")); 
		    String elscript1 =el.substring(el.indexOf("@@]")+3,el.length()); 
			String elvalue = "0";
			try {
				elvalue = ""+getElResult(elscript)+"";
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			el = elscript0+ elvalue+ elscript1;			
		}
		return el;
	}
	
	public static void main(String[] args){
		String a = "('223.00'.indexOf('-') == -1 and '20000.00'.indexOf('-') == -1 ) ? (('223.00'.replaceAll('[^0-9\\.]','').indexOf('.')>'20000.00'.indexOf('.'))  ?  true :(('223.00'.replaceAll('[^0-9\\.]','').indexOf('.')=='20000.00'.indexOf('.')) ? '223.00'.replaceAll('[^0-9\\.]','')>'20000.00'  : false  : ('223.00'.indexOf('-') == 1 and '20000.00'.indexOf('-') == 1 ) ? (('223.00'.replaceAll('[^0-9\\.\\-]','').indexOf('.')<'20000.00'.indexOf('.'))  ?  true :(('223.00'.replaceAll('[^0-9\\.\\-]','').indexOf('.')=='20000.00'.indexOf('.')) ? '223.00'.replaceAll('[^0-9\\.\\-]','')<'20000.00'  : false : ('223.00'.indexOf('-') == 1 and '20000.00'.indexOf('-') == -1 ) ? false  : ('223.00'.indexOf('-') == -1 and '20000.00'.indexOf('-') == 1 ) ? true : false";
				Boolean elResult;
				try {
					elResult = (Boolean) new  ElCompnent().getElResult(a);
					System.out.println(elResult);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	
	}
}
