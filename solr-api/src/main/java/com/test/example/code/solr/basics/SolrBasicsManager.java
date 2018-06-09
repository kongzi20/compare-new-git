package com.test.example.code.solr.basics;

import com.test.example.core.exception.ServiceException;

 
public interface SolrBasicsManager {

	public void InitSolrClient(String solrCore)  throws ServiceException;
	
	public void CloseSolrClient()  throws ServiceException;
}
