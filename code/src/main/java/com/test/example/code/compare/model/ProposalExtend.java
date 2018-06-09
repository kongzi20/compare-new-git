package com.test.example.code.compare.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

/**
 * 申报扩展表.
 * 
 * @author yamingd
 * 
 */
@Entity
@Table(name = "PROPOSAL_EXTEND")
public class ProposalExtend implements Serializable {

	private static final long serialVersionUID = 6274386620449682540L;

	// 主键
	@Id
	@Column(name = "PRP_CODE")
	private Long prpCode;

	// 申请书版本
	@Column(name = "PRP_VERSON")
	private String prpVerson;

	// 申请书xml
	@Column(name = "PRP_XML")
	@Type(type = "com.test.example.core.dao.orm.OracleXmlType")
	private Document prpXML;

	// 申请书pdf版本号
	@Column(name = "PDF_VERSON")
	private String pdfVerson;

	// 申请书pdf状态
	@Column(name = "PDF_STATUS")
	private String pdfStatus;

	// 申请书pdf文件主键
	@Column(name = "PDF_FILE_CODE")
	private String pdfFileCode;

	// 申请书pdf打印请求时间
	@Column(name = "PDF_REQ_TIME")
	private Date pdfReqTime;

	// 申请书pdf生成时间
	@Column(name = "PDF_GEN_TIME")
	private Date pdfGenTime;

	// 生产疑似单位数据时承担单位xml
	@Column(name = "COMPARE_ORG_XML")
	@Type(type = "com.test.example.core.dao.orm.OracleXmlType")
	private Document compareOrgXml;

	// 对比状态
	@Column(name = "COMPARE_ORG_STATUS")
	private Integer compareOrgStatus;

	// 请求时间
	@Column(name = "COMPARE_ORG_REQUEST_DATE")
	private Date compareOrgRequestDate;

	// 完成时间
	@Column(name = "COMPARE_ORG_FINISH_DATE")
	private Date compareOrgFinishDate;

	// 疑似项目数量
	@Column(name = "COMPARE_PRP_FAMILIAR_CNT")
	private Long comparePrpFamiliarCnt;

	// 疑似单位数量 取消疑似时需要更新此字段
	@Column(name = "COMPARE_ORG_FAMILIAR_CNT")
	private Long compareOrgFamiliarCnt;

	// 违反规则数量
	@Column(name = "COMPARE_RULE_FAMILIAR_CNT")
	private Long compareRuleFamiliarCnt;

	// 疑似单位数量，用于校正/忽略
	@Column(name = "COMPARE_ORG_CNT2")
	private Long compareOrgCnt2;
	
	// 疑似项目数量，用于校正/忽略
	@Column(name = "COMPARE_PRP_CNT2")
	private Long comparePrpCnt2;

	// 违反规则数量，用于校正/忽略
	@Column(name = "COMPARE_RULE_CNT2")
	private Long compareRuleCnt2;
	
	@Column(name = "BELONG_INDUSTRY")
	private String belongIndustry;
	
	@Column(name = "BELONG_INDUSTRY_NAME")
	private String belongIndustryName;
	
	public Long getPrpCode() {
		return prpCode;
	}

	public void setPrpCode(Long prpCode) {
		this.prpCode = prpCode;
	}

	public String getPrpVerson() {
		return prpVerson;
	}

	public void setPrpVerson(String prpVerson) {
		this.prpVerson = prpVerson;
	}

	public Document getPrpXML() {
		return prpXML;
	}

	public void setPrpXML(Document prpXML) {
		this.prpXML = prpXML;
	}

	public String getPdfVerson() {
		return pdfVerson;
	}

	public void setPdfVerson(String pdfVerson) {
		this.pdfVerson = pdfVerson;
	}

	public String getPdfStatus() {
		return pdfStatus;
	}

	public void setPdfStatus(String pdfStatus) {
		if (pdfStatus == null) {
			this.pdfStatus = null;
		} else {
			this.pdfStatus = pdfStatus;
		}
	}

	public String getPdfFileCode() {
		return pdfFileCode;
	}

	public void setPdfFileCode(String pdfFileCode) {
		this.pdfFileCode = pdfFileCode;
	}

	public Date getPdfReqTime() {
		return pdfReqTime;
	}

	public void setPdfReqTime(Date pdfReqTime) {
		this.pdfReqTime = pdfReqTime;
	}

	public Date getPdfGenTime() {
		return pdfGenTime;
	}

	public void setPdfGenTime(Date pdfGenTime) {
		this.pdfGenTime = pdfGenTime;
	}

	public Document getCompareOrgXml() {
		return compareOrgXml;
	}

	public void setCompareOrgXml(Document compareOrgXml) {
		this.compareOrgXml = compareOrgXml;
	}

	public Integer getCompareOrgStatus() {
		return compareOrgStatus;
	}

	public void setCompareOrgStatus(Integer compareOrgStatus) {
		this.compareOrgStatus = compareOrgStatus;
	}

	public Date getCompareOrgRequestDate() {
		return compareOrgRequestDate;
	}

	public void setCompareOrgRequestDate(Date compareOrgRequestDate) {
		this.compareOrgRequestDate = compareOrgRequestDate;
	}

	public Date getCompareOrgFinishDate() {
		return compareOrgFinishDate;
	}

	public void setCompareOrgFinishDate(Date compareOrgFinishDate) {
		this.compareOrgFinishDate = compareOrgFinishDate;
	}

	public Long getComparePrpFamiliarCnt() {
		return comparePrpFamiliarCnt;
	}

	public void setComparePrpFamiliarCnt(Long comparePrpFamiliarCnt) {
		this.comparePrpFamiliarCnt = comparePrpFamiliarCnt;
	}

	public Long getCompareOrgFamiliarCnt() {
		return compareOrgFamiliarCnt;
	}

	public void setCompareOrgFamiliarCnt(Long compareOrgFamiliarCnt) {
		this.compareOrgFamiliarCnt = compareOrgFamiliarCnt;
	}

	public Long getCompareRuleFamiliarCnt() {
		return compareRuleFamiliarCnt;
	}

	public void setCompareRuleFamiliarCnt(Long compareRuleFamiliarCnt) {
		this.compareRuleFamiliarCnt = compareRuleFamiliarCnt;
	}

	public Long getCompareOrgCnt2() {
		return compareOrgCnt2;
	}

	public void setCompareOrgCnt2(Long compareOrgCnt2) {
		this.compareOrgCnt2 = compareOrgCnt2;
	}

	public Long getComparePrpCnt2() {
		return comparePrpCnt2;
	}

	public void setComparePrpCnt2(Long comparePrpCnt2) {
		this.comparePrpCnt2 = comparePrpCnt2;
	}

	public Long getCompareRuleCnt2() {
		return compareRuleCnt2;
	}

	public void setCompareRuleCnt2(Long compareRuleCnt2) {
		this.compareRuleCnt2 = compareRuleCnt2;
	}

	public String getBelongIndustry() {
		return belongIndustry;
	}

	public void setBelongIndustry(String belongIndustry) {
		this.belongIndustry = belongIndustry;
	}

	public String getBelongIndustryName() {
		return belongIndustryName;
	}

	public void setBelongIndustryName(String belongIndustryName) {
		this.belongIndustryName = belongIndustryName;
	}

}
