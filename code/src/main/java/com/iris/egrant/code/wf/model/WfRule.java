/**
 *  Licensed to IRIS-System co.
 */
package com.iris.egrant.code.wf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
@Table(name = "wf_rule")
public class WfRule implements Serializable {

	private static final long serialVersionUID = 6413425386554762562L;

	private Long id;
	/**
	 * 规则名称
	 */
	private String name;
	/**
	 * 规则模版id
	 */
	private Long ruleTmpId;
	/**
	 * 规则类型(1:触发条件规则2:校验条件规则 3:触发事件规则)
	 */
	private String ruleType;
	/**
	 * 规则表达式
	 */
	private String expression;
	/**
	 * 执行类别：0：常量；1：表达式；2：SQL；3：类；4：HQL
	 */
	private int expType;
	/**
	 * 提示信息（中文）
	 */
	private String msgZhCn;
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

	private String paramTmpIds;
	
	private Map<String, Object> paramTmpValues;
	/**
	 * 规则参数列表
	 */
	private List<WfRuleParam> paramList = new ArrayList<WfRuleParam>();

	@Transient
	public List<WfRuleParam> getParamList() {
		return paramList;
	}

	public void setParamList(List<WfRuleParam> paramList) {
		this.paramList = paramList;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_WF_RULE", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "RULE_TMP_ID")
	public Long getRuleTmpId() {
		return ruleTmpId;
	}

	public void setRuleTmpId(Long ruleTmpId) {
		this.ruleTmpId = ruleTmpId;
	}

	@Column(name = "RULE_TYPE")
	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	@Column(name = "EXPRESSION")
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Column(name = "EXP_TYPE")
	public int getExpType() {
		return expType;
	}

	public void setExpType(int expType) {
		this.expType = expType;
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

	public void wfRuleCopyFromTemplate(WfRuleTemplate wfrt) {
		// WfRule wfRule = new WfRule();
		this.ruleTmpId = wfrt.getId();
		this.ruleType = wfrt.getRuleType();
		this.expression = wfrt.getExpression();
		this.msgEnUs = wfrt.getMsgEnUs();
		this.msgZhCn = wfrt.getMsgZhCn();
		this.msgZhTw = wfrt.getMsgZhTw();
		this.ruleDesc = wfrt.getRuleDesc();
		this.name = wfrt.getName();
		this.expType = wfrt.getExpType();
		// return wfRule;
	}

	@Transient
	public String getParamTmpIds() {
		return paramTmpIds;
	}

	public void setParamTmpIds(String paramTmpIds) {
		this.paramTmpIds = paramTmpIds;
	}
	
	@Transient
	public Map<String, Object> getParamTmpValues() {
		return paramTmpValues;
	}

	public void setParamTmpValues(Map<String, Object> paramTmpValues) {
		this.paramTmpValues = paramTmpValues;
	}

}
