package com.iris.egrant.code.grantsetting.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 申报时间设置表.
 * 
 */
@Entity
@Table(name = "GRANT_SCHEDULE")
public class GrantSchedule implements Serializable {

	private static final long serialVersionUID = -1575805884909036358L;
	@Id
	@Column(name = "GS_CODE")
	@SequenceGenerator(name = "SEQ_GRANT_SCHEDULE", sequenceName = "SEQ_GRANT_SCHEDULE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GRANT_SCHEDULE")
	private Long gsCode;

	// 业务类别code
	@Column(name = "GRANT_CODE")
	private Long grantCode;

	// 申报开始时间
	@Column(name = "START_DATE")
	private Date startDate;

	// 申报截止时间
	@Column(name = "END_DATE")
	private Date endDate;

	// 更新人
	@Column(name = "UPDATE_PSN_CODE")
	private Long updatePsnCode;

	// 更新时间
	@Column(name = "UPDATE_TIME")
	private Date updateDate;

	// 推荐单位code
	@Column(name = "ORG_CODE")
	private Long orgCode;

	// 受理开始时间
	@Column(name = "ORG_START_DATE")
	private Date orgStartDate;

	// 受理截止时间
	@Column(name = "ORG_END_DATE")
	private Date orgEndDate;

	@Column(name = "STAT_YEAR")
	private Long statYear;
	
	@Column(name="REPORT_FLAG")
	private Long reportFlag;
	
	
	public Long getReportFlag() {
		return reportFlag;
	}

	public void setReportFlag(Long reportFlag) {
		this.reportFlag = reportFlag;
	}

	public Long getStatYear() {
		return statYear;
	}

	public void setStatYear(Long statYear) {
		this.statYear = statYear;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "GRANT_CODE", insertable = false, updatable = false)
	private GrantSetting grantSetting;

	public Long getGsCode() {
		return gsCode;
	}

	public void setGsCode(Long gsCode) {
		this.gsCode = gsCode;
	}

	public Long getGrantCode() {
		return grantCode;
	}

	public void setGrantCode(Long grantCode) {
		this.grantCode = grantCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public Long getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}

	public Date getOrgStartDate() {
		return orgStartDate;
	}

	public void setOrgStartDate(Date orgStartDate) {
		this.orgStartDate = orgStartDate;
	}

	public Date getOrgEndDate() {
		return orgEndDate;
	}

	public void setOrgEndDate(Date orgEndDate) {
		this.orgEndDate = orgEndDate;
	}

	public GrantSetting getGrantSetting() {
		return grantSetting;
	}

	public void setGrantSetting(GrantSetting grantSetting) {
		this.grantSetting = grantSetting;
	}

}
