package com.test.example.code.system.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the ORGANIZATION database table.
 * 
 */
@Entity
@Table(name = "ORGANIZATION")
public class Organization implements Serializable {

	private static final long serialVersionUID = 2910158675651854461L;

	private Long orgCode;

	private String orgNo;

	private String name;

	private String enName;

	private String areaNo;

	private String areaName;

	private Character status;

	private Character oldStatus;
	
	//统一社会信用代码
	private String orgSHXYNo;
	//单位机构代码类型，0表示组织机构代码，1表示统一社会信用代码
	private String orgNoType;

	private Date createDate;

	private String nutureType;

	private Long contactPsnCode;

	private String tel;
	private String orgScale;

	private String email;
	private String ifZancun;

	private Long orgVerson;
	
	// 单位银行账户
	private String bankOrgName;
	// 注册银行
	private String bankName;
	@Column(name = "ORG_SCALE")
	public String getOrgScale() {
		return orgScale;
	}

	public void setOrgScale(String orgScale) {
		this.orgScale = orgScale;
	}
	private BigDecimal nowNevenues;
	@Column(name = "NOW_REVENUES")
	public BigDecimal getNowNevenues() {
		return nowNevenues;
	}
	public void setNowNevenues(BigDecimal nowNevenues) {
		this.nowNevenues = nowNevenues;
	}
	// 银行帐号
	private String bankAccount;
	// 银行行号
	private String bankIdNo;

	// 银行信用等级
	private String bankCreditLevel;
	// 隶属关系
	private Long belongToCode;
	// 机构类型
	private String nature;
	// 单位类型
	private Long orgType;
	// 联络网
	private Long belongnet;
	// 单位批准号
	private String pno;
	// 邮编
	private String zipCode;
	// 注册年份
	private String regYear;
	// A/B类
	private String regType;
	private String regType2;
	// 主管部门(关联organization表)
	private Long parent_code;
	// 注册资金
	private BigDecimal registe_fund;
	// 注册资金类型
	private String registe_fund_type;
	// 注册日期
	private Date registe_date;
	// 所属国民经济行业的name

	private String industryName;
	// 所属国民经济行业的value
	private String industryValue;

	// 单位地址
	private String address;
	// 单位传真
	private String fax;
	// 单位网址
	private String http;
	// 是否推荐单位
	private Character flagRecommend;
	// 所属区
	private String belongArea;
	// 是否市直单位
	private Character flagShizhi;
	@Column(name = "TECHAREA_VALUE")
	private String techareaValue;

	public String getTechareaValue() {
		return techareaValue;
	}

	public void setTechareaValue(String techareaValue) {
		this.techareaValue = techareaValue;
	}
	private Set<PsnOrgState> poRegister = new HashSet<PsnOrgState>(); // 人员单位审核关系

	private Set<Person> adminers = new HashSet<Person>();

	private Set<Person> psns = new HashSet<Person>();

	private Set<Department> depts = new HashSet<Department>();

	public Organization() {
	}

	public Organization(Long orgCode) {
		this.orgCode = orgCode;
	}

	@Id
	@Column(name = "ORG_CODE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ORGANIZATION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	public Long getOrgCode() {
		return orgCode;
	}

	@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Department> getDepts() {
		return depts;
	}

	public void setDepts(Set<Department> depts) {
		this.depts = depts;
	}

	public void addDept(Department dept) {
		dept.setOrganization(this);
		this.depts.add(dept);
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH }, fetch = FetchType.LAZY)
	@JoinTable(name = "Person_Org", joinColumns = { @JoinColumn(name = "ORG_CODE") }, inverseJoinColumns = { @JoinColumn(name = "PSN_CODE") })
	// @OrderBy("psnCode")
	public Set<Person> getPsns() {
		return psns;
	}

	public void setPsns(Set<Person> psns) {
		this.psns = psns;
	}

	public void removePsn(Person psn) {
		this.psns.remove(psn);
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name = "ORG_NO")
	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AREA_NO")
	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	@Column(name = "STATUS")
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	@Column(name = "OLD_STATUS")
	public Character getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Character oldStatus) {
		this.oldStatus = oldStatus;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "NATURE_TYPE")
	public String getNutureType() {
		return nutureType;
	}

	public void setNutureType(String nutureType) {
		this.nutureType = nutureType;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "Person_Org_Admin", joinColumns = { @JoinColumn(name = "ORG_CODE") }, inverseJoinColumns = { @JoinColumn(name = "PSN_CODE") })
	@OrderBy("psnCode")
	public Set<Person> getAdminers() {
		return adminers;
	}

	@Column(name = "CONTACT_PSN_CODE")
	public Long getContactPsnCode() {
		return contactPsnCode;
	}

	@Column(name = "BANKORGNAME")
	public String getBankOrgName() {
		return bankOrgName;
	}

	@Column(name = "BANKNAME")
	public String getBankName() {
		return bankName;
	}

	@Column(name = "BELONGTOCODE")
	public Long getBelongToCode() {
		return belongToCode;
	}

	@Column(name = "NATURE")
	public String getNature() {
		return nature;
	}

	@Column(name = "ORGTYPE")
	public Long getOrgType() {
		return orgType;
	}

	@Column(name = "BELONGNET")
	public Long getBelongnet() {
		return belongnet;
	}

	@Column(name = "PNO")
	public String getPno() {
		return pno;
	}

	@Column(name = "ZIPCODE")
	public String getZipCode() {
		return zipCode;
	}

	@Column(name = "REGYEAR")
	public String getRegYear() {
		return regYear;
	}

	@Column(name = "REGTYPE")
	public String getRegType() {
		return regType;
	}

	@Column(name = "REGTYPE2")
	public String getRegType2() {
		return regType2;
	}

	@Column(name = "IF_ZANCUN")
	public String getIfZancun() {
		return ifZancun;
	}

	public void setIfZancun(String ifZancun) {
		this.ifZancun = ifZancun;
	}

	public void setBankOrgName(String bankOrgName) {
		this.bankOrgName = bankOrgName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setBelongToCode(Long belongToCode) {
		this.belongToCode = belongToCode;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public void setOrgType(Long orgType) {
		this.orgType = orgType;
	}

	public void setBelongnet(Long belongnet) {
		this.belongnet = belongnet;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setRegYear(String regYear) {
		this.regYear = regYear;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public void setRegType2(String regType2) {
		this.regType2 = regType2;
	}

	public void setContactPsnCode(Long contactPsnCode) {
		this.contactPsnCode = contactPsnCode;
	}

	public void setAdminers(Set<Person> adminers) {
		this.adminers = adminers;
	}

	public void addAdminer(Person adminer) {
		adminer.getChargOrgs().add(this);
		this.adminers.add(adminer);
	}

	public void removeAdminer(Person adminer) {
		adminer.getChargOrgs().remove(this);
		this.adminers.remove(adminer);
	}

	@OneToMany(mappedBy = "organization", cascade = { CascadeType.REFRESH })
	public Set<PsnOrgState> getPoRegister() {
		return poRegister;
	}

	public void setPoRegister(Set<PsnOrgState> poRegister) {
		this.poRegister = poRegister;
	}

	@Column(name = "EN_NAME")
	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	@Column(name = "PARENT_ORG_CODE")
	public Long getParent_code() {
		return parent_code;
	}

	public void setParent_code(Long parent_code) {
		this.parent_code = parent_code;
	}

	@Column(name = "REGISTE_FUND")
	public BigDecimal getRegiste_fund() {
		return registe_fund;
	}

	public void setRegiste_fund(BigDecimal registe_fund) {
		this.registe_fund = registe_fund;
	}

	@Column(name = "REGISTE_FUND_TYPE")
	public String getRegiste_fund_type() {
		return registe_fund_type;
	}

	public void setRegiste_fund_type(String registe_fund_type) {
		this.registe_fund_type = registe_fund_type;
	}

	@Column(name = "REGISTE_DATE")
	public Date getRegiste_date() {
		return registe_date;
	}

	public void setRegiste_date(Date registe_date) {
		this.registe_date = registe_date;
	}

	@Column(name = "TEL")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "FAX")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "HTTP")
	public String getHttp() {
		return http;
	}

	public void setHttp(String http) {
		this.http = http;
	}

	@Column(name = "AREA_NAME")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "FLAG_RECOMMEND")
	public Character getFlagRecommend() {
		return flagRecommend;
	}

	public void setFlagRecommend(Character flagRecommend) {
		this.flagRecommend = flagRecommend;
	}

	@Column(name = "INDUSTRY_NAME")
	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	@Column(name = "INDUSTRY_VALUE")
	public String getIndustryValue() {
		return industryValue;
	}

	public void setIndustryValue(String industryValue) {
		this.industryValue = industryValue;
	}

	@Column(name = "BANK_ACCOUNT")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Column(name = "BANK_ID_NO")
	public String getBankIdNo() {
		return bankIdNo;
	}

	public void setBankIdNo(String bankIdNo) {
		this.bankIdNo = bankIdNo;
	}

	@Column(name = "BANK_CREDIT_LEVEL")
	public String getBankCreditLevel() {
		return bankCreditLevel;
	}

	public void setBankCreditLevel(String bankCreditLevel) {
		this.bankCreditLevel = bankCreditLevel;
	}
	
	@Column(name = "org_shxy_no")
	public String getOrgSHXYNo() {
		return orgSHXYNo;
	}

	public void setOrgSHXYNo(String orgSHXYNo) {
		this.orgSHXYNo = orgSHXYNo;
	}

	@Column(name = "org_no_type")
	public String getOrgNoType() {
		return orgNoType;
	}

	public void setOrgNoType(String orgNoType) {
		this.orgNoType = orgNoType;
	}

	@Transient
	public String getBelongArea() {
		return belongArea;
	}

	public void setBelongArea(String belongArea) {
		this.belongArea = belongArea;
	}

	@Transient
	public Character getFlagShizhi() {
		return flagShizhi;
	}

	public void setFlagShizhi(Character flagShizhi) {
		this.flagShizhi = flagShizhi;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Organization) {
			return ((Organization) obj).getOrgCode().longValue() == this.getOrgCode().longValue();
		}
		return false;
	}

	public Long getOrgVerson() {
		return orgVerson;
	}

	public void setOrgVerson(Long orgVerson) {
		this.orgVerson = orgVerson;
	}

}