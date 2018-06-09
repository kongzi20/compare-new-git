package com.test.example.code.wf.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.test.example.core.dao.hibernate.SimpleHibernateDao;


/**
 * Dao通用.
 * 
 * @author chenxiangrong
 */
@SuppressWarnings("rawtypes")
@Component
public class WfCommonDao extends SimpleHibernateDao {
    /**
     * 获取结果集.
     * 
     * @param hql
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object> getResultList(String hql, List<Object> param) {
        List<Object> list = super.createQuery(hql, param.toArray()).list();
        return list;
    }

    /**
     * 获取是否有结果.
     * 
     * @param hql
     * @param param
     * @return
     */
    public boolean getCountResult(String hql, List<Object> param) {
        Long count = (Long) super.findUnique(hql, param.toArray());
        return count > 0;
    }

    public void executeHql(String hql, List<Object> param) {
        super.createQuery(hql, param.toArray()).executeUpdate();
    }
}
