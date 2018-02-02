package com.iris.egrant.code.project.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

/**
 * 项目表.
 * 
 */
@Entity
@Table(name = "PROJECT")
public class Project implements Serializable {

	private static final long serialVersionUID = 7565633676318865557L;
	@Id
	@Column(name = "PRJ_CODE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PROJECT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	private Long prjCode;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	private ProjectExtend projectExtend;

	@Column(name = "ZH_TITLE")
	private String zhTitle;

	@Column(name = "EN_TITLE")
	private String enTitle;

	@Column(name = "PRJ_NO")
	private String prjNo;

	@Column(name = "PSN_CODE")
	private Long psnCode;

	@Column(name = "DEPT_CODE")
	private Long deptCode;

	@Column(name = "ORG_CODE")
	private Long orgCode;

	@Column(name = "GRANT_CODE")
	private Long grantCode;

	@Column(name = "GRANT_NAME")
	private String grantName;

	@Column(name = "SUBJECT_CODE1")
	private String subjectCode1;

	@Column(name = "AWARD_AMT")
	private BigDecimal totalAmt;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "FLAG_SHI_ZHI")
	private String flagShiZhi;

	@Column(name = "END_DATE")
	private Date endDate;
	@Column(name = "PERFORMANCE_START_DATE")
	private Date performanceStartDate;

	@Column(name = "PERFORMANCE_END_DATE")
	private Date performanceEndDate;
	
	@Column(name = "BANK_ORGNAME")
	private String bankOrgName;
	
	@Column(name = "BANK_NAME")
	private String bankName;
	@Column(name = "BANK_ACCOUNT")
	private String bankAccount;
	
	

	public String getBankOrgName() {
		return bankOrgName;
	}

	public void setBankOrgName(String bankOrgName) {
		this.bankOrgName = bankOrgName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Date getPerformanceStartDate() {
		return performanceStartDate;
	}

	public void setPerformanceStartDate(Date performanceStartDate) {
		this.performanceStartDate = performanceStartDate;
	}

	public Date getPerformanceEndDate() {
		return performanceEndDate;
	}

	public void setPerformanceEndDate(Date performanceEndDate) {
		this.performanceEndDate = performanceEndDate;
	}

	@Column(name = "HIGH_TECH_NO")
	private String highTechNo;

	@Column(name = "PRP_NO")
	private String prpNo;

	@Column(name = "PROJNATURE")
	private Character projNature;

	@Column(name = "CSUMMARY")
	private String cSummary;

	@Column(name = "ESUMMARY")
	private String eSummary;

	@Column(name = "STAT_YEAR")
	private String statYear;

	@Column(name = "EXPCOMPLETIONDATE")
	private Date expCompletionDate;

	@Column(name = "ACTCOMPLETIONDATE")
	private Date actCompletionDate;

	@Column(name = "DURATION")
	private Integer duration;

	@Column(name = "SUBJECT_CODE2")
	private String subjectCode2;

	@Column(name = "DIVNO")
	private String divNo;

	@Column(name = "DEPNO")
	private String depNo;

	@Column(name = "MIS_XM_ID")
	private Integer misXmId;

	@Column(name = "MIS_XMZT")
	private Integer misXmzt;

	@Column(name = "GRANTTYPEID")
	private String granttypeid;

	@Column(name = "GRANT_TYPE_ID")
	private String grantTypeId;

	@Column(name = "SUB_GRANT_TYPE_ID")
	private String subGrantTypeId;

	@Column(name = "GRANT_DESCRIPTION")
	private String grantDescription;

	@Column(name = "PSN_NAME")
	private String psnName;

	@Column(name = "STATUSCODE")
	private String statuscode;

	@Column(name = "SUB_GRANT_CODE")
	private Long subGrantCode;

	@Column(name = "SUB_GRANT_NAME")
	private String subGrantName;

	@Column(name = "HELP_GRANT_CODE")
	private Long helpGrantCode;

	@Column(name = "HELP_GRANT_NAME")
	private String helpGrantName;

	@Column(name = "PRJ_XML")
	@Type(type = "com.iris.egrant.core.dao.orm.OracleXmlType")
	private Document prjXml;

	@Column(name = "IS_RESTORE")
	private Long isRestore;

	@Column(name = "RESTORE_DATE")
	private Date restoreDate;

	@Column(name = "PRP_XML")
	@Type(type = "com.iris.egrant.core.dao.orm.OracleXmlType")
	private Document prpXml;

	@Column(name = "PRP_CODE")
	private Long prpCode;// NUMBER(18) Y 对应申请书主键proposal.prp_code

	@Column(name = "POS_CODE")
	private Long posCode;// NUMBER(18) Y 对应申请书主键proposalCache.pos_code

	@Column(name = "STATUS")
	private String status; // 项目状态

	@Column(name = "LIABLE_PSN_CODE")
	private Long liablePsnCode; // 立项责任人

	@Column(name = "RPT_GEN_STATUS")
	private String rptGenStatus;

	@Column(name = "IS_HISTORY_PRJ")
	private String isHistoryPrj;

	@Column(name = "HIS_ORG_NO")
	private String hisOrgNo;

	@Column(name = "HIS_ORG_NAME")
	private String hisOrgName;

	@Column(name = "HIS_PSN_NAME")
	private String hisPsnName;

	@Column(name = "CONTACT_PSN_EMAIL")
	private String contactPsnEmail;// 项目联系人邮箱

	@Column(name = "CONTACT_PSN_CNAME")
	private String contactPsnCname;// 项目联系人名称

	@Column(name = "CONTACT_PSN_MOBILE")
	private String contactPsnMoblie;// 项目联系人手机

	@Column(name = "PAY_CHANNEL")
	private String payChannel; // 拨款渠道,默认为空,表示市本级直拨(0或空)，区（市）县拨付(1)

	public String getContactPsnEmail() {
		return contactPsnEmail;
	}

	public void setContactPsnEmail(String contactPsnEmail) {
		this.contactPsnEmail = contactPsnEmail;
	}

	public String getContactPsnCname() {
		return contactPsnCname;
	}

	public void setContactPsnCname(String contactPsnCname) {
		this.contactPsnCname = contactPsnCname;
	}

	public String getContactPsnMoblie() {
		return contactPsnMoblie;
	}

	public void setContactPsnMoblie(String contactPsnMoblie) {
		this.contactPsnMoblie = contactPsnMoblie;
	}

	public Long getPrjCode() {
		return prjCode;
	}

	public void setPrjCode(Long prjCode) {
		this.prjCode = prjCode;
	}

	public ProjectExtend getProjectExtend() {
		return projectExtend;
	}

	public void setProjectExtend(ProjectExtend projectExtend) {
		this.projectExtend = projectExtend;
	}

	public String getZhTitle() {
		return zhTitle;
	}

	public void setZhTitle(String zhTitle) {
		this.zhTitle = zhTitle;
	}

	public String getEnTitle() {
		return enTitle;
	}

	public void setEnTitle(String enTitle) {
		this.enTitle = enTitle;
	}

	public String getPrjNo() {
		return prjNo;
	}

	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	public Long getPsnCode() {
		return psnCode;
	}

	public void setPsnCode(Long psnCode) {
		this.psnCode = psnCode;
	}

	public Long getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(Long deptCode) {
		this.deptCode = deptCode;
	}

	public Long getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}

	public Long getGrantCode() {
		return grantCode;
	}

	public void setGrantCode(Long grantCode) {
		this.grantCode = grantCode;
	}

	public String getGrantName() {
		return grantName;
	}

	public void setGrantName(String grantName) {
		this.grantName = grantName;
	}

	public String getSubjectCode1() {
		return subjectCode1;
	}

	public void setSubjectCode1(String subjectCode1) {
		this.subjectCode1 = subjectCode1;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
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

	public String getHighTechNo() {
		return highTechNo;
	}

	public void setHighTechNo(String highTechNo) {
		this.highTechNo = highTechNo;
	}

	public String getPrpNo() {
		return prpNo;
	}

	public void setPrpNo(String prpNo) {
		this.prpNo = prpNo;
	}

	public Character getProjNature() {
		return projNature;
	}

	public void setProjNature(Character projNature) {
		this.projNature = projNature;
	}

	public String getcSummary() {
		return cSummary;
	}

	public void setcSummary(String cSummary) {
		this.cSummary = cSummary;
	}

	public String geteSummary() {
		return eSummary;
	}

	public void seteSummary(String eSummary) {
		this.eSummary = eSummary;
	}

	public String getStatYear() {
		return statYear;
	}

	public void setStatYear(String statYear) {
		this.statYear = statYear;
	}

	public Date getExpCompletionDate() {
		return expCompletionDate;
	}

	public void setExpCompletionDate(Date expCompletionDate) {
		this.expCompletionDate = expCompletionDate;
	}

	public Date getActCompletionDate() {
		return actCompletionDate;
	}

	public void setActCompletionDate(Date actCompletionDate) {
		this.actCompletionDate = actCompletionDate;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getSubjectCode2() {
		return subjectCode2;
	}

	public void setSubjectCode2(String subjectCode2) {
		this.subjectCode2 = subjectCode2;
	}

	public String getDivNo() {
		return divNo;
	}

	public void setDivNo(String divNo) {
		this.divNo = divNo;
	}

	public String getDepNo() {
		return depNo;
	}

	public void setDepNo(String depNo) {
		this.depNo = depNo;
	}

	public Integer getMisXmId() {
		return misXmId;
	}

	public void setMisXmId(Integer misXmId) {
		this.misXmId = misXmId;
	}

	public Integer getMisXmzt() {
		return misXmzt;
	}

	public void setMisXmzt(Integer misXmzt) {
		this.misXmzt = misXmzt;
	}

	public String getGranttypeid() {
		return granttypeid;
	}

	public void setGranttypeid(String granttypeid) {
		this.granttypeid = granttypeid;
	}

	public String getGrantTypeId() {
		return grantTypeId;
	}

	public void setGrantTypeId(String grantTypeId) {
		this.grantTypeId = grantTypeId;
	}

	public String getSubGrantTypeId() {
		return subGrantTypeId;
	}

	public void setSubGrantTypeId(String subGrantTypeId) {
		this.subGrantTypeId = subGrantTypeId;
	}

	public String getGrantDescription() {
		return grantDescription;
	}

	public void setGrantDescription(String grantDescription) {
		this.grantDescription = grantDescription;
	}

	public String getPsnName() {
		return psnName;
	}

	public void setPsnName(String psnName) {
		this.psnName = psnName;
	}

	public String getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}

	public Long getSubGrantCode() {
		return subGrantCode;
	}

	public void setSubGrantCode(Long subGrantCode) {
		this.subGrantCode = subGrantCode;
	}

	public String getSubGrantName() {
		return subGrantName;
	}

	public void setSubGrantName(String subGrantName) {
		this.subGrantName = subGrantName;
	}

	public Long getHelpGrantCode() {
		return helpGrantCode;
	}

	public void setHelpGrantCode(Long helpGrantCode) {
		this.helpGrantCode = helpGrantCode;
	}

	public String getHelpGrantName() {
		return helpGrantName;
	}

	public void setHelpGrantName(String helpGrantName) {
		this.helpGrantName = helpGrantName;
	}

	public Document getPrjXml() {
		return prjXml;
	}

	public void setPrjXml(Document prjXml) {
		this.prjXml = prjXml;
	}

	public Long getIsRestore() {
		return isRestore;
	}

	public void setIsRestore(Long isRestore) {
		this.isRestore = isRestore;
	}

	public Date getRestoreDate() {
		return restoreDate;
	}

	public void setRestoreDate(Date restoreDate) {
		this.restoreDate = restoreDate;
	}

	public Document getPrpXml() {
		return prpXml;
	}

	public void setPrpXml(Document prpXml) {
		this.prpXml = prpXml;
	}

	public Long getPrpCode() {
		return prpCode;
	}

	public void setPrpCode(Long prpCode) {
		this.prpCode = prpCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLiablePsnCode() {
		return liablePsnCode;
	}

	public void setLiablePsnCode(Long liablePsnCode) {
		this.liablePsnCode = liablePsnCode;
	}

	public String getRptGenStatus() {
		return rptGenStatus;
	}

	public void setRptGenStatus(String rptGenStatus) {
		this.rptGenStatus = rptGenStatus;
	}

	public String getIsHistoryPrj() {
		return isHistoryPrj;
	}

	public void setIsHistoryPrj(String isHistoryPrj) {
		this.isHistoryPrj = isHistoryPrj;
	}

	public String getHisOrgNo() {
		return hisOrgNo;
	}

	public void setHisOrgNo(String hisOrgNo) {
		this.hisOrgNo = hisOrgNo;
	}

	public String getHisOrgName() {
		return hisOrgName;
	}

	public void setHisOrgName(String hisOrgName) {
		this.hisOrgName = hisOrgName;
	}

	public String getHisPsnName() {
		return hisPsnName;
	}

	public void setHisPsnName(String hisPsnName) {
		this.hisPsnName = hisPsnName;
	}

	public Long getPosCode() {
		return posCode;
	}

	public void setPosCode(Long posCode) {
		this.posCode = posCode;
	}

	public String getFlagShiZhi() {
		return flagShiZhi;
	}

	public void setFlagShiZhi(String flagShiZhi) {
		this.flagShiZhi = flagShiZhi;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

}
