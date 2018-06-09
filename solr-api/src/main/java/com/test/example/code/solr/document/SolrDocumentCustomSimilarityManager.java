package com.test.example.code.solr.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.solr.constant.TypeConstants;
import com.test.example.code.solr.dao.SolrDocumentJDBCDao;
import com.test.example.code.solr.model.SolrItem;
import com.test.example.code.solr.util.testSolrUtils;
import com.test.example.core.cp.model.CompareListInfo;
import com.test.example.core.exception.ServiceException;
import com.test.example.core.utils.testStringUtils;

@Service("solrDocumentCustomSimilarityManager")
@Transactional(rollbackFor = Exception.class)
public class SolrDocumentCustomSimilarityManager implements SolrDocumentSimilarityManager {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource(name = "solrDocumentJDBCDao")
	private SolrDocumentJDBCDao solrDocumentJDBCDao;
		
	@Override
	public void addAllDocumentBeans(String type, String[] dataTypes ) throws ServiceException{
		addAllDocumentBeans(type,dataTypes,"");
	}
	
	@Override
	public void addAllDocumentBeans(String type, String[] dataTypes , String sysType)
			throws ServiceException {

		if(dataTypes==null || dataTypes.length == 0){
			throw new ServiceException("参数dataTypes数组不能为空或者数组长度为0");
		}
		
		for(int i=0;i<dataTypes.length;i++){
			addAllDocumentSingleDimensionBeans(TypeConstants.solrCoresTypeMap.get(dataTypes[i]), type, dataTypes[i], sysType);
		}
		
	}
	
	
	private void addAllDocumentSingleDimensionBeans(String solrCore, String type, String dataType, String sysType) throws ServiceException{
		
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
        try {
			testSolrUtils.InitSolrClientLocal(solrCore);
		} catch (Exception e) {
			throw new ServiceException("solr客户端打开失败solrCore="+solrCore+e.getMessage());
		}
        List<Map<String, Object>> itemList = null;
		List<Map<String, Object>> objList = null;
		List<SolrItem> solrItemList = new ArrayList<SolrItem>();
		SolrItem solrItem = null;
	    int i = 1; 
	    int index = 1;
	    
	    try {
			objList = solrDocumentJDBCDao.getAllSingleAimensionKeyCode(dataType,type);
			for(Map<String, Object> obj : objList){
				
				String idStr = ObjectUtils.toString(obj.get("id"));
				itemList = solrDocumentJDBCDao.getItemContentById(idStr);
				solrItem = new SolrItem();
				for(Map<String, Object> item : itemList){
					
					String content = testStringUtils.replaceRegex(ObjectUtils.toString(item.get("content")));
					solrItem.setId(idStr);
					solrItem.setContent(content);
					solrItem.setSystype(sysType);
					solrItem.setType(type);
				}
				
				
				solrItemList.add(solrItem);
				if(index>=100  || i==objList.size()){
			    	testSolrUtils.addDocumentSolrItemBeans(solrItemList);
			    	solrItemList.clear();
			    	index = 1;
				}
				
				i++;
				index++;
				logger.debug("solr库（"+solrCore+"）：第"+i+"个项目导进了solr");
			}
	    } catch (Exception e) {
			throw new ServiceException("solr添加所有索引失败solrCore="+solrCore+e.getMessage());
		} finally {
			try {
				testSolrUtils.CloseSolrClientLocal();
			} catch (Exception e) {
				throw new ServiceException("solr客户端关闭失败"+e.getMessage());
			}
		}
		
	}
	
	@Override
	public void addDocumentBeans(String keyCode, String type, String[] dataTypes) throws ServiceException{
		addDocumentBeans(keyCode,type,dataTypes,"");
	}
	
	@Override
	public void addDocumentBeans(String keyCode, String type, String[] dataTypes , String sysType) throws ServiceException {
		
		if(dataTypes==null || dataTypes.length == 0){
			throw new ServiceException("参数dataTypes数组不能为空或者数组长度为0");
		}
		
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
		SolrItem solrItem = null;
		
		for(int i=0;i<dataTypes.length;i++){
			
			String solrCore = TypeConstants.solrCoresTypeMap.get(dataTypes[i]);
			if(testStringUtils.isNullOrBlank(solrCore)){
				throw new ServiceException("solr没有该库"+solrCore);
			}
			
			try {
				testSolrUtils.InitSolrClientLocal(solrCore);
			} catch (Exception e) {
				throw new ServiceException("solr客户端打开失败solrCore="+solrCore+e.getMessage());
			}
			
			try {
				List<Map<String, Object>> itemList = solrDocumentJDBCDao.getItemInfoByKeyCodeAndDataType(keyCode,dataTypes[i],type);
				Map<String, Object> item = itemList.get(0);
				
				solrItem = new SolrItem();
					
				String content = testStringUtils.replaceRegex(ObjectUtils.toString(item.get("content")));
				String idStr  =ObjectUtils.toString(item.get("id"));
				solrItem.setId(idStr);
				solrItem.setContent(content);
				solrItem.setSystype(sysType);
				solrItem.setType(type);
				
				testSolrUtils.addDocumentBean(solrItem);
			
			} catch (Exception e) {
				throw new ServiceException("solr添加索引ID="+keyCode+"AND type="+type+"AND dataType="+solrCore+"失败"+e.getMessage());
			} finally {
				try {
					testSolrUtils.CloseSolrClientLocal();
				} catch (Exception e) {
					throw new ServiceException("solr客户端关闭失败"+e.getMessage());
				}
			}
			
		}
		
	}
	
	@Value("${solr.sys.type}")
	private String systype ;
	
	@Override
	public void addDocumentBeans(CompareListInfo compareListInfo) throws ServiceException{
		addDocumentBeans(compareListInfo,systype);
	}
	
	@Override
	public void addDocumentBeans(CompareListInfo compareListInfo , String sysType) throws ServiceException{
		
		if(compareListInfo==null){
			throw new ServiceException("参数compareListInfo不能为空");
		}
		
		String solrCore = TypeConstants.solrCoresTypeMap.get(ObjectUtils.toString(compareListInfo.getDataType()));
		if(solrCore==null){
			throw new ServiceException("solr没有该库"+compareListInfo.getDataType());
		}
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
		
		try {
			testSolrUtils.InitSolrClientLocal(solrCore);
			//先删除
			testSolrUtils.delDocumentBean(ObjectUtils.toString(compareListInfo.getKeyCode()),ObjectUtils.toString(compareListInfo.getType()));
			
			SolrItem solrItem = new SolrItem();;
			solrItem.setId(ObjectUtils.toString(compareListInfo.getId()));
			solrItem.setContent(compareListInfo.getContent());
			solrItem.setType(ObjectUtils.toString(compareListInfo.getType()));
			solrItem.setSystype(sysType);
			
			testSolrUtils.addDocumentBean(solrItem);
			
		} catch (Exception e) {
			throw new ServiceException("solr添加索引ID="+compareListInfo.getId()+"AND type="+compareListInfo.getType()+"AND dataType="+solrCore+"失败"+e.getMessage());
		} finally {
			try {
				testSolrUtils.CloseSolrClientLocal();
			} catch (Exception e) {
				throw new ServiceException("solr客户端关闭失败"+e.getMessage());
			}
		}
		
	}
	
	@Override
	public void delDocumentAll(String solrCore) throws ServiceException {
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
		try {
			testSolrUtils.InitSolrClientLocal(solrCore);
			testSolrUtils.delDocumentAll();
			testSolrUtils.CloseSolrClientLocal();
		} catch (Exception e) {
			throw new ServiceException("solr删除所有索引失败"+e.getMessage());
		}
		
	}
	
	@Override
	public void delDocumentAll(String[] dataTypes) throws ServiceException {
		
		if(dataTypes==null || dataTypes.length == 0){
			throw new ServiceException("参数dataTypes数组不能为空或者数组长度为0");
		}
        for(int i=0;i<dataTypes.length;i++){
        	delDocumentAll(TypeConstants.solrCoresTypeMap.get(dataTypes[i]));
		}
	}
	
	@Override
	public void delDocumentBean(String[] dataTypes , String keyCode , String type) throws ServiceException{
		
		if(dataTypes==null || dataTypes.length == 0){
			throw new ServiceException("参数dataTypes数组不能为空或者数组长度为0");
		}
		
		
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
		
		for(int i=0;i<dataTypes.length;i++){
			
			String solrCore = TypeConstants.solrCoresTypeMap.get(dataTypes[i]);
			if(testStringUtils.isNullOrBlank(solrCore)){
				throw new ServiceException("solr没有该库"+solrCore);
			}
			
			try {
				testSolrUtils.InitSolrClientLocal(TypeConstants.solrCoresTypeMap.get(dataTypes[i]));
				testSolrUtils.delDocumentBean(keyCode,type);
			} catch (Exception e) {
				throw new ServiceException("solr删除索引ID="+keyCode+"AND type="+type+"AND dataType="+solrCore+"失败"+e.getMessage());
			} finally {
				try {
					testSolrUtils.CloseSolrClientLocal();
				} catch (Exception e) {
					throw new ServiceException("solr客户端关闭失败"+e.getMessage());
				}
			}
			
			
		}
	}
	
	@Override
	public void delDocumentBean(CompareListInfo compareListInfo) throws ServiceException{
		
		
		if(compareListInfo==null){
			throw new ServiceException("参数compareListInfo不能为空");
		}
		
		String solrCore = TypeConstants.solrCoresTypeMap.get(ObjectUtils.toString(compareListInfo.getDataType()));
		if(testStringUtils.isNullOrBlank(solrCore)){
			throw new ServiceException("solr没有该库"+compareListInfo.getDataType());
		}
		
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
		
		try {
			testSolrUtils.InitSolrClientLocal(solrCore);
			testSolrUtils.delDocumentBean(ObjectUtils.toString(compareListInfo.getId()),ObjectUtils.toString(compareListInfo.getType()));
		} catch (Exception e) {
			throw new ServiceException("solr删除索引ID="+compareListInfo.getId()+"AND type="+compareListInfo.getType()+"AND dataType="+solrCore+"失败"+e.getMessage());
		} finally {
			try {
				testSolrUtils.CloseSolrClientLocal();
			} catch (Exception e) {
				throw new ServiceException("solr客户端关闭失败"+e.getMessage());
			}
		}
		
		
	}
	
	@Override
	public void optimize(String solrCore) throws ServiceException {
		testSolrUtils testSolrUtils = testSolrUtils.gettestSolrUtilsInstance();
		try {
			testSolrUtils.InitSolrClientLocal(solrCore);
			testSolrUtils.optimize();
			testSolrUtils.CloseSolrClientLocal();
		} catch (Exception e) {
			throw new ServiceException("solr索引优化出错"+e.getMessage());
		}
		
	}
	
	@Override
	public void optimize(String[] dataTypes) throws ServiceException {
		
		if(dataTypes==null || dataTypes.length == 0){
			throw new ServiceException("参数dataTypes数组不能为空或者数组长度为0");
		}
        for(int i=0;i<dataTypes.length;i++){
        	optimize(TypeConstants.solrCoresTypeMap.get(dataTypes[i]));
		}
	}
	
}
