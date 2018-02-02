package com.iris.egrant.code.grantsetting.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "GRANT_SETTING_EXTEND")
public class GrantSettingExtend implements Serializable {

	private static final long serialVersionUID = 2097810619403803503L;

	// 业务类别CODE 主键
	@Id
	@Column(name = "GRANT_CODE")
	private Long grantCode;

	// 申报书样本--pdf文件
	@Column(name = "SAMPLE_PDF")
	private String samplePdf;

	// 申报书样本-word文件
	@Column(name = "SAMPLE_WORD")
	private String sampleWord;

	// 责任人
	@Column(name = "LIABLE_PSN_CODE")
	private Long liablePsnCode;

	// 首页显示的指南的链接
	@Column(name = "GUIDE_URL")
	private String guideUrl;

	// 是否需要区县审核，1是0否
	@Column(name = "NEED_APPROVE")
	private Character needApprove;
	
	// 备注
	@Column(name = "NOTE")
	private String note;
	
	//基金类别字段

	@Column(name = "FUND_OFFICE_CODE")
	private String fundOfficeCode;	//受托机构code，多个以","分隔
	
	@Column(name = "MAGNIFICATION")
	private BigDecimal magnification;	//放大倍数

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "GRANT_CODE", insertable = false, updatable = false)
	private GrantSetting grantSetting;

	public Long getGrantCode() {
		return grantCode;
	}

	public void setGrantCode(Long grantCode) {
		this.grantCode = grantCode;
	}

	public String getSamplePdf() {
		return samplePdf;
	}

	public void setSamplePdf(String samplePdf) {
		this.samplePdf = samplePdf;
	}

	public String getSampleWord() {
		return sampleWord;
	}

	public void setSampleWord(String sampleWord) {
		this.sampleWord = sampleWord;
	}

	public Long getLiablePsnCode() {
		return liablePsnCode;
	}

	public void setLiablePsnCode(Long liablePsnCode) {
		this.liablePsnCode = liablePsnCode;
	}

	public String getGuideUrl() {
		return guideUrl;
	}

	public void setGuideUrl(String guideUrl) {
		this.guideUrl = guideUrl;
	}

	public Character getNeedApprove() {
		return needApprove;
	}

	public void setNeedApprove(Character needApprove) {
		this.needApprove = needApprove;
	}

	public GrantSetting getGrantSetting() {
		return grantSetting;
	}

	public void setGrantSetting(GrantSetting grantSetting) {
		this.grantSetting = grantSetting;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFundOfficeCode() {
		return fundOfficeCode;
	}

	public void setFundOfficeCode(String fundOfficeCode) {
		this.fundOfficeCode = fundOfficeCode;
	}

	public BigDecimal getMagnification() {
		return magnification;
	}

	public void setMagnification(BigDecimal magnification) {
		this.magnification = magnification;
	}
	
}
