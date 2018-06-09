package com.test.example.code.forminit.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 通用初始化配置表.
 * 
 */
@Entity
@Table(name = "FORM_BASE_LIBRARY")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class FormBaseLibrary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FORM_BASE_LIBRARY_FORMCODE_GENERATOR", sequenceName = "SEQ_PERSON")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORM_BASE_LIBRARY_FORMCODE_GENERATOR")
	@Column(name = "FORM_CODE", unique = true, nullable = false, precision = 4)
	private Long formCode;

	// 一个申请类别的一个模板类型的编号，模板类型如poposal,contract,acceptance,project,report
	@Column(name = "FORM_ID")
	private Long formId;

	/**
	 * '0'表示历史form,'1'表示当前正在使用的form，一个form_id对应的一些列，只允许有一个form为1.
	 */
	@Column(nullable = false, length = 1)
	private String status;

	/**
	 * 模板类型,关联到const_dictionary.category='form_type',如poposal,contract,acceptance,project,report.
	 */
	@Column(name = "FORM_TYPE", nullable = false, length = 20)
	private String formType;

	/**
	 * 编辑的主页面.
	 */
	@Column(name = "EDIT_INDEX_URL", length = 200)
	private String editIndexUrl;

	/**
	 * 查看主页面.
	 */
	@Column(name = "VIEW_INDEX_URL", length = 200)
	private String viewIndexUrl;

	/**
	 * 概况主页面.
	 */
	@Column(name = "SUMMARY_URL", length = 200)
	private String summaryUrl;
	/**
	 * 概况主页面.
	 */
	@Column(name = "PRP_RULE_URL", length = 200)
	private String prpRuleURL;

	public String getPrpRuleURL() {
		return prpRuleURL;
	}

	public void setPrpRuleURL(String prpRuleURL) {
		this.prpRuleURL = prpRuleURL;
	}

	/**
	 * 评审查看主页面.
	 */
	@Column(name = "REVIEW_URL", length = 200)
	private String reviewUrl;

	/**
	 * 填写样例url.
	 */
	@Column(name = "TEMPLATE_URL", length = 200)
	private String templateUrl;

	/**
	 * 可行性报告模板.
	 */
	@Column(name = "FEASIBLE_TEMPLATE")
	private String feasibleTemplate;

	/**
	 * 离线word模板.
	 */
	@Column(name = "WORD_TEMPLATE_NAME")
	private String wordTemplateName;

	/**
	 * 离线程序包.
	 */
	@Column(name = "ZIP_TEMPLATE_NAME")
	private String zipTemplateName;

	/**
	 * 生成pdf的word模板.
	 */
	@Column(name = "PDF_TEMPLATE")
	private String pdfTemplate;

	/**
	 * 生成pdf的xsl模板.
	 */
	@Column(name = "PDF_XSL")
	private String pdfXsl;

	/**
	 * 是否支持离线填写，0-只支持离线填写，1-只支持在线填写，2-支持在线离线填写.
	 */
	@Column(name = "ISONLINE", length = 1)
	private String isonline;

	@Column(name = "COMPARE_INIT_ITEMS", length = 50)
	private String compareInitItems;

	@Column(name = "INIT_TABS", length = 50)
	private String initTabs;

	@Transient
	private List<FormInitTab> formInitTabs;

	@Transient
	private String xmlData;
	// pdf查看页面
	@Column(name = "PDF_VIEW_URL")
	private String pdfViewUrl;

	// PDF模版类型
	@Column(name = "PDF_CODE")
	private String pdfCode;
	
	@Column(name = "INIT_ITEMS", length = 50)
	private String initItems;

	public String getPdfViewUrl() {
		return pdfViewUrl;
	}

	public void setPdfViewUrl(String pdfViewUrl) {
		this.pdfViewUrl = pdfViewUrl;
	}

	public Long getFormCode() {
		return formCode;
	}

	public void setFormCode(Long formCode) {
		this.formCode = formCode;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getEditIndexUrl() {
		return editIndexUrl;
	}

	public void setEditIndexUrl(String editIndexUrl) {
		this.editIndexUrl = editIndexUrl;
	}

	public String getViewIndexUrl() {
		return viewIndexUrl;
	}

	public void setViewIndexUrl(String viewIndexUrl) {
		this.viewIndexUrl = viewIndexUrl;
	}

	public String getSummaryUrl() {
		return summaryUrl;
	}

	public void setSummaryUrl(String summaryUrl) {
		this.summaryUrl = summaryUrl;
	}

	public String getReviewUrl() {
		return reviewUrl;
	}

	public void setReviewUrl(String reviewUrl) {
		this.reviewUrl = reviewUrl;
	}

	public String getTemplateUrl() {
		return templateUrl;
	}

	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	public String getFeasibleTemplate() {
		return feasibleTemplate;
	}

	public void setFeasibleTemplate(String feasibleTemplate) {
		this.feasibleTemplate = feasibleTemplate;
	}

	public String getWordTemplateName() {
		return wordTemplateName;
	}

	public void setWordTemplateName(String wordTemplateName) {
		this.wordTemplateName = wordTemplateName;
	}

	public String getZipTemplateName() {
		return zipTemplateName;
	}

	public void setZipTemplateName(String zipTemplateName) {
		this.zipTemplateName = zipTemplateName;
	}

	public String getPdfTemplate() {
		return pdfTemplate;
	}

	public void setPdfTemplate(String pdfTemplate) {
		this.pdfTemplate = pdfTemplate;
	}

	public String getPdfXsl() {
		return pdfXsl;
	}

	public void setPdfXsl(String pdfXsl) {
		this.pdfXsl = pdfXsl;
	}

	public String getIsonline() {
		return isonline;
	}

	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}

	public String getInitItems() {
		return initItems;
	}

	public void setInitItems(String initItems) {
		this.initItems = initItems;
	}

	public String getInitTabs() {
		return initTabs;
	}

	public void setInitTabs(String initTabs) {
		this.initTabs = initTabs;
	}

	public List<FormInitTab> getFormInitTabs() {
		return formInitTabs;
	}

	public void setFormInitTabs(List<FormInitTab> formInitTabs) {
		this.formInitTabs = formInitTabs;
	}

	public String getXmlData() {
		return xmlData;
	}

	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}

	public String getPdfCode() {
		return pdfCode;
	}

	public void setPdfCode(String pdfCode) {
		this.pdfCode = pdfCode;
	}
	
	public String getCompareInitItems() {
		return compareInitItems;
	}

	public void setCompareInitItems(String compareInitItems) {
		this.compareInitItems = compareInitItems;
	}

}