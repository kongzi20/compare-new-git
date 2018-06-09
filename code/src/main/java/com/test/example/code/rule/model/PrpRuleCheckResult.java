package com.test.example.code.rule.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 申请规则比对结果
 * 
 */
@Entity
@Table(name = "PRP_RULE_CHECK_RESULT")
public class PrpRuleCheckResult implements Serializable {

	private static final long serialVersionUID = 2572723701524671511L;

	@Id
	private PrpRuleCheckResultKey pkey;

	// @Id
	// @Column(name = "PRP_CODE")
	// private Long prpCode;
	//
	// @Id
	// @Column(name = "RULE_CODE")
	// private Long ruleCode;

	@Column(name = "RESULT_DESC")
	private String resultDesc;

	@Column(name = "VALLIDATE_TYPE")
	private String vallidateType;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "IS_RESTORE")
	private String isRestore;

	@Column(name = "IS_CONFIRM")
	private String isConfirm;
	
	@Column(name = "KEY_CODES")
	private String keyCodes;
	
	@Column(name = "WARN_LEVEL")
	private String warnLevel;

	public String getWarnLevel() {
		return warnLevel;
	}

	public void setWarnLevel(String warnLevel) {
		this.warnLevel = warnLevel;
	}

	public String getKeyCodes() {
		return keyCodes;
	}

	public void setKeyCodes(String keyCodes) {
		this.keyCodes = keyCodes;
	}

	@Column(name = "RESTORE_REQUEST_DATE")
	private Date restoreRequestDate;

	@Column(name = "RESTORE_FINISH_DATE")
	private Date restoreFinishDate;

	@Column(name = "CONFIRM_DATE")
	private Date confirmDate;

	@Column(name = "CONFIRM_PSN")
	private Long confirmPsn;

	@Column(name = "CONFIRM_REMARK")
	private String confirmRemark;

	@Column(name = "USER_CUSTOM_VALUES")
	private String userCustomValues;

	@Column(name = "SYS_CUSTOM_VALUES")
	private String sysCustomValues;

	
	/**
     * 规则对比中文信息
     */
	@Column(name = "FAVER_MSG")
	private String faverMsg;
	/**
     * 规则对比中文信息
     */
	@Column(name = "REALITY_MSG")
	private String realityMsg;
	// public Long getPrpCode() {
	// return prpCode;
	// }
	//
	// public void setPrpCode(Long prpCode) {
	// this.prpCode = prpCode;
	// }
	//
	// public Long getRuleCode() {
	// return ruleCode;
	// }
	//
	// public void setRuleCode(Long ruleCode) {
	// this.ruleCode = ruleCode;
	// }

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

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getVallidateType() {
		return vallidateType;
	}

	public void setVallidateType(String vallidateType) {
		this.vallidateType = vallidateType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsRestore() {
		return isRestore;
	}

	public void setIsRestore(String isRestore) {
		this.isRestore = isRestore;
	}

	public String getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(String isConfirm) {
		this.isConfirm = isConfirm;
	}

	public Date getRestoreRequestDate() {
		return restoreRequestDate;
	}

	public void setRestoreRequestDate(Date restoreRequestDate) {
		this.restoreRequestDate = restoreRequestDate;
	}

	public Date getRestoreFinishDate() {
		return restoreFinishDate;
	}

	public void setRestoreFinishDate(Date restoreFinishDate) {
		this.restoreFinishDate = restoreFinishDate;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Long getConfirmPsn() {
		return confirmPsn;
	}

	public void setConfirmPsn(Long confirmPsn) {
		this.confirmPsn = confirmPsn;
	}

	public String getConfirmRemark() {
		return confirmRemark;
	}

	public void setConfirmRemark(String confirmRemark) {
		this.confirmRemark = confirmRemark;
	}

	public String getUserCustomValues() {
		return userCustomValues;
	}

	public void setUserCustomValues(String userCustomValues) {
		this.userCustomValues = userCustomValues;
	}

	public String getSysCustomValues() {
		return sysCustomValues;
	}

	public void setSysCustomValues(String sysCustomValues) {
		this.sysCustomValues = sysCustomValues;
	}

	public PrpRuleCheckResultKey getPkey() {
		return pkey;
	}

	public void setPkey(PrpRuleCheckResultKey pkey) {
		this.pkey = pkey;
	}

	

}
