package com.iris.egrant.code.compare.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iris.egrant.code.compare.model.CompareSource;
import com.iris.egrant.core.exception.ServiceException;

 

public interface CompareSourceService extends Serializable {

	/**
	 * /** 合并每个业务code内容
	 * 
	 * @param cs
	 * @throws ServiceException
	 */
	public void mergeContent(CompareSource cs) throws ServiceException;

	/**
	 * 批量获取业务code
	 * 
	 * @param startId
	 * @param fetchSize
	 * @return
	 * @throws ServiceException
	 */
	public List getKeyCode(int startId, int fetchSize) throws ServiceException;

	/**
	 * 修改对比数据来源表
	 * 
	 * @param cs
	 * @throws ServiceException
	 */
	public void updateCompareSource(CompareSource cs) throws ServiceException;

	/**
	 * 获取待处理的compareSourceList
	 */
	public List<CompareSource> getCompareSourceList(int startId, int fetchSize) throws ServiceException;

	/**
	 * 获取内容
	 * 
	 * @param cs
	 * @return
	 * @throws ServiceException
	 */
	public String getContent(CompareSource cs) throws ServiceException;
	
	/**
	 * wk add 2014-5-12
	 * 根据dataType抽取待比较内容
	 * 
	 * @param cs
	 * @return
	 * @throws ServiceException
	 */
	public String getContentByDataType(CompareSource cs) throws ServiceException;

	/**
	 * 执行回调存储过程
	 * 
	 * @param cs
	 * @return
	 * @throws ServiceException
	 */
	public void callExec(Long keyCode, Integer type, String content) throws ServiceException;

	/**
	 * wk add 2014-5-12
	 * 根据dataType初始化表compare_list和表compare_result数据
	 * 
	 * @param cs
	 * @return
	 * @throws ServiceException
	 */
	public void callExecByDataType(Long keyCode, Integer type, Integer dataType, String content) throws ServiceException;

	public String getDBContent(long prpCode, int type, int dataType, String path,String path2);
	public List<Map<String,Object>> getSpecialCompareNodeByGrantCode(Long grantCode,Integer type,Integer dataType) ;

	
}