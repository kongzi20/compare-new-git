package com.iris.egrant.code.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员同步MODEL.
 * 
 */
@Entity
@Table(name = "Person")
public class Person4Sync implements Serializable {

	public static final Integer CARD_TYPE_INDENTITY = 1; // 身份证
	public static final Integer CARD_TYPE_MILITARY = 2; // 军人证
	public static final Integer CARD_TYPE_PUP = 3; // 学生证

	private static final long serialVersionUID = 4906324348453695193L;

	private Long psnCode;

	private String zhName;

	private String firstName;

	private String lastName;

	private String englishName;

	private String orgName;

	private Long orgCode;

	private Integer cardType;

	private String cardCode;

	private String mobile;

	private String email;

	private Date createDate;

	private Character status;

	private String title;

	private String profTitle;

	private String profTitleId;

	private Boolean enable;

	private Boolean edited;

	@Column(name = "PROF_TITLE")
	public String getProfTitle() {
		return profTitle;
	}

	public void setProfTitle(String profTitle) {
		this.profTitle = profTitle;
	}

	public Person4Sync() {
	}

	public Person4Sync(Long psnCode) {
		this.psnCode = psnCode;
	}

	@Id
	@Column(name = "PSN_CODE")
	public Long getPsnCode() {
		return psnCode;
	}

	public void setPsnCode(Long psnCode) {
		this.psnCode = psnCode;
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

	@Column(name = "ORG_CODE")
	public Long getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
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

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * person be actived
	 * 
	 * @author lineshow created on 2011-10-25
	 * @param enable
	 */
	@Column(name = "PENABLE")
	public Boolean getEnable() {
		return enable;
	}

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

	@Column(name = "PROF_TITLE_ID")
	public String getProfTitleId() {
		return profTitleId;
	}

	public void setProfTitleId(String profTitleId) {
		this.profTitleId = profTitleId;
	}
}