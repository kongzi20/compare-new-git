package com.test.example.code.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author lineshow email:lineshow7@gmail.com
 * @version 1.0
 * 
 *          PsnOrgState.java created on 2011-10-25 | modified on 2011-10-25
 * 
 *          description: maintain relationship of person and organization
 */

@Entity
@Table(name = "PERSON_ORG_REGISTER")
public class PsnOrgState implements Serializable {

	private static final long serialVersionUID = 5568750332158751299L;

	public static Character RELATION_ABSTRACT = 'A'; // 虚拟的（不确定的）单位人员关系
	public static Character RELATION_UNSURE = 'U'; // 人员申请变更单位暂存记录关系

	private Long porCode;
	private Person person;
	private Organization organization;
	private Character signSpot; // 表示 psn-org关系的某种特征
	private String remark; //审核备注
	private Date verifyDate; //审核时间
	private String oldOrgName; //原单位
	private Integer status;

	public PsnOrgState() {
	}

	public PsnOrgState(Long porCode) {
		this.porCode = porCode;
	}

	@Id
	@Column(name = "POR_CODE")
	@SequenceGenerator(name = "SEQ_POR_STORE", sequenceName = "SEQ_PERSON_ORG_REGISTER", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POR_STORE")
	public Long getPorCode() {
		return porCode;
	}

	public void setPorCode(Long porCode) {
		this.porCode = porCode;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "PSN_CODE", unique = true)
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "ORG_CODE", unique = true)
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Column(name = "SIGN_SPOT")
	public Character getSignSpot() {
		return signSpot;
	}

	public void setSignSpot(Character signSpot) {
		this.signSpot = signSpot;
	}
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "VERIFYDATE")
	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	
	
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "OLDORGNAME")
	public String getOldOrgName() {
		return oldOrgName;
	}

	public void setOldOrgName(String oldOrgName) {
		this.oldOrgName = oldOrgName;
	}

	

}