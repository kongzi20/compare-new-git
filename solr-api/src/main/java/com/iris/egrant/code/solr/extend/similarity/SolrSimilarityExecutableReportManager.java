package com.iris.egrant.code.solr.extend.similarity;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("solrSimilarityExecutableReportManager")
@Transactional(rollbackFor = Exception.class)
public class SolrSimilarityExecutableReportManager extends AbstractSolrSimilarityManager {
	

	@Override
    public String getEdismaxMM(){
    	return "50%";
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
