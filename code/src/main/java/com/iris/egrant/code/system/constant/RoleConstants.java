package com.iris.egrant.code.system.constant;

/**
 * @author liqinghua
 */
public interface RoleConstants {

	/**
	 * 0 匿名用户，未验证用户所具有的权限.
	 */
	public static final int ANONYMOUS_USER_INT = 0;

	/**
	 * 0 匿名用户，未验证用户所具有的权限.
	 */
	public static final long ANONYMOUS_USER_LONG = 0;

	/**
	 * 1 申请人，对于注册的人员，均有该角色.
	 */
	public static final int APPLY_USER_INT = 1;

	/**
	 * 1 申请人，对于注册的人员，均有该角色.
	 */
	public static final long APPLY_USER_LONG = 1L;

	/**
	 * 3 申请人，对于注册的人员，均有该角色. add by liulijie 2012/5/21
	 */
	public static final int PROPOSER_USER = 3;

	/**
	 * 2 项目负责人.
	 */
	public static final int PROJECT_PRINCIPAL_INT = 2;

	/**
	 * 2 项目负责人.
	 */
	public static final long PROJECT_PRINCIPAL_LONG = 2L;

	/**
	 * 8 项目支援员工.
	 */
	public static final int PRJ_SUPORT_USER_INT = 8;

	/**
	 * 8 项目支援员工.
	 */
	public static final long PRJ_SUPORT_USER_LONG = 8L;

	/**
	 * 9 项目评议人.
	 */
	public static final int PRJ_REVIEW_INT = 9;

	/**
	 * 9 项目评议人.
	 */
	public static final long PRJ_REVIEW_LONG = 9L;
	/**
	 * 12 外国青年申请人.
	 */
	public static final int FOREIGN_YOUNG_INT = 12;
	/**
	 * 12 外国青年申请人.
	 */
	public static final long FOREIGN_YOUNG_LONG = 12L;
	/**
	 * 专家库管理员.
	 */
	public static final int REVIEW_EXPERT_ADMIN = 24;

	/**
	 * 评价人员
	 */
	public static final int EVA_PERSON_INT = 2211;

	/**
	 * 评审专家.
	 */
	public static final int REVIEW_EXPERT_INT = 25;
	/**
	 * 评审专家.
	 */
	public static final long REVIEW_EXPERT_LONG = 25L;

	/**
	 * 临时评审专家.
	 */
	public static final int TEMPORARY_REVIEW_EXPERT_INT = 10;
	/**
	 * 临时评审专家.
	 */
	public static final long TEMPORARY_REVIEW_EXPERT_LONG = 10L;

	/**
	 * 26 单位联系人.单位管理员.
	 */
	public static final int ORG_CONTACT_INT = 26;
	/**
	 * 26 单位联系人.单位管理员.
	 */
	public static final long ORG_CONTACT_LONG = 26L;

	/**
	 * 21 人才基金联络人，对基础人才类数据进行管理.
	 */
	public static final int TALENTS_FUND_INT = 21;
	/**
	 * 21 人才基金联络人，对基础人才类数据进行管理.
	 */
	public static final long TALENTS_FUND_LONG = 21L;

	/**
	 * 22 二级单位联系人(部门联系人)，.
	 */
	public static final int DEPARTMENT_CONTACT_INT = 22;
	/**
	 * 22 二级单位联系人(部门联系人)，.
	 */
	public static final long DEPARTMENT_CONTACT_LONG = 22L;

	/**
	 * 23 信息中心，对全委数据进行检索及统计管理.
	 */
	public static final int INFORMATION_CENTER_INT = 23;
	/**
	 * 23 信息中心，对全委数据进行检索及统计管理.
	 */
	public static final long INFORMATION_CENTER_LONG = 23L;

	/**
	 * 32 基金委用户，对全委数据进行普通检索功能.
	 */
	public static final int FUND_USER_INT = 32;
	/**
	 * 32 基金委用户，对全委数据进行普通检索功能.
	 */
	public static final long FUND_USER_LONG = 32L;

	/**
	 * 34 学科用户，对本学科范围内数据进行各种操作及管理 .
	 */
	public static final int SUBJECT_USER_INT = 34;
	/**
	 * 34 学科用户 ，对本学科范围内数据进行各种操作及管理.
	 */
	public static final long SUBJECT_USER_LONG = 34L;

	/**
	 * 35 学科赋权用户，对指定范围内项目进行操作及管理 .
	 */
	public static final int SUBJECT_ENDOW_INT = 35;
	/**
	 * 35 学科赋权用户， 对指定范围内项目进行操作及管理.
	 */
	public static final long SUBJECT_ENDOW_LONG = 35L;

	/**
	 * 36 学部用户，对本学部或本领域范围内数据进行管理 .
	 */
	public static final int DIVISIONS_USER_INT = 36;
	/**
	 * 36 学部用户 ，对本学部或本领域范围内数据进行管理.
	 */
	public static final long DIVISIONS_USER_LONG = 36L;

	/**
	 * 37 接收工作组 ，对集中接收项目进行检索及确认工作.
	 */
	public static final int ACCEPT_WORKGROUP_INT = 37;
	/**
	 * 37 接收工作组 ，对集中接收项目进行检索及确认工作.
	 */
	public static final long ACCEPT_WORKGROUP_LONG = 37L;

	/**
	 * 38 文印中心，只负责打印任务 .
	 */
	public static final int PRINT_CENTER_INT = 38;
	/**
	 * 38 文印中心，只负责打印任务 .
	 */
	public static final long PRINT_CENTER_LONG = 38L;

	/**
	 * 40 财务用户 ，对全委数据进行普通检索功能.
	 */
	public static final int FINANCE_USER_INT = 40;
	/**
	 * 40 财务用户，对全委数据进行普通检索功能.
	 */
	public static final long FINANCE_USER_LONG = 40L;

	/**
	 * 41 国际合作局，对国际合作类项目进行管理 .
	 */
	public static final int INTERNATION_COOFFICE_INT = 41;
	/**
	 * 41 国际合作局 ，对国际合作类项目进行管理.
	 */
	public static final long INTERNATION_COOFFICE_LONG = 41L;
	/**
	 * 42 计划局，对全委数据进行检索及统计管理以及部分类别项目的操作和管理 .
	 */
	public static final int SCHEME_OFFICE_INT = 42;
	/**
	 * 42 计划局 ，对全委数据进行检索及统计管理以及部分类别项目的操作和管理.
	 */
	public static final long SCHEME_OFFICE_LONG = 42L;

	/**
	 * 43 学部综合处，对本学部范围内数据进行检索及统计.
	 */
	public static final int DIVISIONS_SYNTHESIS_INT = 43;
	/**
	 * 43 学部综合处，对本学部范围内数据进行检索及统计.
	 */
	public static final long DIVISIONS_SYNTHESIS_LONG = 43L;

	/**
	 * 44 计划局综合处，对全委数据进行普通检索功能 .
	 */
	public static final int SCHEME_SYNTHESIS_INT = 44;
	/**
	 * 44 计划局综合处， 对全委数据进行普通检索功能.
	 */
	public static final long SCHEME_SYNTHESIS_LONG = 44L;

	/**
	 * 45 国际合作局计划处，对国际合作类项目进行管理 .
	 */
	public static final int INTERNATION_COSCHEME_INT = 45;
	/**
	 * 45 国际合作局计划处 ，对国际合作类项目进行管理.
	 */
	public static final long INTERNATION_COSCHEME_LONG = 45L;
	/**
	 * 46 计划局兼聘用户，对基础人才类数据进行操作及管理 .
	 */
	public static final int SCHEME_SIDELINE_INT = 46;
	/**
	 * 46 计划局兼聘用户， 对基础人才类数据进行操作及管理.
	 */
	public static final long SCHEME_SIDELINE_LONG = 46L;

	/**
	 * 47 政策局， 对全委数据进行普通检索功能.
	 */
	public static final int POLICY_OFFICE_INT = 47;
	/**
	 * 47 政策局 ，对全委数据进行普通检索功能.
	 */
	public static final long POLICY_OFFICE_LONG = 47L;

	/**
	 * 48 机关财务，对全委数据进行普通检索功能 .
	 */
	public static final int ORGAN_FINANCE_INT = 48;
	/**
	 * 48 机关财务，对全委数据进行普通检索功能.
	 */
	public static final long ORGAN_FINANCE_LONG = 48L;

	/**
	 * 49 计划局兼聘用户，发布项目统计 .
	 */
	public static final int SCHEME_SIDELINE2_INT = 49;
	/**
	 * 49 计划局兼聘用户，发布项目统计.
	 */
	public static final long SCHEME_SIDELINE2_LONG = 49L;

	/**
	 * 50 基金委委主任， 对全委数据进行普通检索功能.
	 */
	public static final int FUND_DIRECTOR_INT = 50;
	/**
	 * 50 基金委委主任，对全委数据进行普通检索功能.
	 */
	public static final long FUND_DIRECTOR_LONG = 50L;

	/**
	 * 52 基金杂志部，对全委数据进行普通检索功能 .
	 */
	public static final int FUND_JOURNAL_INT = 52;
	/**
	 * 52 基金杂志部，对全委数据进行普通检索功能.
	 */
	public static final long FUND_JOURNAL_LONG = 52L;

	/**
	 * 53 监审局，对全委数据进行普通检索功能 .
	 */
	public static final int SUPERVISION_DEPT_INT = 53;
	/**
	 * 53 监审局 ，对全委数据进行普通检索功能.
	 */
	public static final long SUPERVISION_DEPT_LONG = 53L;

	/**
	 * 54 应用系统管理员 .
	 */
	public static final int APP_SYSADMIN_INT = 53;
	/**
	 * 54 应用系统管理员 .
	 */
	public static final long APP_SYSADMIN_LONG = 53L;

	/**
	 * 55 审计用户组 .
	 */
	public static final int AUDIT_USER_INT = 55;
	/**
	 * 55 审计用户组 .
	 */
	public static final long AUDIT_USER_LONG = 55L;
}
