package com.iris.egrant.code.wf.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.iris.egrant.code.wf.model.WfMessage;

public class WfMessageUtils {

    /**
     * 
     * @param message
     * @param param
     * @return
     */
    public static WfMessage builderMessage(WfMessage message, Map<String, Object> param) {

        WfMessageUtils.builderMessage(message.getMessageEnUs(), param);
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
	        Pattern pattern = Pattern.compile("@[\\w]+");
	        Matcher matcher = pattern.matcher(msg);
	        while (matcher.find()) {
	            String rpparam = matcher.group();
	            String paramName = rpparam.substring(1);
	            msg = msg.replace(rpparam, param.get(paramName) == null ? "" : param.get(paramName).toString());
	        }
        }
        return msg;
    }
}
