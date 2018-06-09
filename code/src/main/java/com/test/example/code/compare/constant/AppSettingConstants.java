package com.test.example.code.compare.constant;

/**
 * 系统配置常量.
 * 
 * @author liqinghua
 * 
 */
public interface AppSettingConstants {

	/**
	 * 是否启用菜单缓存.
	 */
	String MENU_CACHE_ENABLED = "menu_cache_enabled";

	/**
	 * 是否启用bu工作提醒缓存.
	 */
	String BU_CACHE_ENABLED = "bu_cache_enabled";

	/**
	 * 是否启用isis同步数据到example的任务.
	 */
	String SYNCDATA_I2E_ENABLED = "syncdata_i2e_enabled";

	/**
	 * 是否启用example同步数据到isis的任务.
	 */
	String SYNCDATA_E2I_ENABLED = "syncdata_e2i_enabled";

	/**
	 * 系统当前年度.
	 */
	String SYNCDATA_STAT_YEAR = "stat_year";

	/**
	 * 除了申请之外的年度(需要查前一年).
	 */
	String SELECT_YEAR = "select_year";
	
	String DEFAULT_WIDTH = "70";

}
