package com.iris.egrant.code.forminit.service;

import java.util.List;
import java.util.Map;

import com.iris.egrant.code.forminit.model.FormBaseLibrary;
import com.iris.egrant.code.forminit.model.FormInitItem;
import com.iris.egrant.code.grantsetting.model.GrantSetting;
import com.iris.egrant.code.proposal.service.BaseXmlInitService;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.exception.ServiceException;


public interface InitalizeReadService extends BaseXmlInitService {
    
    
	
	
	public FormBaseLibrary getFormBaseLibraryDao(Long formId);
	
	public List<Map<String, Object>> getRefreshList(String sqlcontent, List<Object> params) ;
	
	
	public FormBaseLibrary getFormBaseLibraryByFormId(Long formId) throws DaoException;
	
	public FormBaseLibrary getFormBaseLibraryByFormCode(Long formId) throws DaoException ;
	
	
	public List<FormInitItem> getFormInitItemListById(String ids) throws DaoException, ServiceException ;
	
	public GrantSetting getGrantInfoByGrantNo(String subGrantNo) ;
	

	
	public GrantSetting getGrantInfoByGrantCode(Long grantCode) ;

}
