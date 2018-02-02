package com.iris.egrant.code.grantsetting.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 业务类别扩展表.
 */
@Entity
@Table(name = "GRANT_NOTES")
public class GrantNote implements Serializable {

	private static final long serialVersionUID = 2097810619403803503L;

	// 业务类别CODE 主键
	@Id
	@Column(name = "GRANT_CODE")
	private Long grantCode;

	// 申报书样本--pdf文件
	@Column(name = "TITLE")
	private String title;

	// 申报书样本--pdf文件
	@Column(name = "NOTES")
	private String notes;
	// 申报书样本--pdf文件
	@Column(name = "remark")
	private String remark;
	// 发布日期
	@Column(name = "RELEASE_DATE")
	private Date releaseDate;

	// 有效期限
	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "GRANT_CODE", insertable = false, updatable = false)
	private GrantSetting grantSetting;

	public Long getGrantCode() {
		return grantCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setGrantCode(Long grantCode) {
		this.grantCode = grantCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public GrantSetting getGrantSetting() {
		return grantSetting;
	}

	public void setGrantSetting(GrantSetting grantSetting) {
		this.grantSetting = grantSetting;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

}
