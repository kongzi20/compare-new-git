package com.test.example.code.rule.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
 

/**
 * 规则模板实体
 * @author 张杰
 *seq_rule_template
 */
@Entity
@Table(name="RULE_TEMPLATE")
public class RuleTemplate {

	/**
	 * 自动编号，规则模板ID
	 */
	@Id
	@Column(name = "ID")	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RULE_TEMPLATE")
	@SequenceGenerator(name = "SEQ_RULE_TEMPLATE", sequenceName = "SEQ_RULE_TEMPLATE", allocationSize = 1)  
	private Long id;
	
	/**
	 * 规则名称
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * 规则适用范围（工作流 立项）
	 */	
	@Column(name = "RULE_CATEGORY")
	private String ruleCategory;
	/**
	 * 规则类型（condition、validator、functions）
(对比只有validator)
	 */	
	@Column(name = "RULE_TYPE")
	private String ruleType;
	/**
	 * 校验方式：0系统计算,1人工识别
	 */	
	@Column(name = "VALLIDATE_TYPE")
	private String vallidateType;
	/**
	 * 规则输入参数模式（0固定值，1可选大于表达式）(待定)
	 */	
	@Column(name = "INPUT_PARAM_MODE")
	private Integer inputParamMode;
	/**
	 * 表达式类型：0:常量；1：springel表达式；2：sql；3：class；4：hql
	 */	
	@Column(name = "EXPRESSION_TYPE")
	private Integer expressionType;
	/**
	 * 表达式详情(sql、springel、java函数)
	 */	
	@Column(name = "EXPRESSION_DETAIL")
	private String expressionDetail;
	/**
	 * 界面规则展示详情 参数替换{}占位符
	 */	
	@Column(name = "MESSAGE")
	private String message;
	/**
	 * 描述
	 */	
	@Column(name = "RULE_DESC")
	private String ruleDesc;
	/**
	 * 更新人
	 */	
	@Column(name = "UPDATE_PSN_CODE")
	private Long updatePsnCode;
	/**
	 * 更新时间
	 */	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	/**
	 * 规则对比中文信息
	 */
	@Column(name = "MSG_ZH_CN_CMP")
	private String msgZhCnCmp;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH }, fetch = FetchType.LAZY)
	@JoinTable(name = "rule_param_template", joinColumns = { @JoinColumn(name = "rule_tmp_id") }, inverseJoinColumns = { @JoinColumn(name = "param_tmp_id") })
	private Set<ParamTemplate> paramTemps = new HashSet<ParamTemplate>();
	
	/**
	 * 期望值
	 */
	@Column(name = "FAVER_MSG")
	private String faverMsg;
	
	/**
	 * 实际值
	 */
	@Column(name = "REALITY_MSG")
	private String realityMsg;
	
	public Set<ParamTemplate> getParamTemps() {
		return paramTemps;
	}
	public void setParamTemps(Set<ParamTemplate> paramTemps) {
		this.paramTemps = paramTemps;
	}
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
	public String getRuleCategory() {
		return ruleCategory;
	}
	public void setRuleCategory(String ruleCategory) {
		this.ruleCategory = ruleCategory;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getVallidateType() {
		return vallidateType;
	}
	public void setVallidateType(String vallidateType) {
		this.vallidateType = vallidateType;
	}
	public Integer getInputParamMode() {
		return inputParamMode;
	}
	public void setInputParamMode(Integer inputParamMode) {
		this.inputParamMode = inputParamMode;
	}
	public Integer getExpressionType() {
		return expressionType;
	}
	public void setExpressionType(Integer expressionType) {
		this.expressionType = expressionType;
	}
	public String getExpressionDetail() {
		return expressionDetail;
	}
	public void setExpressionDetail(String expressionDetail) {
		this.expressionDetail = expressionDetail;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRuleDesc() {
		return ruleDesc;
	}
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
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
	public String getMsgZhCnCmp() {
		return msgZhCnCmp;
	}
	public void setMsgZhCnCmp(String msgZhCnCmp) {
		this.msgZhCnCmp = msgZhCnCmp;
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
	
}
