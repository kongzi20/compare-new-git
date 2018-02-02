package com.iris.egrant.code.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

/**
 * The persistent class for the ORGANIZATION_EXTEND database table.
 * 
 */
@Entity
@Table(name = "ORGANIZATION_EXTEND")
public class OrganizationExtend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6046908879740251162L;

	@Id
	@Column(name = "ID", nullable = false, precision = 18)
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ORGANIZATION_EXTEND", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	private Long id;

	@Column(name = "ORG_CODE")
	private Long orgCode;

	@Column(name = "ORG_XML")
	@Type(type = "com.iris.egrant.core.dao.orm.OracleXmlType")
	private Document extInfo;

	@Column(name = "ORG_VERSON")
	private Long orgVerson;

	@Column(name = "UPDATE_TIME")
	private Date update_date;

	@Column(name = "UPDATE_PSN_CODE")
	private Long update_psn_code;

	public OrganizationExtend() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(Long orgCode) {
		this.orgCode = orgCode;
	}

	public Long getOrgVerson() {
		return orgVerson;
	}

	public void setOrgVerson(Long orgVerson) {
		this.orgVerson = orgVerson;
	}

	public Document getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(Document extInfo) {
		this.extInfo = extInfo;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Long getUpdate_psn_code() {
		return update_psn_code;
	}

	public void setUpdate_psn_code(Long update_psn_code) {
		this.update_psn_code = update_psn_code;
	}

}