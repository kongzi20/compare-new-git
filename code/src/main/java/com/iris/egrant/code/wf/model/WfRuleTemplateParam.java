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

/**
 * 
 * @author zzhx 2012-11-2
 */
@Entity
@Table(name="wf_rule_template_param")
public class WfRuleTemplateParam implements Serializable {

	private static final long serialVersionUID = -7473846704536701843L;

	private Long id;
	/**
	 * 规则模板ID，对应wf_rule_template表主键
	 */
	private Long ruleTmpId;
	/**
	 * 参数模板ID，对应wf_template_param表主键
	 */
	private Long tmpParamId;
	/**
	 * 参数来源或者参数值类型（整型:0:常量，1：表达式；2:sql;3:class;4:hql）
	 */
	private int type;
	/**
	 * 参数名称
	 */
	private String name;
	/**
	 * 参数描述，支持｛0｝
	 */
	private String descr;
	/**
	 * 参数模式（0无流程管理员输入，1有流程管理员输入）
	 */
	private int inputParamMode;
	/**
	 * 参数值(sql、springel、java函数,constant)，限无流程管理员输入使用
	 */
	private String paramValue;
	/**
	 * 标签类别，input,mutileText,singleSelect,mutilSelect,singleTree,mutilTree
	 */
	private String tagType;
	/**
	 * 单下拉框，复下拉框，单选树，复选树的数据来源，自定义的sql脚本
	 */
	private String dbScript;
	/**
	 * 单下拉框，复下拉框，单选树，复选树的常数，const_category或者ajaxtree_category的常数
	 */
	private String dbSource;
	/**
	 * 单下拉框，复下拉框的枚举选项，用分号隔开
	 */
	private String dbEnum;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_WF_RULE_TEMPLATE_PARAM", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "RULE_TMP_ID")
	public Long getRuleTmpId() {
		return ruleTmpId;
	}

	public void setRuleTmpId(Long ruleTmpId) {
		this.ruleTmpId = ruleTmpId;
	}

	@Column(name = "TMP_PARAM_ID")
	public Long getTmpParamId() {
		return tmpParamId;
	}

	public void setTmpParamId(Long tmpParamId) {
		this.tmpParamId = tmpParamId;
	}

	@Column(name = "TYPE")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR")
	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Column(name = "INPUT_PARAM_MODE")
	public int getInputParamMode() {
		return inputParamMode;
	}

	public void setInputParamMode(int inputParamMode) {
		this.inputParamMode = inputParamMode;
	}

	@Column(name = "PARAM_VALUE")
	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	@Column(name = "TAG_TYPE")
	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	@Column(name = "DB_SCRIPT")
	public String getDbScript() {
		return dbScript;
	}

	public void setDbScript(String dbScript) {
		this.dbScript = dbScript;
	}

	@Column(name = "DB_SOURCE")
	public String getDbSource() {
		return dbSource;
	}

	public void setDbSource(String dbSource) {
		this.dbSource = dbSource;
	}

	@Column(name = "DB_ENUM")
	public String getDbEnum() {
		return dbEnum;
	}

	public void setDbEnum(String dbEnum) {
		this.dbEnum = dbEnum;
	}
	
	public WfRuleParam copyWfRuleTemplateParamToWfRuleParam(WfRuleTemplateParam wfRtp){
		WfRuleParam wfRuleParam = new WfRuleParam();
		
		wfRuleParam.setParamTmpId(wfRtp.getTmpParamId());
		wfRuleParam.setTagType(wfRtp.getTagType());
		wfRuleParam.setParamName(wfRtp.getName());
		wfRuleParam.setExpType(wfRtp.getType());
		wfRuleParam.setInputParamMode(wfRtp.getInputParamMode());
		wfRuleParam.setDescr(wfRtp.getDescr());
		if(wfRtp.getInputParamMode() == 0){
			wfRuleParam.setExpression(wfRtp.getParamValue());
		}
		return wfRuleParam;
	}

}
