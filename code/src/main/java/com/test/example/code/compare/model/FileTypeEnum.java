package com.test.example.code.compare.model;

/**
 * 文件用途分类
 * 
 * @author liulijie
 * 
 */
public enum FileTypeEnum {

	/**
	 * 申请书xml.
	 */
	prp_xml,
	/*
	 * 变更附件
	 */
	cr_attach,
	/**
	 * 申请书附件.
	 */

	prp_attach,
	/**
	 * 单位附件.
	 */
	org_attach,
	/**
	 * 邮件模板附件.
	 */
	mail_tmp_attach,
	/**
	 * 项目资助计划书附件.
	 */
	prj_plan_attach,
	/**
	 * 项目申请书附件.
	 */
	prj_proposal_attach,
	/**
	 * 项目成果报告附件.
	 */
	prj_outcome_attach,
	/**
	 * 项目进展报告附件.
	 */
	prj_progress_attach,
	/**
	 * 项目结题报告附件.
	 */
	prj_concl_rpt_attach,

	/**
	 * 申请书PDF
	 */
	prp_pdf,

	/**
	 * 合同书PDF
	 */
	ctr_pdf,
	/**
	 * 验收证书终稿
	 */
	cert_final,
	/**
	 * SQL版本文件
	 */
	sql_veriosn_attach,
	/**
	 * fckeditor编辑器上传附件
	 */
	fck_attach,
	/**
	 * 首页操作规程/申报指南
	 */
	guide,
	/**
	 * 申报书样本
	 */
	sample,
	/**
	 * 专题报告
	 */
	specialReport,

	/**
	 * 内外网文件同步 下载
	 */
	network_down,
	/**
	 * 内外网文件同步 上传
	 */
	network_upload,
	grant_template, rptp_pdf, prjcr_pdf, rptc_pdf,rptctf_pdf,
	/**
	 * 风险补偿相关文件
	 */
	loan_fund_file,
	/**
	 * 投资分红相关文件
	 */
	share_bonus_file
}
