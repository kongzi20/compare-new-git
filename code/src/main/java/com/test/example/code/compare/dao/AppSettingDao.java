package com.test.example.code.compare.dao;

import org.springframework.stereotype.Repository;

import com.test.example.code.compare.model.AppSetting;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;

 
/**
 * 系统配置信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class AppSettingDao extends SimpleHibernateDao<AppSetting, Long> {

	/**
	 * 获取KEY对应的系统配置.
	 * 
	 * @param key
	 * @return
	 */
	public String getAppSettingValue(String key) {
		return super.findUnique("select value from AppSetting where key = ? ", key.toLowerCase());
	}

	public void setAppSettingValue(String key, String value) {
		super.createQuery("update AppSetting t set t.value = ? where t.key = ? ", value, key.toLowerCase())
				.executeUpdate();
	}
}