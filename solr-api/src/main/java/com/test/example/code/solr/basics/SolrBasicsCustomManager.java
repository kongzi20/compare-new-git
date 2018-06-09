package com.test.example.code.solr.basics;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.solr.util.testSolrUtils;
import com.test.example.core.exception.ServiceException;
 

@Service("solrBasicsCustomManager")
@Transactional(rollbackFor = Exception.class)
public class SolrBasicsCustomManager implements SolrBasicsManager {

	@Override
	public void InitSolrClient(String solrCore)  throws ServiceException{
		
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
		try {
			testSolrUtils.InitSolrClientLocal(solrCore);
		} catch (Exception e) {
			throw new ServiceException("solr客户端打开失败solrCore="+solrCore+e.getMessage());
		}
		
	}
	
	@Override
    public void CloseSolrClient()  throws ServiceException{
		
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
		try {
			testSolrUtils.CloseSolrClientLocal();
		} catch (Exception e) {
			throw new ServiceException("solr客户端关闭失败"+e.getMessage());
		}
		
	}
}
