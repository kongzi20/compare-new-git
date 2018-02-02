package com.iris.egrant.code.rule.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.iris.egrant.code.rule.model.PcheckMessage;
import com.iris.egrant.core.utils.IrisStringUtils;

public class PcheckMessageUtils {

    /**
     * 
     * @param message
     * @param param
     * @return
     */
    public static PcheckMessage builderMessage(PcheckMessage message, Map<String, Object> param) {

    	message.setMessage(PcheckMessageUtils.builderMessage(message.getMessage(), param));
    	message.setMsgZhCnCmp(PcheckMessageUtils.builderMessage(message.getMsgZhCnCmp(), param));
    	message.setRealityMsg(PcheckMessageUtils.builderMessage(message.getRealityMsg(), param));
    	message.setFaverMsg(PcheckMessageUtils.builderMessage(message.getFaverMsg(), param));
    	message.setRuleDesc(PcheckMessageUtils.builderMessage(message.getRuleDesc(), param));
        return message;
    }

    /**
     * 构造消息.
     * 
     * @param msg
     * @param param
     * @return
     */
    private static String builderMessage(String msg, Map<String, Object> param) {
    	if(StringUtils.isNotBlank(msg)){
	        //Pattern pattern = Pattern.compile("@[\\w]+");
	        Pattern pattern = Pattern.compile("\\[@[\\w]+@\\]");
	        Matcher matcher = pattern.matcher(msg);
	        while (matcher.find()) {
		         String rpparam = matcher.group().toLowerCase();
		         String paramName = rpparam.substring(2, rpparam.length() - 2);// 去掉[@ @]
				if (param.containsKey(paramName)) {
					msg =  IrisStringUtils
							.regexReplaceString(msg, "\\[@" + paramName + "@\\]", "" + param.get(paramName) + "");
				}
			}
        }
        return msg;
    }
}
