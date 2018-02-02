package com.iris.egrant.code.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员信息扩展MODEL.（此类最初用于I2E存储同步数据，也是作为person类的一个子类存储一些非重要信息）
 * 
 */
@Entity
@Table(name = "Person_sub")
public class PersonSubInfo4Sync implements Serializable {

	private static final long serialVersionUID = 5998647235202721718L;

	private Long psnCode;

	private Character gender;

	private Date birthday;

	private String ethnicity; // 民族 字段存储为常量表 ID

	private String regionCode; // 国别

	private String proftitle; // 职称

	private String position; // 职务

	private String degree;

	private String degreeYear;

	private String degreeCountry;

	private String major;

	private String telphone;

	private String fax;

	private String backupEmail;

	private String address;

	private String postCode;

	private String province;

	private String city;

	private String department;

	private Date loginTime;

	private Boolean validEmail;
	private Boolean validMobile;
	private Boolean validCard;

	public PersonSubInfo4Sync() {

	}

	public PersonSubInfo4Sync(Long psnCode) {
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

	@Column(name = "GENDER")
	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	@Column(name = "BIRTHDAY")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "ETHNICITY")
	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	@Column(name = "REGIONCODE")
	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	@Column(name = "PROFTITLE")
	public String getProftitle() {
		return proftitle;
	}

	public void setProftitle(String proftitle) {
		this.proftitle = proftitle;
	}

	@Column(name = "POSITION")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "DEGREECODE")
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	@Column(name = "DEGREEYEAR")
	public String getDegreeYear() {
		return degreeYear;
	}

	public void setDegreeYear(String degreeYear) {
		this.degreeYear = degreeYear;
	}

	@Column(name = "DEGREECOUNTRY")
	public String getDegreeCountry() {
		return degreeCountry;
	}

	public void setDegreeCountry(String degreeCountry) {
		this.degreeCountry = degreeCountry;
	}

	@Column(name = "MAJOR")
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Column(name = "TEL")
	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	@Column(name = "FAX")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "BACKUPEMAIL")
	public String getBackupEmail() {
		return backupEmail;
	}

	public void setBackupEmail(String backupEmail) {
		this.backupEmail = backupEmail;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "POSTCODE")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "PROVINCE")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "DEPARTMENT")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "valid_email")
	public Boolean getValidEmail() {
		return validEmail;
	}

	public void setValidEmail(Boolean validEmail) {
		this.validEmail = validEmail;
	}

	@Column(name = "valid_mobile")
	public Boolean getValidMobile() {
		return validMobile;
	}

	public void setValidMobile(Boolean validMobile) {
		this.validMobile = validMobile;
	}

	@Column(name = "valid_card")
	public Boolean getValidCard() {
		return validCard;
	}

	public void setValidCard(Boolean validCard) {
		this.validCard = validCard;
	}

	@Column(name = "login_time")
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

}
