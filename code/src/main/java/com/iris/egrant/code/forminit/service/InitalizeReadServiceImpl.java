package com.iris.egrant.code.forminit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.egrant.code.forminit.dao.FormBaseLibraryDao;
import com.iris.egrant.code.forminit.model.FormBaseLibrary;
import com.iris.egrant.code.forminit.model.FormInitItem;
import com.iris.egrant.code.grantsetting.dao.GrantSettingDao;
import com.iris.egrant.code.grantsetting.model.GrantSetting;
import com.iris.egrant.code.proposal.service.BaseXmlInitServiceImpl;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.exception.ServiceException;


 


/**
 * 通用XML初始化实现接口.
 * 
 * 
 * @version $Rev$ $Date$
 */
@Service("InitializeReadService")
@Transactional(propagation = org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED,readOnly=true)
public class InitalizeReadServiceImpl extends BaseXmlInitServiceImpl implements InitalizeReadService{
	
	@Autowired
	private FormBaseLibraryDao formBaseLibraryDao;
	@Autowired
	private GrantSettingDao grantSettingDao;
	
	

	public FormBaseLibrary getFormBaseLibraryDao(Long formId) {
		return formBaseLibraryDao.get(formId);
	}
	

	public List<Map<String, Object>> getRefreshList(String sqlcontent, List<Object> params) {
		return formBaseLibraryDao.getRefreshList(sqlcontent,params);
	}
	

	public FormBaseLibrary getFormBaseLibraryByFormId(Long formId) throws DaoException {
		return formBaseLibraryDao.getFormBaseLibraryByFormId(formId);
	}
	

	public FormBaseLibrary getFormBaseLibraryByFormCode(Long formId) throws DaoException {
		return formBaseLibraryDao.getFormBaseLibraryByFormCode(formId);
	}
	
		public List<FormInitItem> getFormInitItemListById(String ids) throws DaoException, ServiceException {
		return formBaseLibraryDao.getFormInitItemListById(ids);
	}


	public GrantSetting getGrantInfoByGrantNo(String subGrantNo) {
		return grantSettingDao.getGrantInfoByGrantNo(subGrantNo);
	}
	


	public GrantSetting getGrantInfoByGrantCode(Long grantCode) {
		return grantSettingDao.get(grantCode);
	}

	
}
