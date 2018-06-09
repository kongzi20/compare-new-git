package com.test.example.code.forminit.service;

import java.util.List;
import java.util.Map;

import com.test.example.code.forminit.model.FormBaseLibrary;
import com.test.example.code.forminit.model.FormInitItem;
import com.test.example.code.grantsetting.model.GrantSetting;
import com.test.example.code.proposal.service.BaseXmlInitService;
import com.test.example.core.exception.DaoException;
import com.test.example.core.exception.ServiceException;


public interface InitalizeReadService extends BaseXmlInitService {
    
    
	
	
	public FormBaseLibrary getFormBaseLibraryDao(Long formId);
	
	public List<Map<String, Object>> getRefreshList(String sqlcontent, List<Object> params) ;
	
	
	public FormBaseLibrary getFormBaseLibraryByFormId(Long formId) throws DaoException;
	
	public FormBaseLibrary getFormBaseLibraryByFormCode(Long formId) throws DaoException ;
	
	
	public List<FormInitItem> getFormInitItemListById(String ids) throws DaoException, ServiceException ;
	
	public GrantSetting getGrantInfoByGrantNo(String subGrantNo) ;
	

	
	public GrantSetting getGrantInfoByGrantCode(Long grantCode) ;

}
