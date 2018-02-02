package com.iris.egrant.code.compare.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.iris.egrant.code.solr.query.SolrQuerySimilarityCustomManager;
import com.iris.egrant.core.cp.model.CompareListInfo;
import com.iris.egrant.core.exception.ServiceException;
import com.iris.egrant.core.utils.CollectionUtils;

/**
 * 疑似项目比对：处理接口
 * @author wk 2014-5
 *
 */
public interface CompareTemplateService {

	/**
	 * 实现抽取动作 通过prpCode获取xml数据进行解析，将相应内容填充至compare_source.content字段中
	 * 
	 * @param prpCode
	 * @return 抽取出来的比对数据
	 **/
	String extractCompareSource(long prpCode);

	/**
	 * 实现比对动作 从业务xml为起点 将内容填充至compare_source.content字段中
	 * 
	 * @param 源数据和比较数据
	 * @return 相似度
	 **/
	Double compareContent(String sourceContent, String targetContent);

	/**
	 * 实现比对动作 从业务xml为起点 将内容填充至compare_source.content字段中
	 * 
	 * @param 需要渲染的源与目标数据
	 * @return 疑似结果渲染效果
	 **/
	Map<String, String> renderDiffer(String sourceContent, String targetContent);
	
	/**
	 *  compareList初始化compare_result记录之前 数据过滤 （Solr API/Java实现）
	 * @param compareList
	 * @return
	 */
	Set<Long> doDataFilter(CompareListInfo compareList) throws ServiceException;
}

/**
 * 数据过滤类 solr实现
 * @author cg
 *
 */
abstract class  DataFilterSolrSupport implements CompareTemplateService {
	
	@Autowired
	private CompareListInfoService compareListInfoService ;
	
	@Autowired
	private SolrQuerySimilarityCustomManager solrQuerySimilarityCustomManager ;
	
	public Set<Long> doDataFilter(CompareListInfo compareList) throws ServiceException {
		List<Long> keyCodeList = new ArrayList<Long>() ;
		List<Map<String,Object>> listMap = solrQuerySimilarityCustomManager.query(compareList) ;
		if (!CollectionUtils.isEmpty(listMap)){
			for (Map<String, Object> map : listMap) {
				if (!CollectionUtils.isEmpty(map)){
					keyCodeList.add(Long.valueOf(ObjectUtils.toString((map.get("keyCode"))))) ;
				}
			}
		}
		Set<Long> returnSet = new HashSet<Long>() ;
		if (!CollectionUtils.isEmpty(keyCodeList)){
			// 只取相似的项目的前100个
			List<Long> tempList =  keyCodeList.size() > 100 ? keyCodeList.subList(0, 100) : keyCodeList ;
			  returnSet  = compareListInfoService.getKeyCodeByIds(tempList ) ;
		}
		return returnSet;
	}
}

/**
 *   数据过滤类 java实现
 * @author cg
 *
 */
abstract class DataFilterJavaSupport implements CompareTemplateService{
	
	private static Double SIMILARITY_BASE_LINE  = 0.00d;  // 相关度 基准线
	
	@Autowired
	private CompareListInfoService compareListInfoService ;
	
	public Set<Long> doDataFilter(CompareListInfo compareList) throws ServiceException {
		Set<Long> set = new HashSet<Long>() ;
	//	System.out.println( "===============list start===============" + new Date());
		List<CompareListInfo> list =  compareListInfoService.getCompareListInfoByDataType(compareList.getDataType()) ;  // 已做缓存
	//	System.out.println( "===============list end===============" + new Date());
	 	Date startTime = new Date()  ;
	 // 	System.out.println( "===============doDataFilter start===============" + compareList.getDataType() + list.size()    );
		for ( int i = 0 ; i< list.size()  ; i++ ) {
			CompareListInfo otherCompareList = list.get(i) ;
		// 	System.out.println(otherCompareList.getDataType() + "===============" + i + "===============");
			if (compareList.getKeyCode().equals( otherCompareList.getKeyCode())){  // 同一项目
				continue ;
			}else {
				// Double d =	 this.compareContent(compareList.getContent(), otherCompareList.getContent()) ;   // 调用原相似度  比较逻辑
				Double d ;
				// 较小的Id作为第一个参数   用于缓存key
			 if (otherCompareList.getId() < compareList.getId() ){
				 d = getSimilarity(otherCompareList , compareList); 
			 }else{
				 d = getSimilarity(compareList ,otherCompareList);
			  }
			 if (d >  SIMILARITY_BASE_LINE ){
				 set.add( otherCompareList.getKeyCode()) ;
			  }
			}
		}
	  //	Date endTime = new Date() ;
//	 	System.out.println( "===============doDataFilter end===============" + (endTime.getTime() - startTime.getTime())/1000 );
		return set;
	}
	
	@Cacheable(value = "compareListInfoSimilarity")
	public Double getSimilarity(CompareListInfo c1 ,CompareListInfo c2){
		return	compareContent(c1.getContent(), c2.getContent()) ;   // 调用原相似度  比较逻辑
	}
}
