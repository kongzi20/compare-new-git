package com.test.example.code.compare.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 申报临时表.
 * 
 * @author zxg
 * 
 */
@Entity
@Table(name = "PROPOSAL_CACHED")
public class ProposalCached implements Serializable {

	private static final long serialVersionUID = 4563769803800825065L;

	// 主键
	@Column(name = "POS_CODE")
	@Id
	@SequenceGenerator(name = "seq_proposal_cached_pos_code", sequenceName = "SEQ_POS_CODE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proposal_cached_pos_code")
	private Long posCode;

	// 业务类别主键
	@Column(name = "GRANT_CODE")
	private Long grantCode;

	// 业务类别Name
	@Column(name = "GRANT_NAME")
	private String grantName;
	// 亚业务类别主键
	@Column(name = "SUB_GRANT_CODE")
	private Long subGrantCode;
	@Column(name = "SUB_GRANT_NAME")
	private String subGrantName;

	// 项目申报人
	@Column(name = "PSN_CODE")
	private Long psnCode;

	@Column(name = "PSN_NAME")
	private String psnName;

	// 项目申报单位
	@Column(name = "ORG_CODE")
	private Long orgCode;
	// 项目管理部门
	@Column(name = "DEPT_CODE")
	private Long deptCode;

	@Column(name = "ORG_NAME")
	private String orgName;

	// 项目名称
	@Column(name = "ZH_TITLE")
	private String zhTitle;

	// 项目英文名称
	@Column(name = "EN_TITLE")
	private String enTitle;

	// 申请书最后更新时间
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	// 申请书模板code
	@Column(name = "FORM_CODE")
	private Long formCode;

	// 申请书版本号
	@Column(name = "PRP_VERSION")
	private String prpVersion;

	// pdf文件code
	@Column(name = "PDF_FILE_CODE")
	private String pdfFileCode;

	public Long getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(Long deptCode) {
		this.deptCode = deptCode;
	}

	// PDF请求打印时间
	@Column(name = "PDF_REQ_TIME")
	private Date pdfReqTime;

	// PDF打印成功时间
	@Column(name = "PDF_GEN_TIME")
	private Date pdfGenTime;

	// PDF状态 1-准备生成 2-进入生成队列 3-生成成功 4-生成失败
	@Column(name = "PDF_STATUS")
	private String pdfStatus;

	// PDF版本号
	@Column(name = "PDF_VERSION")
	private String pdfVersion;

	// 申请书年度
	@Column(name = "STAT_YEAR")
	private String statYear;

	// 申报书状态
	@Column(name = "STATUS")
	private String status;

	// 申报书提交时间
	@Column(name = "SUBMIT_DATE")
	private Date submitDate;

	// 前一个状态
	@Column(name = "OLD_STATUS")
	private String oldStatus;

	// 申请书正文上传后的filecode
	@Column(name = "CONTENT_FILE_CODE")
	private String contentFileCode;

	// 申请书生成pdf后的pdffilecode
	@Column(name = "CONTENT_PDF_FILE_CODE")
	private String contentPdfFileCode;

	// 是否正式PDF 0-草稿pdf 1-正式pdf
	@Column(name = "FLAG_PDF_TYPE")
	private Character flagPdfType;

	// 项目负责人code
	@Column(name = "PRJ_PSN_CODE")
	private Long prjPsnCode;

	// 业务类别第三级code
	@Column(name = "HELP_GRANT_CODE")
	private Long helpGrantCode;

	// 业务类别第三级名称
	@Column(name = "HELP_GRANT_NAME")
	private String helpGrantName;

	// 申报书中文状态名
	@Transient
	private String zh_cn_statusName;

	// 申报书英文状态名
	@Transient
	private String en_us_statusName;

	// 申报书中文繁体状态名
	@Transient
	private String zh_tw_statusName;

	// 初始化信息
	/*@Transient
	private FormBaseLibrary fbl;*/

	// 项目提交人code
	@Column(name = "SUBMIT_PSN_CODE")
	private Long submitPsnCode;

	// 项目创建人code
	@Column(name = "CREATE_PSN_CODE")
	private Long createPsnCode;

	// 项目创建时间
	@Column(name = "CREATE_DATE")
	private Date createDate;

	// 项目更新人
	@Column(name = "UPDATE_PSN_CODE")
	private Long updatePsnCode;
	
	
	// 规则主键
	@Column(name = "EX_CODE")
	private Long exCode;
	
	

	public Long getExCode() {
		return exCode;
	}

	public void setExCode(Long exCode) {
		this.exCode = exCode;
	}

	public Long getPosCode() {
		return posCode;
	}

	public void setPosCode(Long posCode) {
		this.posCode = posCode;
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

	public Long getPsnCode() {
		return psnCode;
	}

	public void setPsnCode(Long psnCode) {
		this.psnCode = psnCode;
	}

	public String getPsnName() {
		return psnName;
	}

	public void setPsnName(String psnName) {
		this.psnName = psnName;
	}

	public Long getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getFormCode() {
		return formCode;
	}

	public void setFormCode(Long formCode) {
		this.formCode = formCode;
	}

	public String getPrpVersion() {
		return prpVersion;
	}

	public void setPrpVersion(String prpVersion) {
		this.prpVersion = prpVersion;
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

	public String getPdfStatus() {
		return pdfStatus;
	}

	public void setPdfStatus(String pdfStatus) {
		this.pdfStatus = pdfStatus;
	}

	public String getPdfVersion() {
		return pdfVersion;
	}

	public void setPdfVersion(String pdfVersion) {
		this.pdfVersion = pdfVersion;
	}

	public String getStatYear() {
		return statYear;
	}

	public void setStatYear(String statYear) {
		this.statYear = statYear;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getContentFileCode() {
		return contentFileCode;
	}

	public void setContentFileCode(String contentFileCode) {
		this.contentFileCode = contentFileCode;
	}

	public String getContentPdfFileCode() {
		return contentPdfFileCode;
	}

	public void setContentPdfFileCode(String contentPdfFileCode) {
		this.contentPdfFileCode = contentPdfFileCode;
	}

	public Character getFlagPdfType() {
		return flagPdfType;
	}

	public void setFlagPdfType(Character flagPdfType) {
		this.flagPdfType = flagPdfType;
	}

	public Long getPrjPsnCode() {
		return prjPsnCode;
	}

	public void setPrjPsnCode(Long prjPsnCode) {
		this.prjPsnCode = prjPsnCode;
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

	public String getZh_cn_statusName() {
		return zh_cn_statusName;
	}

	public void setZh_cn_statusName(String zh_cn_statusName) {
		this.zh_cn_statusName = zh_cn_statusName;
	}

	public String getEn_us_statusName() {
		return en_us_statusName;
	}

	public void setEn_us_statusName(String en_us_statusName) {
		this.en_us_statusName = en_us_statusName;
	}

	public String getZh_tw_statusName() {
		return zh_tw_statusName;
	}

	public void setZh_tw_statusName(String zh_tw_statusName) {
		this.zh_tw_statusName = zh_tw_statusName;
	}

	/*public FormBaseLibrary getFbl() {
		return fbl;
	}

	public void setFbl(FormBaseLibrary fbl) {
		this.fbl = fbl;
	}*/

	public Long getSubmitPsnCode() {
		return submitPsnCode;
	}

	public void setSubmitPsnCode(Long submitPsnCode) {
		this.submitPsnCode = submitPsnCode;
	}

	public Long getCreatePsnCode() {
		return createPsnCode;
	}

	public void setCreatePsnCode(Long createPsnCode) {
		this.createPsnCode = createPsnCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUpdatePsnCode() {
		return updatePsnCode;
	}

	public void setUpdatePsnCode(Long updatePsnCode) {
		this.updatePsnCode = updatePsnCode;
	}

}
