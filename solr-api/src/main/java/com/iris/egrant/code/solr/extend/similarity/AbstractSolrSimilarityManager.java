package com.iris.egrant.code.solr.extend.similarity;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iris.egrant.code.solr.util.IrisSolrUtils;
import com.iris.egrant.core.exception.ServiceException;
import com.iris.egrant.core.utils.IrisStringUtils;
 

public abstract class AbstractSolrSimilarityManager implements SolrSimilarityManager {

	private String hlLabelPre = "<font color='red'>";
	private String hlLabelPost = "</font>";
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public List<Map<String, Object>> query(String id ,String content , String type , String systype) throws ServiceException {
		
		String fieldName = "content";
		IrisSolrUtils irisSolrUtils = IrisSolrUtils.getIrisSolrUtilsInstance();    
		SolrQuery query = new SolrQuery();  
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		//查询参数
		StringBuilder params = new StringBuilder(fieldName+":" + content);  
		params.append(" AND type:" + type);  
		if(!IrisStringUtils.isNullOrBlank(systype))
		  params.append(" AND systype:" + systype);  
        query.setQuery(params.toString()); 
        //排除自身
        query.setParam("fq","-id:"+id);
        
        //设置分页参数  
        query.setStart(0);  
        //获取所有数据（对比所有数据）
        query.setRows(Integer.MAX_VALUE);
        
        //相似度
        query.set("defType","edismax");  
        query.setParam("mm",getEdismaxMM());  
        
        
      //设置高亮开始----------------------------
        if(isHl()){
        	query.setHighlight(true);  
            //设置高亮的字段  
            query.addHighlightField("fieldName"); 
            query.setHighlightRequireFieldMatch(true);
            //设置高亮的样式  
            query.setHighlightSimplePre(hlLabelPre);  
            query.setHighlightSimplePost(hlLabelPost); 
            query.setHighlightFragsize(0);  
            //设置高亮结束----------------------------
        }
        
        logger.debug("----------------------"+fieldName+"搜索开始---------------------------");
                
        //获取查询结果
        QueryResponse response;
		try {
			response = irisSolrUtils.getSolrClientLocal().query(query,METHOD.POST);
		} catch (SolrServerException e) {
			throw new ServiceException("solr[Server]查询失败id="+id+"AND type="+type+e.getMessage());
		} catch (IOException e) {
			throw new ServiceException("solr[io]查询失败id="+id+"AND type="+type+e.getMessage());
		}  
                
        //查询得到文档的集合  
        SolrDocumentList solrDocumentList = response.getResults();  
        logger.debug("查询结果的总数量：" + solrDocumentList.getNumFound());
        Map<String,Object> itemMap = new HashMap<String,Object>(); 
        if(isHl()){
        	
        	//获取高亮
            Map<String,Map<String,List<String>>> map = response.getHighlighting(); 
            for (SolrDocument doc : solrDocumentList) {
            	
            	itemMap = new HashMap<String,Object>();
            	Map<String,List<String>> contenthlMap = map.get(doc.get("id"));
            	List<String> contenthlListStr = contenthlMap.get(fieldName);
            	//排除高亮占比低的结果
            	if(contenthlListStr!=null && contenthlListStr.size()>0){
            		String contenthlStr = contenthlListStr.get(0);
            		float hlWordsNumScale = CalculationHlWordsScale(contenthlStr);
        	        if(hlWordsNumScale < getResultIndexRate() ){
        	        	continue;
        	        }
            	}
            	
            	resultList.add(itemMap);
            	itemMap.put("keyCode", doc.get("id"));
            }
        	
        }else{
        	
        	for (SolrDocument doc : solrDocumentList) {
            	itemMap = new HashMap<String,Object>();
            	resultList.add(itemMap);
            	itemMap.put("keyCode", doc.get("id"));
            }
        	
        }
        
        logger.debug("----------------------"+fieldName+"搜索完毕---------------------------");
        
        return resultList;
	}

	/**
	 * 获取搜索出来的结果字段的高亮占比值
	 * @param contenthlStr
	 * @return
	 */
	private Float CalculationHlWordsScale(String contenthlStr) {


		String pattern = hlLabelPre+ "(.*?)"+hlLabelPost;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(contenthlStr);
        int hlWordsNum = 0;
        while(m.find()){
        	String groupStr = m.group().replace(hlLabelPre, "").replace(hlLabelPost, "");
        	//System.out.println(groupStr+"：文字数："+groupStr.length());
        	hlWordsNum = hlWordsNum + groupStr.length();
        }
        int totalWordsNum = 0;
        totalWordsNum = contenthlStr.replace(hlLabelPre, "").replace(hlLabelPost, "").length();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String hlWordsNumScaleStr = numberFormat.format(((float)hlWordsNum / (float)totalWordsNum) * 100);
        float hlWordsNumScale = Float.parseFloat(hlWordsNumScaleStr);
        String flag = "";
        logger.debug("高亮的占比为:" + hlWordsNumScaleStr + "%" + flag);
		
		return hlWordsNumScale;
		
	}
	
	@Override
    public String getEdismaxMM(){
    	return "68%";
    }
	
	@Override
	public float getResultIndexRate(){
		return 30f;
	}
	
	/*@Override
	public int getStartNo(){
		return 0;
	}
	
	@Override
	public int getEndNo(String dataType, String type){
		return 10;
	}*/

}
