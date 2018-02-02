package com.iris.egrant.code.solr.extend.similarity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("solrSimilarityTitleManager")
@Transactional(rollbackFor = Exception.class)
public class SolrSimilarityTitleManager extends AbstractSolrSimilarityManager {

	@Override
    public String getEdismaxMM(){
    	return "50%";
    }
	
	@Override
	public float getResultIndexRate(){
		return 30f;
	}

	@Override
	public boolean isHl() {
		return true;
	}
	
}
