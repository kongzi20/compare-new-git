package com.test.example.code.wf.compnent;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.test.example.code.wf.dao.WfCommonDao;
import com.test.example.code.wf.model.WfMessage;
import com.test.example.code.wf.model.WfRule;
import com.test.example.code.wf.utils.WfMessageUtils;
import com.test.example.core.exception.ServiceException;

@Component
public class WorkFlowHqlCompnent {
    @Resource(name = "wfCommonDao")
    private WfCommonDao wfCommonDao;

    /**
     * 执行Hql.
     * 
     * @param condition
     * @param param
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
    public WfMessage getHqlEngRule(WfRule wfRule, Map<String, Object> param) throws ServiceException {
        Map<String, Object> hqlTemp = parseSql(wfRule.getExpression(), param);
        String queryHql = (String) hqlTemp.get("hql");
        final List<Object> sqlParam = (List<Object>) hqlTemp.get("param");

        boolean engRuleResult = wfCommonDao.getCountResult(queryHql, sqlParam);
        WfMessage wfMessage =
            new WfMessage(wfRule.getId(), engRuleResult, wfRule.getMsgZhCn(),
            		wfRule.getMsgEnUs(), wfRule.getMsgZhTw());
        WfMessageUtils.builderMessage(wfMessage, param);
        return wfMessage;
    }

    /**
     * 执行hql.
     * 
     * @param hql
     * @param param
     */
    @SuppressWarnings("unchecked")
    public void executeHql(String hql, Map<String, Object> param) throws ServiceException {
        Map<String, Object> hqlTemp = parseSql(hql, param);
        hql = (String) hqlTemp.get("hql");
        final List<Object> sqlParam = ((List<Object>) hqlTemp.get("param"));
        wfCommonDao.executeHql(hql, sqlParam);
    }

    /**
     * 解析SQL语言，拆分SQL参数.
     * 
     * @param hql
     * @param param
     * @return
     * @throws ServiceException
     */
    public Map<String, Object> parseSql(String hql, Map<String, Object> param) throws ServiceException {
        Pattern pattern = Pattern.compile("@[\\w]+");
        Matcher matcher = pattern.matcher(hql);
        List<Object> sqlParam = new ArrayList<Object>();
        while (matcher.find()) {
            String rpparam = matcher.group();
            String paramName = rpparam.substring(1);
            if (param.containsKey(paramName)) {
                Object paramValue = param.get(paramName);
                if (paramValue instanceof Collection) {
                    @SuppressWarnings("rawtypes")
                    Collection pvc = (Collection) paramValue;
                    StringBuilder sb = new StringBuilder();
                    for (Object obj : pvc) {
                        sqlParam.add(obj);
                        if (sb.length() > 0) {
                            sb.append(",?");
                        } else {
                            sb.append("?");
                        }
                    }
                    hql = hql.replace(rpparam, sb.toString());
                } else {
                    hql = hql.replace(rpparam, "?");
                    sqlParam.add(param.get(paramName));
                }
            } else {
                throw new ServiceException("sql语句" + hql + "参数:" + rpparam + "不存在");
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("hql", hql);
        result.put("param", sqlParam);
        return result;
    }

    /**
     * 执行SQL获取参数.
     * 
     * @param hql
     * @param param
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSqlParam(String hql, Map<String, Object> param) throws ServiceException {
        Map<String, Object> hqlTemp = parseSql(hql, param);
        String queryHql = (String) hqlTemp.get("hql");
        final List<Object> sqlParam = (List<Object>) hqlTemp.get("param");

        return convertBeanToMap(wfCommonDao.findUnique(queryHql, sqlParam.toArray()));
    }

    /**
     * Object转换Map.
     * 
     * @param bean
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Object> convertBeanToMap(Object bean) throws ServiceException {
        try {
            Class type = bean.getClass();
            Map<String, Object> returnMap = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
            return returnMap;
        } catch (Exception e) {
            throw new ServiceException("Object转换Map时出错啦！", e);
        }
    }

}
