package com.iris.egrant.code.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

/**
 * 人员扩展信息同步使用MODEL.
 * 
 */
@Entity
@Table(name = "PERSON_EXTEND")
public class PersonExtend4Sync implements Serializable {

	private static final long serialVersionUID = 1400373980033754257L;

	private Long psnCode;

	private String psnVersion;

	private Document extInfo;

	public PersonExtend4Sync() {

	}

	public PersonExtend4Sync(Long psnCode) {
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

	@Column(name = "PSN_VERSON")
	public String getPsnVersion() {
		return psnVersion;
	}

	public void setPsnVersion(String psnVersion) {
		this.psnVersion = psnVersion;
	}

	@Column(name = "PSN_XML")
	@Type(type = "com.iris.egrant.core.dao.orm.OracleXmlType")
	public Document getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(Document extInfo) {
		this.extInfo = extInfo;
	}
}