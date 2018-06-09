package com.test.example.code.solr.document;

import com.test.example.core.cp.model.CompareListInfo;
import com.test.example.core.exception.ServiceException;

public interface SolrDocumentSimilarityManager {

	
	/**
	 * 导入所有项目（6维度）
	 * @param solrUrl solr地址
	 * @param type 项目类型
	 * @param dataTypes 维度类型
	 * @throws ServiceException
	 */
	public void addAllDocumentBeans(String type, String[] dataTypes ) throws ServiceException;
	
	
	/**
	 * 导入所有项目（单维度）
	 * @param solrUrl solr地址
	 * @param type 项目类型
	 * @param dataTypes 维度类型
	 * @param sysType 系统类型
	 * @throws ServiceException
	 */
	public void addAllDocumentBeans(String type, String[] dataTypes , String sysType) throws ServiceException;
	
	
	/**
	 * 导入单个项目（以keycode为主键）
	 * @param solrUrl
	 * @param keyCode
	 * @param type
	 * @param dataTypes
	 * @throws ServiceException
	 */
	public void addDocumentBeans(String keyCode, String type, String[] dataTypes ) throws ServiceException;
	
	/**
	 * 导入单个项目（以keycode为主键）
	 * @param solrUrl
	 * @param keyCode
	 * @param type
	 * @param dataTypes
	 * @param sysType
	 * @throws ServiceException
	 */
	public void addDocumentBeans(String keyCode, String type, String[] dataTypes , String sysType) throws ServiceException;
	
	
	/**
	 * 导入单个项目（以compareListInfo为参数）
	 * @param solrUrl
	 * @param compareListInfo 项目对比实体
	 * @throws ServiceException
	 */
	public void addDocumentBeans(CompareListInfo compareListInfo) throws ServiceException;
	
	/**
	 * 导入单个项目（以compareListInfo为参数）
	 * @param solrUrl
	 * @param compareListInfo 项目对比实体
	 * @throws ServiceException
	 */
	public void addDocumentBeans(CompareListInfo compareListInfo , String sysType) throws ServiceException;
	
	
	/**
	 * 删除所有分词索引
	 * @param solrUrl
	 * @param solrCore
	 * @throws ServiceException
	 */
	public void delDocumentAll(String solrCore) throws ServiceException ;
	
	/**
	 * 删除所有分词索引
	 * @param solrUrl
	 * @param dataTypes
	 * @throws ServiceException
	 */
	public void delDocumentAll(String[] dataTypes) throws ServiceException ;
	
	/**
	 * 删除指定keyCode和type分词索引
	 * @param solrUrl
	 * @param dataTypes
	 */
	public void delDocumentBean(String[] dataTypes , String keyCode , String type) throws ServiceException;
	
	/**
	 * 删除指定keyCode和type分词索引（以compareListInfo为参数）
	 * @param solrUrl
	 * @param dataTypes
	 */
	public void delDocumentBean(CompareListInfo compareListInfo) throws ServiceException;
	
	/**
	 * 优化索引
	 * @param solrUrl
	 * @param solrCore
	 * @throws ServiceException
	 */
	public void optimize(String solrCore) throws ServiceException;
	
	/**
	 * 优化索引
	 * @param solrUrl
	 * @param dataTypes
	 * @throws ServiceException
	 */
	public void optimize(String[] dataTypes) throws ServiceException;
}
