package com.test.example.core.utils;

/**
 * service常量.
 * 
 * 
 */
public class ServiceConstants {

	public static final String ENCRYPT_KEY = "asdf(ad@dsd*4fsafas-=34s"; // 必须大于20位，否则报错。

	public static final String IDPATTERN = "^(\\d+)(,\\d+)*$";

	public static final String PASSWORD = "password";

	public static final String ORG_XML = "ORGANIZATION";// 单位XML

	public static final String PSN_XML = "PERSON";// 个人XML

	public static final String PRP_XML = "PROPOSAL";// 申请书XML
	public static final String PRJ_XML = "PROJECT";// 申请书XML

	public static final String JJW_ROLE_ID = "2565";// 基金委rolId

	public static final String WS_SYNC_PSN_PUBS_KEY = "111111222222333333444444";// webservice同步人员项目成果信息对psn guid的加密和解密

	public static final String SYNC_PSN_PUBS_PARAM_KEY = "Rbnsc9%2BP0AMbUXyYOMPn8LUfQhP1rZubx%2F0bP%2BLxNrBgVvT3kJUWhw%3D%3D";// webservice同步人员项目成果信息固定密钥

	public static final String CODE_KEY = "111111222222333333444444";// 针对页面的code明文进行加密的密钥

	public static final Long VALIDATE_TIME = 2L * 3600L * 1000L;// 页面链接时间，默认两小时
}
