package com.test.example.code.system.model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 部门model.
 * 
 * 
 * 
 */
@Entity
@Table(name = "org_department")
public class Department implements Serializable {

	private static final long serialVersionUID = 2910158675651854461L;

	private Long deptCode;// 部门Code

	private String deptName;// 部门名称

	private String enDeptName; // 部门英文名称

	private String abbrName; // 部门名称缩写

	private Organization organization;

	private Set<Person> adminers = new HashSet<Person>();

	private Set<Person> psns = new HashSet<Person>();

	public Department() {

	}

	public Department(Long deptCode) {
		this.deptCode = deptCode;
	}

	@Id
	@Column(name = "DEPT_CODE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	@GenericGenerator(name = "SEQ_STORE", strategy = "com.test.example.core.dao.hibernate.AssignedSequenceGenerator", parameters = { @Parameter(name = "sequence", value = "SEQ_ORG_DEPARTMENT") })
	public Long getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(Long deptCode) {
		this.deptCode = deptCode;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "ORG_CODE")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "person_dept", joinColumns = { @JoinColumn(name = "DEPT_CODE") }, inverseJoinColumns = { @JoinColumn(name = "PSN_CODE") })
	@OrderBy("psnCode")
	public Set<Person> getPsns() {
		return psns;
	}

	public void setPsns(Set<Person> psns) {
		this.psns = psns;
	}

	/**
	 * add specified person
	 * 
	 * @author lineshow created on 2011-11-16
	 * @param psn
	 */
	public void addPsn(Person psn) {
		this.psns.add(psn);
	}

	/**
	 * add person in bulk
	 * 
	 * @author lineshow created on 2011-11-16
	 * @param psn
	 */
	public void addPsns(Collection<Person> psn) {
		this.psns.addAll(psn);
	}

	/**
	 * remove specified person
	 * 
	 * @author lineshow created on 2011-11-16
	 * @param psn
	 */
	public void removePsn(Person psn) {
		this.psns.remove(psn);
	}

	/**
	 * remove all person
	 * 
	 * @author lineshow created on 2011-11-16
	 */
	public void removePsns() {
		this.psns.clear();
	}

	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "Person_Dept_Admin", joinColumns = { @JoinColumn(name = "DEPT_CODE") }, inverseJoinColumns = { @JoinColumn(name = "PSN_CODE") })
	@OrderBy("psnCode")
	public Set<Person> getAdminers() {
		return adminers;
	}

	public void setAdminers(Set<Person> adminers) {
		this.adminers = adminers;
	}

	public void addAdminer(Person adminer) {
		this.adminers.add(adminer);
	}

	@Column(name = "dept_name")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "en_dept_name")
	public String getEnDeptName() {
		return enDeptName;
	}

	public void setEnDeptName(String enDeptName) {
		this.enDeptName = enDeptName;
	}

	@Column(name = "abbr_name")
	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

}