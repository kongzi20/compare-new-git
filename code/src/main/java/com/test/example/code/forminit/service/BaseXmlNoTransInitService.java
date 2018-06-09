package com.test.example.code.forminit.service;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import com.test.example.code.project.model.Project;
import com.test.example.code.system.model.OrganizationExtend;
import com.test.example.code.system.model.Person;

public interface BaseXmlNoTransInitService {

	public List<Map<String, Object>> getRefreshList(String sqlcontent, List<Object> params);
	
	public Document getPersonExtend(Long psnCode);
	
	public Person getPerson(Long psnCode);

	public OrganizationExtend getOrgExtendInfo(Long objOrgCode);
	
 	public Project getProject(Long prjCode);
 	
}
