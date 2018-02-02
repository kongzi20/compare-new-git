package com.iris.egrant.code.solr.extend.similarity;

import java.util.List;
import java.util.Map;

import com.iris.egrant.core.exception.ServiceException;

public interface SolrSimilarityManager {

	/**
	 * 查询
	 * @param id 主键（主要作用为排序查自身）
	 * @param content 搜索关键字
	 * @param type 数据类型（对应不同的prp，ctr等）
	 * @param systype 系统类型
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> query(String id ,String content , String type , String systype) throws ServiceException;
		
	/**
	 * 搜索关键字的索引命中占比
	 * @return
	 */
	public String getEdismaxMM();
	
	/**
	 * 结果集索引命中占比（isHl()返回true才有效）
	 * @return
	 */
	public float getResultIndexRate();
	
	/**
	 * 是否需要高亮计算
	 * @return
	 */
	public boolean isHl();

}
