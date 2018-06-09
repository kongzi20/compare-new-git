package com.test.example.core.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 *  xml解析类  
 *     比对专用 用于时效性不高的场景
 *  add by cg 
 *  @see 参考自 com.test.example.core.utils.XMLHelper
 * 
 */
@Component
public class XMLHelperCacheable {

	protected static final Logger LOGGER = LoggerFactory.getLogger(XMLHelperCacheable.class);
/*	
	private static final String CACHE_HASH_KEY =  "CACHE_NODE_VALUE" ;  
	private static final Long CACHE_EXPIRE_TIMEOUT =  60*60*1000*24L ; // 24小时
	
	@Resource(name="cacheRedisTemplateNoTx")
	private RedisTemplate<String,String>  redisTemplateNoTx ;
	 */
	@Cacheable(value="documentParsed",key="'com.test.example.core.utils.XMLHelperCacheable.parseDocument(String)'+#xmlData" )
	public Document parseDocument(String xmlData) {
		return XMLHelper.parseDocument(xmlData) ; 
	}
	
	@Cacheable(value="w3cDocumentParsed",key="'com.test.example.core.utils.XMLHelperCacheable.parseDoc(Document)'+#doc" )
	public Document parseDoc(org.w3c.dom.Document doc) { 
		return XMLHelper.parseDoc(doc) ; 
	}
	
 //	private Map<String, String>  cacheMap = new ConcurrentHashMap<String, String>() ;
	
  @Cacheable(value="rooDocument-node-subNodeName-value")
	public String getNodeValue(String  rooDocument , Node node ,String subNodeName){
		 /* String keyStr =  rooDocument + node.getPath() +  subNodeName ;
		String valueStr = cacheMap.get(keyStr);
		if ( null == valueStr ){
			Node subNode  = node.selectSingleNode(subNodeName) ;
			valueStr = subNode == null ? null : subNode.getText();
			if (valueStr != null){
				cacheMap.put(keyStr, valueStr);
			}
		}
		return valueStr; */
		
		Node subNode  = node.selectSingleNode(subNodeName) ;
		return  subNode == null ? null : subNode.getText()  ;
		 
		
	/*	String keyStr =  rooDocument + node.getPath() +  subNodeName ;
		String key =  DigestUtils.md5DigestAsHex(keyStr.getBytes());
		BoundHashOperations<String, String, String> ops = redisTemplateNoTx.boundHashOps(CACHE_HASH_KEY);
		String returnVal  ;
		if ( ops == null || StringUtils.isBlank(returnVal = ops.get(key)) ){
			Node subNode  = node.selectSingleNode(subNodeName) ;
			returnVal = subNode == null ? null : subNode.getText()  ;
			if (returnVal != null){
				ops.put(key, returnVal);
				ops.expireAt( new Date(System.currentTimeMillis() +  CACHE_EXPIRE_TIMEOUT ) ) ;
			}
		} 
		return  returnVal ; */
	}
}