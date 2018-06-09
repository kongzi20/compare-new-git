package com.test.example.code.compare.constant;

/**
 * 申报功能使用常量 add by zxg.
 * 
 * @version $Rev$ $Date$
 */
public class ProposalConstants {
	// 申报书状态.
	/**
	 * 申报书填写中.
	 */
	public static final String PRP_EDIT = "00";
	/**
	 * 待申报单位审核.
	 */
	public static final String PRP_BE_PSN_SUBMIT = "01";
	/**
	 * 待推荐单位审核.
	 */
	public static final String PRP_BE_DEPT_SUBMIT = "02";
	/**
	 * 待管理部门审核.
	 */
	public static final String PRP_BE_ORG_SUBMIT = "03";
	/**
	 * 待接收纸质材料.
	 */
	public static final String PRP_BE_ACCEPTED = "04";
	/**
	 * 已受理.
	 */
	public static final String PRP_HAS_ACCEPTED = "05";
	/**
	 * 通过审核.
	 */
	public static final String PRP_PASS = "06";
	/**
	 * 通过立项.
	 */
	public static final String PRJ_PASS = "08";
	/**
	 * 不立项.
	 */
	public static final String PRJ_REJECT = "09";
	/**
	 * /** 退回修改项目.
	 */
	public static final String PRP_HAS_BACK = "10";

	/**
	 * 项目负责人退回 单位退回
	 */
	public static final String PRP_RETURN_BY_PSN = "1001";
	/**
	 * 部门退回 县区退回
	 */
	public static final String PRP_RETURN_BY_DEPT = "1002";
	/**
	 * 单位退回 处室退回
	 */
	public static final String PRP_RETURN_BY_ORG = "1003";
	/**
	 * 基金委退回 窗口退回
	 */
	public static final String PRP_RETURN_BY_JJW = "1004";

	/****
	 * @author Administrator 处室退回
	 */
	public static final String PRP_RETURN_BY_OFFICE = "1005";

	/**
	 * 立项否决.
	 */
	public static final String PRP_HAS_REFUSE = "98";
	/**
	 * 申报书已删除.
	 */
	public static final String PRP_HAS_DELETE = "99";

	/**
	 * 插入记录成功.
	 */
	public static final String INSERT_SUCCESS = "success";
	/**
	 * 存在相同的记录，无法正确插入.
	 */
	public static final String INSERT_SAME_RECORD = "same";

	/**
	 * 申报书已经提交过.
	 */
	public static final int PRP_IS_SUBMIT = 1;
	/**
	 * 申报书还未提交过.
	 */
	public static final int PRP_NOT_SUBMIT = 0;

	// 申报书审核动作.

	/**
	 * 审核通过.
	 */
	public static final String PRP_DO_SUBMIT = "approve";
	/**
	 * 拒绝.
	 */
	public static final String PRP_DO_REFUSE = "refuse";
	/**
	 * 退回修改.
	 */
	public static final String PRP_DO_RETURN = "return";

	public static final String GRANT_GET_ALL = "all";// 获取所有资助类别
	public static final String GRANT_GET_ENABLED = "enabled";// 获取可以填写的资助类别

	public static final String CAN_SELECT_PRJ = "1";// 允许选择项目

	public static final String CAN_NOT_SELECT_PRJ = "0";// 不可选择项目

	/**
	 * 高级职称条件
	 */
	public static final int PROF_TITLE_SENIOR = 3;

	/** 申请书状态名 **/

	public static final String APPROVE_TITLE_01 = "待申报单位审核";
	public static final String APPROVE_TITLE_02 = "待推荐单位审核";
	public static final String APPROVE_TITLE_03 = "待业务处审核";
	public static final String APPROVE_TITLE_04 = "待接收纸质材料";
	public static final String APPROVE_TITLE_05 = "科技厅已受理";
	public static final String APPROVE_TITLE_08 = "给予立项";
	public static final String APPROVE_TITLE_09 = "不予立项";

	public static final String APPROVE_EN_TITLE_01 = "Under review by organization";
	public static final String APPROVE_EN_TITLE_02 = "Under review by country";
	public static final String APPROVE_EN_TITLE_03 = "Under review by office";
	public static final String APPROVE_EN_TITLE_04 = "Under review by checkpoint";
	public static final String APPROVE_EN_TITLE_05 = "Has checked";

	// 退回状态
	public static final String RETURN_TITLE_00 = "申请书填写中";
	public static final String RETURN_TITLE_01 = "申报单位退回";
	public static final String RETURN_TITLE_02 = "退回修改项目";
	public static final String RETURN_TITLE_03 = "业务处室退回";
	public static final String RETURN_TITLE_04 = "业务窗口退回";
	public static final String RETURN_TITLE_05 = "业务处室退回";
	public static final String RETURN_TITLE_10 = "退回修改项目";

	// 退回状态
	public static final String RETURN_EN_TITLE_00 = "Pending";
	public static final String RETURN_EN_TITLE_01 = "Return by organization";
	public static final String RETURN_EN_TITLE_02 = "Return by country";
	public static final String RETURN_EN_TITLE_03 = "Return by office";
	public static final String RETURN_EN_TITLE_04 = "Return by checkpoint";
	public static final String RETURN_EN_TITLE_05 = "Return by office";
	// public static final String RETURN_TITLE_04_07 = "国际合作局退回";

	/** 申请书状态名 **/

	public static final String PDF_STATUS_START = "1";// PDF开始打印
	public static final String PDF_STATUS_SUCCESS = "3";// PDF打印成功
	public static final String PDF_STATUS_FAIL = "4";// PDF打印失败

	/**
	 * 申报流程动作：退回修改
	 * */
	public static final String WF_PRP_DO_BACK = "0";
	/**
	 * 申报流程动作：审核通过
	 * */
	public static final String WF_PRP_DO_PASS = "1";
	/**
	 * 申报流程动作：申报拒绝
	 * */
	public static final String WF_PRP_DO_REJECTED = "2";
	/**
	 * 申报流程动作：审核不通过
	 * */
	public static final String WF_PRP_DO_NOTPASS = "3";
	/**
	 * 申报流程动作：审查不通过
	 * */
	public static final String WF_PRP_DO_AD_NOTPASS = "4";
	/**
	 * 已接纸质材料.
	 */
	public static final String PRP_HAS_ACCEPT = "06";
	/**
	 * 待接收纸质材料.
	 */
	public static final String PRP_BE_ACCEPT = "05";
}
