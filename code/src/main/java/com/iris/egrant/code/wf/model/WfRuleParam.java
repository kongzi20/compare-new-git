/**
 *  Licensed to IRIS-System co.
 */
package com.iris.egrant.code.wf.model;

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
@Table(name="wf_rule_param")
public class WfRuleParam implements Serializable {

	private static final long serialVersionUID = 347483923412633042L;
	private Long id;
	/**
	 * 模板参数ID，参照表WF_TEMPLATE_PARAM
	 */
	private Long paramTmpId;
	/**
	 * 参照WF_RULE表中ID字段
	 */
	private Long ruleId;
	/**
	 * 参数名称
	 */
	private String paramName;
	/**
	 * 参数类型
	 */
	private String tagType;
	/**
	 * 表达式内容
	 */
	private String expression;
	/**
	 * 执行类别（0：CONSTANT；1：EL；2：SQL；3：BEAN；4：HQL）
	 */
	private Integer expType;
	/**
	 * 存储多选值id相对应的Name，主要用于编辑规则
	 */
	private String valueName;
	
	private Integer inputParamMode;
	
	private String descr;
	
	/**
	 * 参数模版
	 */
	private WfParamTemplate wfParamTemplate;

	@Transient
	public WfParamTemplate getWfParamTemplate() {
		return wfParamTemplate;
	}

	public void setWfParamTemplate(WfParamTemplate wfParamTemplate) {
		this.wfParamTemplate = wfParamTemplate;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_WF_RULE_PARAM", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DESCR")
	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	
	
	@Column(name = "INPUT_PARAM_MODE")
	public Integer getInputParamMode() {
		return inputParamMode;
	}

	public void setInputParamMode(Integer inputParamMode) {
		this.inputParamMode = inputParamMode;
	}

	@Column(name = "PARAM_TMP_ID")
	public Long getParamTmpId() {
		return paramTmpId;
	}

	public void setParamTmpId(Long paramTmpId) {
		this.paramTmpId = paramTmpId;
	}

	@Column(name = "RULE_ID")
	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name = "PARAM_NAME")
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	@Column(name = "TAG_TYPE")
	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	@Column(name = "EXPRESSION")
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Column(name = "EXP_TYPE")
	public Integer getExpType() {
		return expType;
	}

	public void setExpType(Integer expType) {
		this.expType = expType;
	}

	@Column(name = "VALUE_NAME")
	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

}
