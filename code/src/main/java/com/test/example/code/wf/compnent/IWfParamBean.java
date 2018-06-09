package com.test.example.code.wf.compnent;

import java.io.Serializable;
import java.util.Map;

import com.test.example.core.exception.ServiceException;

/**
 * 参数获取接口.
 * 
 * 
 * @version $Rev$ $Date$
 */
public interface IWfParamBean extends Serializable {

    /**
     * 参数获取bean，返回结果.
     * 
     * @param aid
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    public Map<String, Object> getParam(Map<String, Object> paramMap) throws ServiceException;
}
