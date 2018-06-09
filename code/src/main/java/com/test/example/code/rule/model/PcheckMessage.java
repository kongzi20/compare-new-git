package com.test.example.code.rule.model;

import java.io.Serializable;

public class PcheckMessage implements Serializable {
    private static final long serialVersionUID = -267456230629056152L;
    private Long id;
    private boolean result;
    private String message;
    private String msgZhCnCmp;
    private String faverMsg;
    private String realityMsg;
    private String ruleDesc;
    private String keyCodes;
    public String getKeyCodes() {
		return keyCodes;
	}


	public void setKeyCodes(String keyCodes) {
		this.keyCodes = keyCodes;
	}


	public PcheckMessage() {
        super();
    }


    public PcheckMessage(Long id, boolean result, String message,
			String msgZhCnCmp) {
		super();
		this.id = id;
		this.result = result;
		this.message = message;
		this.msgZhCnCmp = msgZhCnCmp;
	}
    


	public PcheckMessage(Long id, boolean result, String message,
			String msgZhCnCmp, String faverMsg, String realityMsg,String ruleDesc) {
		super();
		this.id = id;
		this.result = result;
		this.message = message;
		this.msgZhCnCmp = msgZhCnCmp;
		this.faverMsg = faverMsg;
		this.realityMsg = realityMsg;
		this.ruleDesc = ruleDesc;
	}


	public String getRuleDesc() {
		return ruleDesc;
	}


	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}


	public Long getId() {
        return id;
    }

    public String getFaverMsg() {
		return faverMsg;
	}


	public void setFaverMsg(String faverMsg) {
		this.faverMsg = faverMsg;
	}


	public String getRealityMsg() {
		return realityMsg;
	}


	public void setRealityMsg(String realityMsg) {
		this.realityMsg = realityMsg;
	}


	public void setId(Long id) {
        this.id = id;
    }

 
    public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getMsgZhCnCmp() {
		return msgZhCnCmp;
	}


	public void setMsgZhCnCmp(String msgZhCnCmp) {
		this.msgZhCnCmp = msgZhCnCmp;
	}


	public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
