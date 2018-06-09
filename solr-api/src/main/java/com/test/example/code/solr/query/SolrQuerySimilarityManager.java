package com.test.example.code.solr.query;

import java.util.List;
import java.util.Map;

import com.test.example.core.cp.model.CompareListInfo;
import com.test.example.core.exception.ServiceException;
 
public interface SolrQuerySimilarityManager {

		
	/**
	 * 查询 （以compareListInfo为参数） 
	 * @param compareListInfo 项目对比实体
	 * @return 
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> query(CompareListInfo compareListInfo) throws ServiceException;
	
	/**
	 * 查询 （以compareListInfo为参数） 
	 * @param compareListInfo 项目对比实体
	 * @param systype 系统类型
	 * @return 
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> query(CompareListInfo compareListInfo , String systype) throws ServiceException;
	
	
}
