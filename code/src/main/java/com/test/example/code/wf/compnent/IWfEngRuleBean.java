package com.test.example.code.wf.compnent;

import java.io.Serializable;
import java.util.Map;

import com.test.example.code.wf.model.WfMessage;
import com.test.example.code.wf.model.WfRule;
import com.test.example.core.exception.ServiceException;

/**
 * 模板bean接口.
 * 
 * 
 * @author chenxiangrong
 */
public interface IWfEngRuleBean extends Serializable {

    /**
     * 执行bean.
     * 
     * @param wfRule
     * @param paramMap
     * @return
     * @throws ServiceException
     */
    public WfMessage engRuleBean(WfRule wfRule, Map<String, Object> paramMap) throws ServiceException;
}
