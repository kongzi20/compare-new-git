/**
 *  Licensed to IRIS-System co.
 */
package com.test.example.code.project.model;

import java.io.Serializable;

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

/**
 * 项目扩展表
 * 
 * @author Liulijie
 */
@Entity
@Table(name = "PROJECT_EXTEND")
public class ProjectExtend implements Serializable {

	private static final long serialVersionUID = 3290589607782178862L;

	private Long prjCode;
	
	private Project project;
	
	private Long mailCode; // 通知填报合同的邮件编码
	
	private Long smsCode; // 通知填报合同的短信编码
	
	private Long hisCheckTarget; // （历史项目）考核目标

	@Id
	@Column(name = "PRJ_CODE")
	@GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "project"))
	@GeneratedValue(generator = "pkGenerator")
	public Long getPrjCode() {
		return prjCode;
	}

	public void setPrjCode(Long prjCode) {
		this.prjCode = prjCode;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(name = "MAIL_CODE")
	public Long getMailCode() {
		return mailCode;
	}

	public void setMailCode(Long mailCode) {
		this.mailCode = mailCode;
	}

	@Column(name = "SMS_CODE")
	public Long getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(Long smsCode) {
		this.smsCode = smsCode;
	}
    
	@Column(name = "HIS_CHECK_TARGET")
	public Long getHisCheckTarget() {
		return hisCheckTarget;
	}

	public void setHisCheckTarget(Long hisCheckTarget) {
		this.hisCheckTarget = hisCheckTarget;
	}
	
}
