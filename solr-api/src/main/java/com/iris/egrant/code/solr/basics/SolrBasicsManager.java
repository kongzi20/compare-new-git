package com.iris.egrant.code.solr.basics;

import com.iris.egrant.core.exception.ServiceException;

 
public interface SolrBasicsManager {

	public void InitSolrClient(String solrCore)  throws ServiceException;
	
	public void CloseSolrClient()  throws ServiceException;
}
