package com.iris.egrant.code.grantsetting.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 业务类别表.
 * 
 * @author yamingd
 * 
 */
@Entity
@Table(name = "GRANT_SETTING")
public class GrantSetting implements Serializable {

	private static final long serialVersionUID = 6274386620449682540L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_grant_code")
	@GenericGenerator(name = "seq_grant_code", strategy = "com.iris.egrant.core.dao.hibernate.AssignedSequenceGenerator", parameters = { @Parameter(name = "sequence", value = "seq_grant_code") })
	@Column(name = "GRANT_CODE")
	private Long grantCode;

	// 业务类别名称
	@Column(name = "ZH_CN_GRANT_NAME")
	private String zhCnGrantName;

	// 业务类别繁体名称
	@Column(name = "ZH_TW_GRANT_NAME")
	private String zhTwGrantName;

	// 业务类别英文名称
	@Column(name = "EN_US_GRANT_NAME")
	private String enUsGrantName;
	
	@Column(name = "create_date")
	private Date createDate;

	// 业务类别代码（基金委资助类别代码，科技厅也有用到）
	@Column(name = "GRANT_NO")
	private String grantNo;

	// 排序使用
	@Column(name = "SEQ_NO")
	private Integer seqNo;

	// 是否启用
	@Column(name = "ENABLED")
	private Integer enabled;

	// 父节点
	@Column(name = "PARENT_CODE")
	private Long parentCode;

	// 是否需要依托项目
	@Column(name = "NEEDPRJ")
	private Character needPrj;
	// 是否需要合同
	@Column(name = "NEED_CTR")
	private Character needCtr;
	// 是否需要验收
	@Column(name = "NEED_COMPLETE")
	private Character needComplete;
	// 是否需要验收
	@Column(name = "NEED_PROGRESS")
	private Character needProgress;
	// 支持对象
	@Column(name = "SPONSOR_TARGET")
	private String sponsorTarget;
	// 支持节点
	@Column(name = "SPONSOR_PHASE")
	private String sponsorPhase;
	// 支持区域
	@Column(name = "SPONSOR_AREA")
	private String sponsorArea;
	// 支持类别
	@Column(name = "SPONSOR_SORT")
	private String sponsorSort;
	// 支持周期
	@Column(name = "SPONSOR_DURATION")
	private String sponsorDuration;
	// 支持周期
	@Column(name = "FLAG_LIMIT")
	private String flagLimit;
	// 支持周期
	@Column(name = "LIMIT_AMT")
	private Double limitAmt;

	// 业务类别级别
	@Column(name = "GRANT_LEVEL")
	private Integer grantLevel;

	// poposal，申请书模板的编号
	@Column(name = "PRP_FORM_ID")
	private Long prpFormId;

	// contract，合同书模板的编号
	@Column(name = "CTR_FORM_ID")
	private Long ctrFormId;

	// acceptance，验收模板的编号
	@Column(name = "APT_FORM_ID")
	private Long aptFormId;

	// project，项目模板的编号
	@Column(name = "PRJ_FORM_ID")
	private Long prjFormId;

	// report，报表模板的编号
	@Column(name = "RPT_FORM_ID")
	private Long rptFormId;
	// report，报表模板的编号
	@Column(name = "HAS_CHILD")
	private Long hasChild;

	// 标识经费项目
	@Column(name = "FUND_FLAG")
	private String fundFlag = "0";
	@Column(name = "is_show")
	private Long is_show;

	// 1为资金,0为备案,2投资,3贷款
	@Column(name = "PRP_TYPE")
	private String prpType;

	public String getPrpType() {
		return prpType;
	}

	public void setPrpType(String prpType) {
		this.prpType = prpType;
	}

	public Long getHasChild() {
		return hasChild;
	}

	public void setHasChild(Long hasChild) {
		this.hasChild = hasChild;
	}

	public Long getIs_show() {
		return is_show;
	}

	public void setIs_show(Long is_show) {
		this.is_show = is_show;
	}

	// 默认管理处室
	@Column(name = "DEFAULT_ADMIN_ORG_CODE")
	private Long defaultAdminOrgCode;

	@Column(name = "default_admin_office_code")
	private Long defaultAdminOfficeCode;

	// 默认指派的中介机构
	@Column(name = "DEFAULT_ASSIGN_ORG_CODE")
	private Long defaultAssignOrgCode;

	// 申报开始时间
	@Transient
	private String startDate;
	// 申报结束时间
	@Transient
	private String endDate;
	@Transient
	private String message;
	@Transient
	private String father;
	// 禁止申报的错误消息
	@Transient
	private List<String> errMsgList;

	// 进展报告填写模版code，rpt_progress表
	@Column(name = "JINZHAN_FORM_CODE")
	private Long jinzhanFormCode;

	// 中期报告填写模版code，rpt_progress表
	@Column(name = "ZHONGQI_FORM_CODE")
	private Long zhongqiFormCode;

	// 总结报告填写模版code，rpt_completion表
	@Column(name = "ZONGJIE_FORM_CODE")
	private Long zongjieFormCode;

	// 结题报告填写模版code，rpt_completion表
	@Column(name = "JIETI_FORM_CODE")
	private Long jietiFormCode;

	// 成果研究报告填写模版code，rpt_deliverable表
	@Column(name = "CHENGGUO_FORM_CODE")
	private Long chengguoFormCode;

	// 管理方式
	@Column(name = "MANAGE_CATEGORY")
	private String managecategory;

	// 支持方式
	@Column(name = "SPONSOR_CATEGORY")
	private String sponsor_category;

	// 更新时间
	@Column(name = "UPDATE_DATE")
	private Date UPDATE_DATE;

	@Column(name = "FUND_CODE")
	private Long fundCode;

	@Column(name = "FIN_ORG_CODE")
	private Long finOrgCode;

	@Column(name = "APPLY_TYPE")
	private String applyType;

	// 立项级次
	@Column(name = "AMT_GRADE")
	private String amtGrade;

	// 立项部门
	@Column(name = "PRJ_OFFICE_CODE")
	private Long prjOfficeCode;

	// 基金类别
	@Column(name = "SF_CODE")
	private Long sfCode;

	// 地区编号，区级项目专用
	@Column(name = "AREA_NO")
	private String areaNo;

	// 财政管理部门
	@Column(name = "FIN_OFFICE_CODE")
	private Long finOfficeCode;

	// 是否属于流动资金
	@Column(name = "IS_LDZJ")
	private Long isLdzj = 0L;

	// 是否属于担保资金
	@Column(name = "IS_DBZJ")
	private Long isDbzj = 0L;
	
	@Column(name = "NEED_PRP_COMPARE")
	private String needPrpCompare = "1";
	
	@Column(name = "SPONSOR_DURATION_YEAR")
	private Integer sponsorDurationYear = 0;

	@Transient
	private boolean able;

	@OneToOne(mappedBy = "grantSetting",cascade = { CascadeType.ALL})
	private GrantSchedule grantSchedule;

	@OneToOne(mappedBy = "grantSetting",cascade = { CascadeType.ALL})
	private GrantSettingExtend grantSettingExtend;

	@OneToOne(mappedBy = "grantSetting",cascade = { CascadeType.ALL})
	private GrantNote grantNote;
	@Transient
	private FundSetting fundSetting;

	public Long getGrantCode() {
		return grantCode;
	}

	public void setGrantCode(Long grantCode) {
		this.grantCode = grantCode;
	}

	public String getGrantNo() {
		return grantNo;
	}

	public void setGrantNo(String grantNo) {
		this.grantNo = grantNo;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getParentCode() {
		return parentCode;
	}

	public void setParentCode(Long parentCode) {
		this.parentCode = parentCode;
	}

	public Character getNeedPrj() {
		return needPrj;
	}

	public void setNeedPrj(Character needPrj) {
		this.needPrj = needPrj;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public GrantSchedule getGrantSchedule() {
		return grantSchedule;
	}

	public void setGrantSchedule(GrantSchedule grantSchedule) {
		this.grantSchedule = grantSchedule;
	}

	public List<String> getErrMsgList() {
		return errMsgList;
	}

	public void setErrMsgList(List<String> errMsgList) {
		this.errMsgList = errMsgList;
	}

	public Integer getGrantLevel() {
		return grantLevel;
	}

	public void setGrantLevel(Integer grantLevel) {
		this.grantLevel = grantLevel;
	}

	public boolean isAble() {
		return able;
	}

	public void setAble(boolean able) {
		this.able = able;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getZhCnGrantName() {
		return zhCnGrantName;
	}

	public void setZhCnGrantName(String zhCnGrantName) {
		this.zhCnGrantName = zhCnGrantName;
	}

	public String getZhTwGrantName() {
		return zhTwGrantName;
	}

	public void setZhTwGrantName(String zhTwGrantName) {
		this.zhTwGrantName = zhTwGrantName;
	}

	public String getEnUsGrantName() {
		return enUsGrantName;
	}

	public void setEnUsGrantName(String enUsGrantName) {
		this.enUsGrantName = enUsGrantName;
	}

	public Long getPrpFormId() {
		return prpFormId;
	}

	public void setPrpFormId(Long prpFormId) {
		this.prpFormId = prpFormId;
	}

	public Long getCtrFormId() {
		return ctrFormId;
	}

	public void setCtrFormId(Long ctrFormId) {
		this.ctrFormId = ctrFormId;
	}

	public Long getAptFormId() {
		return aptFormId;
	}

	public void setAptFormId(Long aptFormId) {
		this.aptFormId = aptFormId;
	}

	public Long getPrjFormId() {
		return prjFormId;
	}

	public void setPrjFormId(Long prjFormId) {
		this.prjFormId = prjFormId;
	}

	public Long getRptFormId() {
		return rptFormId;
	}

	public void setRptFormId(Long rptFormId) {
		this.rptFormId = rptFormId;
	}

	public Long getDefaultAdminOrgCode() {
		return defaultAdminOrgCode;
	}

	public void setDefaultAdminOrgCode(Long defaultAdminOrgCode) {
		this.defaultAdminOrgCode = defaultAdminOrgCode;
	}

	public Long getDefaultAssignOrgCode() {
		return defaultAssignOrgCode;
	}

	public void setDefaultAssignOrgCode(Long defaultAssignOrgCode) {
		this.defaultAssignOrgCode = defaultAssignOrgCode;
	}

	public GrantSettingExtend getGrantSettingExtend() {
		return grantSettingExtend;
	}

	public void setGrantSettingExtend(GrantSettingExtend grantSettingExtend) {
		this.grantSettingExtend = grantSettingExtend;
	}

	public GrantNote getGrantNote() {
		return grantNote;
	}

	public void setGrantNote(GrantNote grantNote) {
		this.grantNote = grantNote;
	}

	public FundSetting getFundSetting() {
		return fundSetting;
	}

	public void setFundSetting(FundSetting fundSetting) {
		this.fundSetting = fundSetting;
	}

	public Long getJinzhanFormCode() {
		return jinzhanFormCode;
	}

	public void setJinzhanFormCode(Long jinzhanFormCode) {
		this.jinzhanFormCode = jinzhanFormCode;
	}

	public Long getZhongqiFormCode() {
		return zhongqiFormCode;
	}

	public void setZhongqiFormCode(Long zhongqiFormCode) {
		this.zhongqiFormCode = zhongqiFormCode;
	}

	public Long getZongjieFormCode() {
		return zongjieFormCode;
	}

	public void setZongjieFormCode(Long zongjieFormCode) {
		this.zongjieFormCode = zongjieFormCode;
	}

	public Long getJietiFormCode() {
		return jietiFormCode;
	}

	public void setJietiFormCode(Long jietiFormCode) {
		this.jietiFormCode = jietiFormCode;
	}

	public Long getChengguoFormCode() {
		return chengguoFormCode;
	}

	public void setChengguoFormCode(Long chengguoFormCode) {
		this.chengguoFormCode = chengguoFormCode;
	}

	public String getManagecategory() {
		return managecategory;
	}

	public void setManagecategory(String managecategory) {
		this.managecategory = managecategory;
	}

	public String getSponsor_category() {
		return sponsor_category;
	}

	public void setSponsor_category(String sponsor_category) {
		this.sponsor_category = sponsor_category;
	}

	public Long getDefaultAdminOfficeCode() {
		return defaultAdminOfficeCode;
	}

	public void setDefaultAdminOfficeCode(Long defaultAdminOfficeCode) {
		this.defaultAdminOfficeCode = defaultAdminOfficeCode;
	}

	public Date getUPDATE_DATE() {
		return UPDATE_DATE;
	}

	public void setUPDATE_DATE(Date uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}

	public Long getFundCode() {
		return fundCode;
	}

	public void setFundCode(Long fundCode) {
		this.fundCode = fundCode;
	}

	public Long getFinOrgCode() {
		return finOrgCode;
	}

	public void setFinOrgCode(Long finOrgCode) {
		this.finOrgCode = finOrgCode;
	}

	public String getFundFlag() {
		return fundFlag;
	}

	public void setFundFlag(String fundFlag) {
		this.fundFlag = fundFlag;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public Character getNeedCtr() {
		return needCtr;
	}

	public void setNeedCtr(Character needCtr) {
		this.needCtr = needCtr;
	}

	public Character getNeedComplete() {
		return needComplete;
	}

	public void setNeedComplete(Character needComplete) {
		this.needComplete = needComplete;
	}

	public Character getNeedProgress() {
		return needProgress;
	}

	public void setNeedProgress(Character needProgress) {
		this.needProgress = needProgress;
	}

	public String getSponsorTarget() {
		return sponsorTarget;
	}

	public void setSponsorTarget(String sponsorTarget) {
		this.sponsorTarget = sponsorTarget;
	}

	public String getSponsorPhase() {
		return sponsorPhase;
	}

	public void setSponsorPhase(String sponsorPhase) {
		this.sponsorPhase = sponsorPhase;
	}

	public String getSponsorArea() {
		return sponsorArea;
	}

	public void setSponsorArea(String sponsorArea) {
		this.sponsorArea = sponsorArea;
	}

	public String getSponsorSort() {
		return sponsorSort;
	}

	public void setSponsorSort(String sponsorSort) {
		this.sponsorSort = sponsorSort;
	}

	public String getSponsorDuration() {
		return sponsorDuration;
	}

	public void setSponsorDuration(String sponsorDuration) {
		this.sponsorDuration = sponsorDuration;
	}

	public String getFlagLimit() {
		return flagLimit;
	}

	public void setFlagLimit(String flagLimit) {
		this.flagLimit = flagLimit;
	}

	public Double getLimitAmt() {
		return limitAmt;
	}

	public void setLimitAmt(Double limitAmt) {
		this.limitAmt = limitAmt;
	}

	public String getAmtGrade() {
		return amtGrade;
	}

	public void setAmtGrade(String amtGrade) {
		this.amtGrade = amtGrade;
	}

	public Long getPrjOfficeCode() {
		return prjOfficeCode;
	}

	public void setPrjOfficeCode(Long prjOfficeCode) {
		this.prjOfficeCode = prjOfficeCode;
	}

	public Long getSfCode() {
		return sfCode;
	}

	public void setSfCode(Long sfCode) {
		this.sfCode = sfCode;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public Long getFinOfficeCode() {
		return finOfficeCode;
	}

	public void setFinOfficeCode(Long finOfficeCode) {
		this.finOfficeCode = finOfficeCode;
	}

	public Long getIsLdzj() {
		return isLdzj;
	}

	public void setIsLdzj(Long isLdzj) {
		this.isLdzj = isLdzj;
	}

	public Long getIsDbzj() {
		return isDbzj;
	}

	public void setIsDbzj(Long isDbzj) {
		this.isDbzj = isDbzj;
	}

	public String getNeedPrpCompare() {
		return needPrpCompare;
	}

	public void setNeedPrpCompare(String needPrpCompare) {
		this.needPrpCompare = needPrpCompare;
	}

	public Integer getSponsorDurationYear() {
		return sponsorDurationYear;
	}

	public void setSponsorDurationYear(Integer sponsorDurationYear) {
		this.sponsorDurationYear = sponsorDurationYear;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
