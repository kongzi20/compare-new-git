package com.test.example.code.rule.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 申请规则参数
 * 
 * @author 张杰
 * 
 */
@Entity
@Table(name = "PROPOSAL_RULE_PARAM")
public class ProposalRuleParam implements Serializable{

	private static final long serialVersionUID = -8412818023862631219L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PROPOSAL_RULE_PARAM")
	@SequenceGenerator(name = "SEQ_PROPOSAL_RULE_PARAM", sequenceName = "SEQ_PROPOSAL_RULE_PARAM", allocationSize = 1)
	private Long id;

	/**
	 * 规则模板ID
	 */
	@Column(name = "RULE_ID")
	private Long ruleId;
//	/**
//	 * 参数模板ID
//	 */
//	@Column(name = "PARAM_ID")
//	private Long paramId;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "PARAM_ID")
	private ParamTemplate paramTemplate;
	/**
	 * 参数名称
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * 描述
	 */
	@Column(name = "DESCRPTION")
	private String description;
	/**
	 * 参数模式（0 系统参数，1用户设置） Const_dictionaary 枚举 Category=”’param_mode’
	 */
	@Column(name = "PARAM_MODE")
	private String paramMode;
	/**
	 * 参数来源或者参数值类型（整型:0:常量，1：表达式；2:sql;3:class;4:hql ） 限无规则管理员输入使用
	 */
	@Column(name = "SYS_PARAM_TYPE")
	private String sysParamType;
	/**
	 * 参数值(sql、springel、java函数,constant)，限无规则管理员输入使用
	 */
	@Column(name = "SYS_PARAM_VALUE")
	private String sysParamValue;
	/**
	 * 标签类别，input,mutileText,singleSelect,mutilSelect,singleTree,mutilTree
	 * Const_dictionaary 枚举 Category=‘Param_TAG_TYPE’
	 */
	@Column(name = "USER_TAG_TYPE")
	private String userTagType;
	/**
	 * 标签key，input,mutileText,singleSelect,mutilSelect,singleTree,mutilTree
	 * Const_dictionaary 枚举 Category=‘Param_TAG_TYPE’
	 */
	@Column(name = "USER_TAG_Key")
	private String userTagKey;
	public String getUserTagKey() {
		return userTagKey;
	}

	public void setUserTagKey(String userTagKey) {
		this.userTagKey = userTagKey;
	}

	/**
	 * 单下拉框，复下拉框，单选树，复选树的数据来源，自定义的sql脚本 限有规则管理员输入使用
	 */
	@Column(name = "USER_DB_SCRIPT")
	private String userDbScript;
	/**
	 * 单下拉框，复下拉框，单选树，复选树的常数，const_category或者ajaxtree_category的常数 限有规则管理员输入使用
	 */
	@Column(name = "USER_DB_SOURCE")
	private Integer userDbSource;
	/**
	 * 单下拉框，复下拉框的枚举选项，用分号隔开
	 */
	@Column(name = "USER_DB_ENUM")
	private String userDbEnum;
	/**
	 * 用户自定义输入值格式校验脚本
	 */
	@Column(name = "USER_VALIDATE_SCRIPT")
	private String userValidateScript;
	/**
	 * 
	 */
	@Column(name = "USER_CUSTOM_VALUE")
	private String userCustomValue;
	/**
	 * 
	 */
	@Column(name = "USER_CUSTOM_VALUE_NAME")
	private String userCustomValueName;

	/**
	 * 参数中文名称
	 */
	@Column(name = "ZH_CN_NAME")
	private String zhCnName;
	/**
	 * 参数中文名称
	 */
	@Column(name = "XPATH")
	private String xpath;
	
	/**
	 * 参数中文名称
	 */
	@Column(name = "tmp_type")
	private String tmpType;
	
	public String getXpath() {
		return xpath;
	}

	public String getTmpType() {
		return tmpType;
	}

	public void setTmpType(String tmpType) {
		this.tmpType = tmpType;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParamMode() {
		return paramMode;
	}

	public void setParamMode(String paramMode) {
		this.paramMode = paramMode;
	}

	

	public String getSysParamType() {
		return sysParamType;
	}

	public void setSysParamType(String sysParamType) {
		this.sysParamType = sysParamType;
	}

	public String getSysParamValue() {
		return sysParamValue;
	}

	public void setSysParamValue(String sysParamValue) {
		this.sysParamValue = sysParamValue;
	}

	public String getUserTagType() {
		return userTagType;
	}

	public void setUserTagType(String userTagType) {
		this.userTagType = userTagType;
	}

	public String getUserDbScript() {
		return userDbScript;
	}

	public void setUserDbScript(String userDbScript) {
		this.userDbScript = userDbScript;
	}

	public Integer getUserDbSource() {
		return userDbSource;
	}

	public void setUserDbSource(Integer userDbSource) {
		this.userDbSource = userDbSource;
	}

	public String getUserDbEnum() {
		return userDbEnum;
	}

	public void setUserDbEnum(String userDbEnum) {
		this.userDbEnum = userDbEnum;
	}

	public String getUserValidateScript() {
		return userValidateScript;
	}

	public void setUserValidateScript(String userValidateScript) {
		this.userValidateScript = userValidateScript;
	}

	public String getUserCustomValue() {
		return userCustomValue;
	}

	public void setUserCustomValue(String userCustomValue) {
		this.userCustomValue = userCustomValue;
	}

	public String getUserCustomValueName() {
		return userCustomValueName;
	}

	public void setUserCustomValueName(String userCustomValueName) {
		this.userCustomValueName = userCustomValueName;
	}
	public ParamTemplate getParamTemplate() {
		return paramTemplate;
	}

	public void setParamTemplate(ParamTemplate paramTemplate) {
		this.paramTemplate = paramTemplate;
	}

	public String getZhCnName() {
		return zhCnName;
	}

	public void setZhCnName(String zhCnName) {
		this.zhCnName = zhCnName;
	}

	/**
	 * 将参数模板拷贝到规则参数中
	 * @param paramTemplate
	 */
	public void copyParamTemplateToProposalRuleParam(ParamTemplate paramTemplate){
		this.description = paramTemplate.getDescription();
		this.name = paramTemplate.getName();
		ParamTemplate paramt = new ParamTemplate();
		paramt.setId(paramTemplate.getId());
		this.paramTemplate = paramt;
		this.paramMode = paramTemplate.getParamMode();
		this.sysParamType = String.valueOf(paramTemplate.getSysParamType());
		this.sysParamValue = paramTemplate.getSysParamValue();
		//this.userDbEnum = paramTemplate.getuse
		this.userDbScript = paramTemplate.getUserDbScript();
		this.userDbScript = paramTemplate.getUserDbScript();
		this.userDbSource = paramTemplate.getUserDbSource();
		this.userTagType = paramTemplate.getUserTagType();
		this.userValidateScript = paramTemplate.getUserValidateScript();
	}
	
}
