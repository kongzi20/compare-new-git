package com.test.example.code.solr.extend.similarity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
@Service("solrSimilarityContentManager")
@Transactional(rollbackFor = Exception.class)
public class SolrSimilarityContentManager extends AbstractSolrSimilarityManager {

	
	@Override
    public String getEdismaxMM(){
    	return "60%";
    }
	
	@Override
	public float getResultIndexRate(){
		return 0f;
	}

	@Override
	public boolean isHl() {
		return false;
	}
	
	
}
