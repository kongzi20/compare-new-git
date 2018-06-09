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

/**
 * 
 * @author zzhx 2012-11-2
 */
@Entity
@Table(name = "wf_param_template")
public class WfParamTemplate implements Serializable {

	private static final long serialVersionUID = -720821304206634775L;

	private Long id;
	/**
	 * 参数名称
	 */
	private String name;
	/**
	 * 参数名称
	 */
	private String zhCnName;
	/**
	 * 参数模式（0无流程管理员输入，1有流程管理员输入）
	 */
	private Integer inputParamMode;
	/**
	 * 参数来源或者参数值类型（整型:0:常量，1：表达式；2:sql;3:class;4:hql）
	 */
	private Integer type;

	/**
	 * 参数描述，支持｛0｝
	 */
	private String descr;

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
	/**
	 * 数值的类型：0:整型；1：字符串
	 */
	private Integer valueType;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_WF_PARAM_TEMPLATE", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "zh_cn_name")
	public String getZhCnName() {
		return zhCnName;
	}

	public void setZhCnName(String zhCnName) {
		this.zhCnName = zhCnName;
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

	@Column(name = "DB_Source")
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

	@Column(name = "VALUE_TYPE")
	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public WfRuleParam copyWfParamTemplateTowfRuleParam() {
		WfRuleParam wfrp = new WfRuleParam();
		wfrp.setParamTmpId(this.id);
		wfrp.setParamName(this.name);
		wfrp.setTagType(this.tagType);
		wfrp.setExpType(this.type);
		return wfrp;

	}
	
	public WfRuleTemplateParam copyValueToWfRuleTemplateParam(){
		WfRuleTemplateParam wfRuleTemplateParam = new WfRuleTemplateParam();
		wfRuleTemplateParam.setTmpParamId(id);
		wfRuleTemplateParam.setType(type);
		wfRuleTemplateParam.setName(name);
		wfRuleTemplateParam.setDescr(descr);
		wfRuleTemplateParam.setInputParamMode(inputParamMode);
		wfRuleTemplateParam.setParamValue(paramValue);
		wfRuleTemplateParam.setTagType(tagType);
		wfRuleTemplateParam.setDbScript(dbScript);
		wfRuleTemplateParam.setDbSource(dbSource);
		wfRuleTemplateParam.setDbEnum(dbEnum);
		return wfRuleTemplateParam;
	}
}
