package com.iris.egrant.code.forminit.service;

import java.util.List;
import java.util.Map;

import com.iris.egrant.code.forminit.model.FormBaseLibrary;
import com.iris.egrant.code.forminit.model.FormInitItem;
import com.iris.egrant.code.proposal.service.BaseXmlInitService;
import com.iris.egrant.core.exception.ServiceException;

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