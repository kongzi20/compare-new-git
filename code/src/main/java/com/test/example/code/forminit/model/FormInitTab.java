package com.test.example.code.forminit.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the FORM_INIT_TAB database table.
 * 
 */
@Entity
@Table(name = "FORM_INIT_TAB")
public class FormInitTab implements Serializable {

	private static final long serialVersionUID = 9058337119039012905L;

	@Id
	@Column(name = "FORM_TAB_CODE", precision = 4)
	private Long formTabCode;

	@Column(name = "SUBMIT_URL", length = 200)
	private String submitUrl;

	@Column(name = "TAB_NAME")
	private String tabName;

	@Column(name = "VIEW_URL", length = 200)
	private String viewUrl;

	public FormInitTab() {
	}

	public String getSubmitUrl() {
		return this.submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public String getTabName() {
		return this.tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public Long getFormTabCode() {
		return formTabCode;
	}

	public void setFormTabCode(Long formTabCode) {
		this.formTabCode = formTabCode;
	}

}