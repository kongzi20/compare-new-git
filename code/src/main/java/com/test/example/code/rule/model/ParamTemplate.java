package com.test.example.code.rule.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 参数模板实体
 * @author 张杰
 * 
 */
@Entity
@Table(name = "PARAM_TEMPLATE")
public class ParamTemplate implements Serializable{

	private static final long serialVersionUID = 5096160741920542004L;

	/**
	 * 自动编号，参数模板ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PARAM_TEMPLATE")
	@SequenceGenerator(name = "SEQ_PARAM_TEMPLATE", sequenceName = "SEQ_PARAM_TEMPLATE", allocationSize = 1)    	
    private Long id;
	/**
	 * 参数名称
	 */	
	@Column(name="NAME")	
    private String name;
	/**
	 * 描述
	 */    
	@Column(name="DESCRPTION")
    private String description;
	/**
	 * 参数模式（0 系统参数，1用户设置）
       Const_dictionaary 枚举
       Category=”’param_mode’
	 */    
	@Column(name="PARAM_MODE")
    private String paramMode;
	/**
	 * 参数来源或者参数值类型（整型:0:常量，1：表达式；2:sql;3:class;4:hql ） 限无规则管理员输入使用
	 */   
	@Column(name="SYS_PARAM_TYPE")
    private Integer sysParamType;
	/**
	 * 参数值(sql、springel、java函数,constant)，限无规则管理员输入使用
	 */    
	@Column(name="SYS_PARAM_VALUE")
    private String sysParamValue;
	/**
	 * 标签类别，input,mutileText,singleSelect,mutilSelect,singleTree,mutilTree
       Const_dictionaary 枚举
       Category=‘Param_TAG_TYPE’
	 */    
	@Column(name="USER_TAG_TYPE")
    private String userTagType;
	/**
	 * 单下拉框，复下拉框，单选树，复选树的数据来源，自定义的sql脚本 限有规则管理员输入使用
	 */    
	@Column(name="USER_DB_SCRIPT")
    private String userDbScript;
	/**
	 * 单下拉框，复下拉框，单选树，复选树的常数，const_category或者ajaxtree_category的常数
                        限有规则管理员输入使用单下拉框，复下拉框的枚举选项，用分号隔开
	 */ 
	@Column(name="USER_DB_SOURCE")
    private Integer userDbSource;
	/**
	 * 用户自定义输入值格式校验脚本
	 */    
	@Column(name="USER_VALIDATE_SCRIPT")
    private String userValidateScript;
	/**
	 * 更新人 
	 */  
	@Column(name="UPDATE_PSN_CODE")
    private Long updatePsnCode;
	/**
	 * 日期
	 */ 
	@Column(name="UPDATE_DATE")
    private Date updateDate;
    
	/**
	 * 参数中文名称
	 */
	@Column(name="ZH_CN_NAME")
	private String zhCnName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Integer getSysParamType() {
		return sysParamType;
	}
	public void setSysParamType(Integer sysParamType) {
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
	public String getUserValidateScript() {
		return userValidateScript;
	}
	public void setUserValidateScript(String userValidateScript) {
		this.userValidateScript = userValidateScript;
	}
	public Long getUpdatePsnCode() {
		return updatePsnCode;
	}
	public void setUpdatePsnCode(Long updatePsnCode) {
		this.updatePsnCode = updatePsnCode;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getZhCnName() {
		return zhCnName;
	}
	public void setZhCnName(String zhCnName) {
		this.zhCnName = zhCnName;
	}
}
