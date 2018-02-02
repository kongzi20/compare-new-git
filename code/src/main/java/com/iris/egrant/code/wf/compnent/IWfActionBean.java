package com.iris.egrant.code.wf.compnent;

import java.io.Serializable;
import java.util.Map;

import com.iris.egrant.core.exception.ServiceException;

/**
 * 执行模板bean接口.
 * 
 * 
 * @author chenxiangrong
 */
public interface IWfActionBean extends Serializable {

    /**
     * 执行操作.
     * 
     * @param aid
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    public boolean execute(Map<String, Object> paramMap) throws ServiceException;
}
