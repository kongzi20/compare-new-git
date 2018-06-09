package com.test.example.code.solr.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.solr.basics.SolrBasicsManager;
import com.test.example.code.solr.constant.TypeConstants;
import com.test.example.code.solr.extend.similarity.SolrSimilarityManager;
import com.test.example.core.cp.model.CompareListInfo;
import com.test.example.core.exception.ServiceException;
import com.test.example.core.sf.ServiceFactory;
import com.test.example.core.utils.testStringUtils;
 
@Service("solrQuerySimilarityCustomManager")
@Transactional(rollbackFor = Exception.class)
public class SolrQuerySimilarityCustomManager implements SolrQuerySimilarityManager {
	

	@Resource(name = "serviceFactory")
	private ServiceFactory serviceFactory;	
	
	@Value("${solr.sys.type}")
	private String systype ;
	
	@Override
	public List<Map<String,Object>> query(CompareListInfo compareListInfo) throws ServiceException{
		return query(compareListInfo , systype);
	}
	
	@Override
	public List<Map<String,Object>> query(CompareListInfo compareListInfo , String systype) throws ServiceException{
		
		if(compareListInfo==null){
			throw new ServiceException("参数compareListInfo不能为空");
		}
		String solrCore = TypeConstants.solrCoresTypeMap.get(ObjectUtils.toString(compareListInfo.getDataType()));
		if(solrCore==null){
			throw new ServiceException("solr没有该库"+compareListInfo.getDataType());
		}
		String dataTypeVal = TypeConstants.typeMap.get(ObjectUtils.toString(compareListInfo.getDataType()));
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		try {
			
			serviceFactory.getService("solrBasicsCustomManager",SolrBasicsManager.class).InitSolrClient(solrCore);
			
			String content = testStringUtils.replaceRegex(ObjectUtils.toString(compareListInfo.getContent()));
			//出现因为字符长度过大搜索出问题 就修改指定的collection/conf中的solrconfig.xml文件,搜索<maxBooleanClauses>1024</maxBooleanClauses>字段,1024根据实际情况改大一点
			
			if(StringUtils.isBlank(content)){ // 空字符串 不查询
				return resultList;
			}
			
			resultList = serviceFactory.getService("solrSimilarity"+dataTypeVal+"Manager",SolrSimilarityManager.class).query(ObjectUtils.toString(compareListInfo.getId()), content ,  ObjectUtils.toString(compareListInfo.getType()) ,  systype);
			
			
		} catch (Exception e) {
    		throw new ServiceException("solr查询失败id="+compareListInfo.getId()+"AND type="+compareListInfo.getType()+"AND solrCore="+solrCore+e.getMessage());
		} finally {
			try {
				serviceFactory.getService("solrBasicsCustomManager",SolrBasicsManager.class).CloseSolrClient();
			} catch (ServiceException e) {
				throw new ServiceException("solr客户端关闭失败"+e.getMessage());
			}
		}
		
		return resultList;
		
	}

}
