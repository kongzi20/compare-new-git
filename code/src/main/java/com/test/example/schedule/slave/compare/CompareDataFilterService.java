package com.test.example.schedule.slave.compare;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.compare.dao.CompareHibernateDao;
import com.test.example.code.compare.model.PrpCompareListVO;
import com.test.example.code.compare.service.CompareListInfoService;
import com.test.example.code.compare.service.CompareResultService;
import com.test.example.code.compare.service.CompareTemplateService;
import com.test.example.code.solr.constant.TypeConstants;
import com.test.example.code.solr.document.SolrDocumentSimilarityManager;
import com.test.example.core.cp.model.CompareListInfo;
import com.test.example.core.exception.ServiceException;
import com.test.example.core.utils.Assert;
import com.test.example.core.utils.CollectionUtils;
  
/**
 * 比对数据solr过滤
 * @author cg
 */
@Component("compareDataFilterService")
public class CompareDataFilterService extends CompareServiceListener<PrpCompareListVO> implements InitializingBean {
	
	private Map<String,CompareTemplateService>  tmpMap ;
	
	 /**
	  * 分词索引 
	  */
	@Resource(name="solrDocumentCustomSimilarityManager")
	private SolrDocumentSimilarityManager solrDocumenManager ;
	
	@Autowired
	private CompareHibernateDao<PrpCompareListVO ,Long> cpHibernateDao ;
	
	@Autowired
	private CompareResultService compareResultService;
	
	@Autowired
	private CompareListInfoService compareListInfoService ;
	
	 
	@Override
  @Transactional(rollbackFor = Exception.class,readOnly=true )
	protected List<PrpCompareListVO> fetchDb(Long startKeyCode, Long endKeyCode) {
		return cpHibernateDao.getPrpCompareListVOList( startKeyCode, endKeyCode) ;
	}

	@Override
	public void doService(List<PrpCompareListVO> list ) { 
		for (PrpCompareListVO prpCompareListVO : list) {
	 	 Set<Long> keyCodeSet = new HashSet<Long>() ; // 该项目的相关项目集合
	 	 // 遍历
		 for (CompareListInfo compareList : prpCompareListVO.getCompareLists()) {
				 String status =  compareList.getStatus().toString() ;
				 String dataType =  compareList.getDataType() ==  null ? null : compareList.getDataType().toString() ;
				// 分词索引
				 if ("0".equals(status) // solr未分词索引
							&& (TypeConstants.content.equals(dataType) // solr只处理：项目内容、可行性报告、项目名称
									|| TypeConstants.executableReport.equals(dataType) 
									|| TypeConstants.title.equals(dataType))) {
						try {
					 // solr分词索引
							solrDocumenManager.addDocumentBeans(compareList);
						} catch (ServiceException e) {
							//TODO solr异常了 如何处理？
						  	e.printStackTrace();
							// updateCompareListStatus(compareList.getId(), "2");
							// continue ;
							throw new  RuntimeException("solr分词索引异常！") ;
						}
				  }
				 // 得到相关性的keyCode集
				  Set<Long> tempKeyCodeSet =  null ;
			 try {
				  // 单维度获取相关性（Solr API/Java实现）
				 CompareTemplateService cts =  tmpMap.get(dataType) ;
				 Assert.notNull( cts,"CompareTemplateService实现类对象不能为空！");
				 tempKeyCodeSet =  cts.doDataFilter(compareList);
				} catch (Exception e) {
						e.printStackTrace();
						compareListInfoService.updateCompareListStatusByKeyCode(prpCompareListVO.getKeyCode() , "2") ;  // keyCode对应的compare_list批量更新为2
						return ;
				}
				 // addAll to keyCodeSet
				 if (tempKeyCodeSet != null && tempKeyCodeSet.size() > 0){
					 keyCodeSet.addAll(tempKeyCodeSet) ;
				 }
		 }
		 LOGGER.info("过滤结果  keyCode：{},相关项目数量：{},keyCodeSet:{}" ,prpCompareListVO.getKeyCode() ,keyCodeSet.size() ,  keyCodeSet);
		 //遍历完该项目所有的CompareList，开始初始化与其相关的项目的compare_result
		 if (CollectionUtils.isEmpty(keyCodeSet)){
			 compareListInfoService.updateCompareListStatusByKeyCode(prpCompareListVO.getKeyCode() , "1") ;  // keyCode对应的compare_list批量更新为1
		 }else{
			//从compare_list初始化compare_result
			 compareListInfoService.initCompareResultAndUpdateCompareList(prpCompareListVO, keyCodeSet);
		 }
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		tmpMap = compareResultService.getCompareTemplateAll() ;
	}
}