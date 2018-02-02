package com.iris.egrant.code.forminit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import com.iris.egrant.code.forminit.dao.FormBaseLibraryDao;
import com.iris.egrant.code.project.dao.ProjectDao;
import com.iris.egrant.code.project.model.Project;
import com.iris.egrant.code.system.dao.OrganizationExtendDao;
import com.iris.egrant.code.system.dao.PersonDao;
import com.iris.egrant.code.system.dao.PersonExtendDao;
import com.iris.egrant.code.system.model.OrganizationExtend;
import com.iris.egrant.code.system.model.Person;


@Service("baseXmlNoTransInitService")
@Transactional(readOnly=true)
public class BaseXmlNoTransInitServiceImpl implements BaseXmlNoTransInitService {

	public PersonExtendDao getPsnExtendDao() {
		return psnExtendDao;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public OrganizationExtendDao getOrgExtendDao() {
		return orgExtendDao;
	}

	public ProjectDao getProjectDao() {
		return projectDao;
	}

	public FormBaseLibraryDao getFblDao() {
		return fblDao;
	}

	@Autowired
	private PersonExtendDao psnExtendDao;// 个人详细信息Dao
	@Autowired
	private PersonDao personDao;// 个人详细信息Dao
	@Autowired
	private OrganizationExtendDao orgExtendDao;// 单位详细信息Dao
	@Autowired
	private ProjectDao projectDao;// 单位详细信息Dao

	@Autowired
	private FormBaseLibraryDao fblDao;// 通用刷新Dao
	
	@Override
	public List<Map<String, Object>> getRefreshList(String sqlcontent, List<Object> params){
		return this.fblDao.getRefreshList(sqlcontent, params);
	}
	
	@Override
	public Document getPersonExtend(Long psnCode){
		return this.psnExtendDao.getPersonExtend(psnCode);
	}

	@Override
	public Person getPerson(Long psnCode){
		return this.personDao.get(psnCode);
	}

	@Override
	public OrganizationExtend getOrgExtendInfo(Long objOrgCode){
		return  this.orgExtendDao.getOrgExtendInfo(objOrgCode);
	}

	@Override
 	public Project getProject(Long prjCode){
 		return this.projectDao.get(prjCode);
 	}
 	
	
}
