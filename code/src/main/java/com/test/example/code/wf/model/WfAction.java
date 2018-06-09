/**
 *  Licensed to IRIS-System co.
 */
package com.test.example.code.wf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author zzhx 2012-11-2
 */
@Entity
@Table(name = "wf_action")
public class WfAction implements Serializable {

	private static final long serialVersionUID = 3764396018408977008L;

	private Long id;
	/**
	 * 触发条件id,对应wf_rule表主键
	 */
	private String conditionIds;
	/**
	 * 校验条件id,对应wf_rule表主键
	 */
	private String validateIds;
	/**
	 * 触发事件id,对应wf_rule表主键
	 */
	private String eventIds;
	/**
	 * 是否发生邮件，默认为0
	 */
	private int isSendMail;
	/**
	 * 是否发生短信，默认为0
	 */
	private int isSendSms;
	/**
	 * 邮件模版,对应mail_template表主键
	 */
	private Long mailTmpCode;
	/**
	 * 短信模版，对于sms_template表主键
	 */
	private Long smsTmpCode;

	private String conditionLogic;
	private String validateLogic;
	private String eventLogic;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_WF_ACTION", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CONDITION_IDS")
	public String getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String conditionIds) {
		this.conditionIds = conditionIds;
	}

	@Column(name = "VALIDATE_IDS")
	public String getValidateIds() {
		return validateIds;
	}

	public void setValidateIds(String validateIds) {
		this.validateIds = validateIds;
	}

	@Column(name = "EVENT_IDS")
	public String getEventIds() {
		return eventIds;
	}

	public void setEventIds(String eventIds) {
		this.eventIds = eventIds;
	}

	@Column(name = "IS_SEND_MAIL")
	public int getIsSendMail() {
		return isSendMail;
	}

	public void setIsSendMail(int isSendMail) {
		this.isSendMail = isSendMail;
	}

	@Column(name = "IS_SEND_SMS")
	public int getIsSendSms() {
		return isSendSms;
	}

	public void setIsSendSms(int isSendSms) {
		this.isSendSms = isSendSms;
	}

	@Column(name = "MAIL_TMP_CODE")
	public Long getMailTmpCode() {
		return mailTmpCode;
	}

	public void setMailTmpCode(Long mailTmpCode) {
		this.mailTmpCode = mailTmpCode;
	}

	@Column(name = "SMS_TMP_CODE")
	public Long getSmsTmpCode() {
		return smsTmpCode;
	}

	public void setSmsTmpCode(Long smsTmpCode) {
		this.smsTmpCode = smsTmpCode;
	}

	@Column(name = "CONDITION_LOGIC")
	public String getConditionLogic() {
		return conditionLogic;
	}

	public void setConditionLogic(String conditionLogic) {
		this.conditionLogic = conditionLogic;
	}

	@Column(name = "VALIDATE_LOGIC")
	public String getValidateLogic() {
		return validateLogic;
	}

	public void setValidateLogic(String validateLogic) {
		this.validateLogic = validateLogic;
	}

	@Column(name = "EVENT_LOGIC")
	public String getEventLogic() {
		return eventLogic;
	}

	public void setEventLogic(String eventLogic) {
		this.eventLogic = eventLogic;
	}

}
