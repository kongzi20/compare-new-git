package com.test.example.code.compare.service;

import java.io.Serializable;

/**
 * 系统配置信息.
 * 
 * @author liqinghua
 * 
 */
 
public interface AppSettingService extends Serializable {

	/**
	 * 获取配置值.
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key);

	/**
	 * 获取int配置值.
	 * 
	 * @param key
	 * @return
	 */
	public Integer getIntValue(String key);

	/**
	 * 获取long配置值.
	 * 
	 * @param key
	 * @return
	 */
	public Long getLongValue(String key);

	/**
	 * 清理所有配置信息.
	 */
	public void clearAll();

	/**
	 * 设置配置值.
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value);

	/**
	 * 将日期作为文件路径返回
	 * */
	public String getDate4Path();
}
