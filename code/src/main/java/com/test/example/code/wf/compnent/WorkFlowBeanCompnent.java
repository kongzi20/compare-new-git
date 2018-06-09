package com.test.example.code.wf.compnent;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import com.test.example.code.wf.model.WfMessage;
import com.test.example.code.wf.model.WfRule;
import com.test.example.core.exception.ServiceException;


/**
 * bean执行公用组件.
 * 
 * @author chenxiangrong
 */

@Component
public class WorkFlowBeanCompnent implements BeanFactoryAware {
    protected BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * bean判断.
     * 
     * @param beanId
     * @param param
     * @return
     * @throws ServiceException
     */
    public WfMessage getBeanEngRule(WfRule wfRule, Map<String, Object> param) throws ServiceException {
        IWfEngRuleBean bean = (IWfEngRuleBean) beanFactory.getBean(wfRule.getExpression());
        return bean.engRuleBean(wfRule, param);
    }

    /**
     * 获取bean参数.
     * 
     * @param beanId
     * @param param
     * @return
     * @throws ServiceException
     */
    public Map<String, Object> getBeanParam(String beanId, Map<String, Object> param) throws ServiceException {

        IWfParamBean bean = (IWfParamBean) beanFactory.getBean(beanId);
        return bean.getParam(param);
    }

    /**
     * 执行bean.
     * 
     * @param beanId
     * @param param
     * @throws ServiceException
     */
    public boolean executeBean(String beanId, Map<String, Object> param) throws ServiceException {
        IWfActionBean bean = (IWfActionBean) beanFactory.getBean(beanId);
        return bean.execute(param);
    }
}