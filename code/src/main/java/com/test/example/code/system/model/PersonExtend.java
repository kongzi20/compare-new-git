package com.test.example.code.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

/**
 * 
 * 人员扩展表.
 */
@Entity
@Table(name = "PERSON_EXTEND")
public class PersonExtend implements Serializable {

	/**
	 * 待验证
	 */
	public static final Integer YET_INVALID = 0; // 待验证
	/**
	 * 验证为有效
	 */
	public static final Integer VALIDED = 1; // 验证为有效

	private static final long serialVersionUID = 6418815223880452058L;

	private Long psnCode;

	private String psnVersion;

	private Document extInfo;

	private Date updateDate;

	private Long updatePsnCode;

	private Person person;

	public PersonExtend() {

	}

	public PersonExtend(Long psnCode) {
		this.psnCode = psnCode;
	}

	@Id
	@Column(name = "PSN_CODE")
	@GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "person"))
	@GeneratedValue(generator = "pkGenerator")
	public Long getPsnCode() {
		return psnCode;
	}

	public void setPsnCode(Long psnCode) {
		this.psnCode = psnCode;
	}

	// the param 'optional' shows that a extra-info should depends on a
	// person-info
	// Rather a person-info is needless to associated with extra-info
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "PSN_VERSON")
	public String getPsnVersion() {
		return psnVersion;
	}

	public void setPsnVersion(String psnVersion) {
		this.psnVersion = psnVersion;
	}

	@Column(name = "PSN_XML")
	@Type(type = "com.test.example.core.dao.orm.OracleXmlType")
	public Document getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(Document extInfo) {
		this.extInfo = extInfo;
	}

	@Column(name = "UPDATE_TIME")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "UPDATE_PSN_CODE")
	public Long getUpdatePsnCode() {
		return updatePsnCode;
	}

	public void setUpdatePsnCode(Long updatePsnCode) {
		this.updatePsnCode = updatePsnCode;
	}

}