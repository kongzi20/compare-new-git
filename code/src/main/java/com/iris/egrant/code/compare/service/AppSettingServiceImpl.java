package com.iris.egrant.code.compare.service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.egrant.code.compare.dao.AppSettingDao;
import com.iris.egrant.core.utils.DateFormator;
import com.iris.egrant.core.utils.DateUtils;
 

/**
 * 系统配置信息.
 * 
 * @author liqinghua
 * 
 */
@Service("appSettingService")
@Transactional(rollbackFor = Exception.class)
public class AppSettingServiceImpl implements AppSettingService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6888186034033943269L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AppSettingDao appSettingDao;
	// 缓存
	private final Map<String, String> cache = new HashMap<String, String>();

	@Override
	public String getValue(String key) {

		try {
			String value = this.cache.get(key);
			if (value == null) {
				value = appSettingDao.getAppSettingValue(key);
				if (value != null) {
					this.cache.put(key, value);
				}
			}
			return value;
		} catch (Exception e) {
			logger.error("获取配置值异常", e);
		}
		return null;
	}

	@Override
	public Integer getIntValue(String key) {
		String value = this.getValue(key);
		if (NumberUtils.isNumber(value)) {
			return Integer.valueOf(value);
		}
		return null;
	}

	@Override
	public Long getLongValue(String key) {

		String value = this.getValue(key);
		if (NumberUtils.isNumber(value)) {
			return Long.valueOf(value);
		}
		return null;
	}

	@Override
	public void clearAll() {
		try {
			this.cache.clear();
		} catch (Exception e) {
			logger.error("清理所有配置信息错误", e);
		}
	}

	@Override
	public void setValue(String key, String value) {
		appSettingDao.setAppSettingValue(key, value);
	}

	@Override
	public String getDate4Path() {
		String nowArr[] = DateUtils.toString(new Date(), DateFormator.YEAR_MONTH_DAY).split(
				DateFormator.SPLIT_CHAR.toString());
		String strPath = nowArr[0] + File.separator + nowArr[1] + File.separator + nowArr[2] + File.separator;
		return strPath;
	}

}
