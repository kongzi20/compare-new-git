package com.iris.egrant.code.system.model;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.iris.egrant.core.utils.XMLHelper;

/**
 * 人员model.
 * 
 * 
 * 
 */
@Entity
@Table(name = "Person")
public class Person implements Serializable {

	public static final Integer CARD_TYPE_INDENTITY = 1; // 身份证
	public static final Integer CARD_TYPE_MILITARY = 2; // 军人证
	public static final Integer CARD_TYPE_PUP = 3; // 学生证

	private static final long serialVersionUID = -1912858535247413876L;

	private Long psnCode;

	private PersonExtend psnExt;

	private String zhName;

	private String firstName;

	private String lastName;

	private String englishName;

	private Long orgCode;

	private String orgName;

	private Long deptCode;

	private String deptName;

	private Integer cardType;

	private String cardCode;

	private String mobile;

	private String email;

	private Date createDate;

	private Date updateTime;

	private Character status;

	private String title;

	private String profTitle;

	private String profTitleId;

	private Boolean enable; // 该人员信息是否有效

	private Boolean edited; // 该人员信息是否被补充完整

	private String tel;

	private Integer importPsn; // 只能取值0或1.为1时是导入的记录，为空或者为0是非导入记录

	private Set<PsnOrgState> poRegister = new HashSet<PsnOrgState>(); // 人员单位审核关系

	private Set<Organization> chargOrgs = new HashSet<Organization>();

	private Set<Organization> orgs = new HashSet<Organization>();

	private Set<Department> depts = new HashSet<Department>();

	private Set<Department> chargDepts = new HashSet<Department>();

	private Set<SimpleRole> roles = new HashSet<SimpleRole>();

	public Person() {
	}

	public Person(Long psnCode) {
		this.psnCode = psnCode;
	}

/*
 * 	@Id
	@Column(name = "PSN_CODE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PERSON", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
 */
	
 	@Id
	@Column(name = "PSN_CODE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PERSON", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	public Long getPsnCode() {
		return psnCode;
	}

	public void setPsnCode(Long psnCode) {
		this.psnCode = psnCode;
	}

	@ManyToMany(mappedBy = "psns", fetch = FetchType.LAZY)
	public Set<Department> getDepts() {
		return depts;
	}

	public void setDepts(Set<Department> depts) {
		this.depts = depts;
	}

	/**
	 * 解除该人员与之前各部门的从属关系.
	 */
	public void removeDepts() {
		for (Department dept : this.depts) {
			dept.removePsn(this);
		}
	}

	@ManyToMany(mappedBy = "psns", cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	public Set<Organization> getOrgs() {
		return orgs;
	}

	public void setOrgs(Set<Organization> orgs) {
		this.orgs = orgs;
	}

	/**
	 * 解除该人员和之前各单位的从属关系.
	 */
	public void removeOrgs() {
		setOrgCode(null);
		setOrgName(null);
		String xmlData = XMLHelper.refreshNodeValue(XMLHelper.parseDoc(getPsnExt().getExtInfo()).asXML(),
				"data/basic_info/org_name", "");
		getPsnExt().setExtInfo(XMLHelper.parseW3cDoc(xmlData));
		for (Organization org : this.orgs) {
			org.getPsns().remove(this);
		}
	}

	@Column(name = "ZH_NAME")
	public String getZhName() {
		return zhName;
	}

	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	@Column(name = "ORG_NAME")
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LAST_NAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Transient
	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName() {
		englishName = this.firstName + " " + this.lastName;
	}

	@Column(name = "CARD_TYPE")
	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	@Column(name = "CARD_CODE")
	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	@Column(name = "MOBILE")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "STATUS")
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@PrimaryKeyJoinColumn
	public PersonExtend getPsnExt() {
		return psnExt;
	}

	public void setPsnExt(PersonExtend psnExt) {
		this.psnExt = psnExt;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "PROF_TITLE")
	public String getProfTitle() {
		return profTitle;
	}

	public void setProfTitle(String profTitle) {
		this.profTitle = profTitle;
	}

	@ManyToMany(mappedBy = "adminers", fetch = FetchType.LAZY)
	public Set<Organization> getChargOrgs() {
		return chargOrgs;
	}

	public void setChargOrgs(Set<Organization> chargOrgs) {
		this.chargOrgs = chargOrgs;
	}

	@ManyToMany(mappedBy = "adminers", fetch = FetchType.LAZY)
	public Set<Department> getChargDepts() {
		return chargDepts;
	}

	public void setChargDepts(Set<Department> chargDepts) {
		this.chargDepts = chargDepts;
	}

	/**
	 * 移除人员所在所有部门
	 * 
	 * @author lineshow created on 2011-12-1
	 */
	public void removeChargDepts() {
		for (Department dept : this.chargDepts) {
			dept.getAdminers().remove(this);
		}
	}

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "SYS_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "PSN_CODE") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	@OrderBy("id")
	public Set<SimpleRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<SimpleRole> roles) {
		this.roles = roles;
	}

	@Column(name = "ORG_CODE")
	public Long getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name = "DEPT_CODE")
	public Long getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(Long deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "DEPT_NAME")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * 
	 * @author lineshow created on 2011-10-25
	 * @return
	 */
	@Column(name = "PENABLE")
	public Boolean getEnable() {
		return enable;
	}

	/**
	 * person be actived
	 * 
	 * @author lineshow created on 2011-10-25
	 * @param enable
	 */
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	/**
	 * it will be completed, once the personal info edited via function editing personalinfo.<br/>
	 * the field 'edited' indicates completed or not, default value is flase[0]
	 * 
	 * @author lineshow created on 2011-11-4
	 * @return
	 */
	@Column(name = "EDITED")
	public Boolean getEdited() {
		return edited;
	}

	public void setEdited(Boolean edited) {
		this.edited = edited;
	}

	@OneToMany(mappedBy = "person", cascade = { CascadeType.REFRESH })
	public Set<PsnOrgState> getPoRegister() {
		return poRegister;
	}

	public void setPoRegister(Set<PsnOrgState> poRegister) {
		this.poRegister = poRegister;
	}

	@Column(name = "PROF_TITLE_ID")
	public String getProfTitleId() {
		return profTitleId;
	}

	public void setProfTitleId(String profTitleId) {
		this.profTitleId = profTitleId;
	}

	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "TEL")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "IMPORT")
	public Integer getImportPsn() {
		return importPsn;
	}

	public void setImportPsn(Integer importPsn) {
		this.importPsn = importPsn;
	}

}