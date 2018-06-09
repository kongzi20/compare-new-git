/**
 *  Licensed to IRIS-System co.
 */
package com.test.example.code.wf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author zzhx 2012-11-2
 */
@Entity
@Table(name = "wf_rule_template")
public class WfRuleTemplate implements Serializable {

	private static final long serialVersionUID = -2005605755130028716L;

	private Long id;
	/**
	 * 规则类型(1)
	 */
	private String ruleType;
	/**
	 * 规则名称
	 */
	private String name;
	/**
	 * 规则输入参数模式（0固定值，1可选），默认为0
	 */
	private int inputParamMode;
	/**
	 * 表达式(sql、springEl、java函数)
	 */
	private String expression;
	/**
	 * 提示信息（中文）
	 */
	private String msgZhCn;
	/**
	 * 对比信息（中文）
	 */
	private String msgZhCnCmp;
	

	/**
	 * 提示信息（英文）
	 */
	private String msgEnUs;
	/**
	 * 提示信息（繁体）
	 */
	private String msgZhTw;
	/**
	 * 规则描述
	 */
	private String ruleDesc;
	/**
	 * 表达式类型
	 */
	private int expType;

	private int ruleId;
	
	private String paramTmpIds;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_WF_RULE_TEMPLATE", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "RULE_TYPE")
	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "INPUT_PARAM_MODE")
	public int getInputParamMode() {
		return inputParamMode;
	}

	public void setInputParamMode(int inputParamMode) {
		this.inputParamMode = inputParamMode;
	}

	@Column(name = "EXPRESSION")
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	@Column(name = "MSG_ZH_CN_CMP")
	public String getMsgZhCnCmp() {
		return msgZhCnCmp;
	}

	public void setMsgZhCnCmp(String msgZhCnCmp) {
		this.msgZhCnCmp = msgZhCnCmp;
	}
	@Column(name = "MSG_ZH_CN")
	public String getMsgZhCn() {
		return msgZhCn;
	}

	public void setMsgZhCn(String msgZhCn) {
		this.msgZhCn = msgZhCn;
	}

	@Column(name = "MSG_EN_US")
	public String getMsgEnUs() {
		return msgEnUs;
	}

	public void setMsgEnUs(String msgEnUs) {
		this.msgEnUs = msgEnUs;
	}

	@Column(name = "MSG_ZH_TW")
	public String getMsgZhTw() {
		return msgZhTw;
	}

	public void setMsgZhTw(String msgZhTw) {
		this.msgZhTw = msgZhTw;
	}

	@Column(name = "RULE_DESC")
	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	@Column(name = "EXP_TYPE")
	// @Transient
	public int getExpType() {
		return expType;
	}

	public void setExpType(int expType) {
		this.expType = expType;
	}

	@Transient
	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	@Transient
	public String getParamTmpIds() {
		return paramTmpIds;
	}

	public void setParamTmpIds(String paramTmpIds) {
		this.paramTmpIds = paramTmpIds;
	}

}
