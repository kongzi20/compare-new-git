package com.test.example.code.system.dao;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.test.example.code.system.model.PersonExtend;
import com.test.example.code.system.model.PersonExtend4Sync;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;

/**
 * 人员扩展表dao.
 */
@Repository
public class PersonExtendDao extends SimpleHibernateDao<PersonExtend, Long> {
    /**
     * 保存人员 扩展信息.
     * 
     * @param person
     */
    public void createPersonExtend4Sync(PersonExtend4Sync personExt) {
        this.getSession().merge(personExt);
    }

    /**
     * 取人员扩展信息用于同步.
     * 
     * @param psnCode
     * @return
     */
    public PersonExtend4Sync getPersonExtend4Sync(Long psnCode) {

        return super.findUnique("from PersonExtend4Sync where psnCode = ? ", psnCode);
    }

    /**
     * 获取人员扩张信息
     * 
     * @param psnCode
     * @return
     */
    @Cacheable(value = "psnXml", key = "#psnCode")
    public org.w3c.dom.Document getPersonExtend(Long psnCode) {
        return super.get(psnCode).getExtInfo();
    }

    /**
     * 清除缓存
     */
    @CacheEvict(value = "psnXml", key = "#psnCode", condition = "#psnCode !=null")
    public void removePersonExtendCache(Long psnCode) {

    }
    
    /**
     * 获取人员扩展信息
     * @param psnCode
     * @return 找不到返回空
     */
    @SuppressWarnings("unchecked")
	public PersonExtend getPersonExtendInfo(Long psnCode) {
    	String hql = "from PersonExtend where psn_code=?";
    	List<PersonExtend> list = super.createQuery(hql, psnCode).list();
    	
    	if (list != null && list.size() > 0) {
    		return list.get(0);
    	}
    	return null;
    }
}
