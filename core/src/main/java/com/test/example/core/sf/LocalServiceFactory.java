package com.test.example.core.sf;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 
 * 本地服务工厂.
 * 
 * @author liqinghua
 */
public class LocalServiceFactory implements Serializable, ServiceFactory, BeanFactoryAware {

    private static final long serialVersionUID = -4829342420862083368L;
    private BeanFactory beanFactory;

    @Override
    public <T> T getService(Class<T> clazz) {

        String beanId = this.getServiceName(clazz, null);
        return beanFactory.getBean(beanId, clazz);
    }

    @Override
    public <T> T getService(String beanId, Class<T> clazz) {
        beanId = this.getServiceName(clazz, beanId);
        return beanFactory.getBean(beanId, clazz);
    }

    private <T> String getServiceName(final Class<T> clazz, final String serviceName) {
        String beanName = serviceName;
        if (StringUtils.isBlank(serviceName)) {
            // 得到HelloService
            beanName = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
            // 首字母小写,得到helloService
            beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        }
        return beanName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
