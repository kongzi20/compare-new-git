package com.test.example.code.forminit.service;

import java.util.List;
import java.util.Map;

import com.test.example.code.forminit.model.FormBaseLibrary;
import com.test.example.code.forminit.model.FormInitItem;
import com.test.example.code.proposal.service.BaseXmlInitService;
import com.test.example.core.exception.ServiceException;

/**
 * 通用初始化类.
 * 
 * 
 * @version $Rev$ $Date$
 */

public interface InitializeService extends BaseXmlInitService {

    /**
     * 获得初始化信息.
     */
    public FormBaseLibrary getFormBaseLibraryByGrantId(Long grantCode, Long formCode, String xmlData,
        Map<String, Object> codes) throws ServiceException;

    public String refreshData(List<FormInitItem> formInitItems, String xmlData, Integer refrashType,
			Map<String, Object> codes) throws ServiceException ;
}