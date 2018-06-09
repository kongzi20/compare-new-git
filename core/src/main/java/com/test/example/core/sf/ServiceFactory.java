package com.test.example.core.sf;

import java.io.Serializable;

/**
 * 服务工厂接口.
 * 
 * 
 * @author liqinghua
 */
public interface ServiceFactory extends Serializable {

    /**
     * 获取服务，通过class.
     * 
     * @param clazz
     * @return
     */
    public <T> T getService(Class<T> clazz);

    /**
     * 获取服务，通过beanId.
     * 
     * @param beanId
     * @param clazz
     * @return
     */
    public <T> T getService(String beanId, Class<T> clazz);
}