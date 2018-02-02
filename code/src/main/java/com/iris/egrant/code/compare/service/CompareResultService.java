package com.iris.egrant.code.compare.service;

import java.util.List;
import java.util.Map;

import com.iris.egrant.code.compare.model.CompareResultVO;
import com.iris.egrant.code.compare.model.CompareTemplateSetting;
 
public interface CompareResultService {
	/**
	 * 比对数据内容(单线程)
	 */
	public void compared2Data();

	/**
	 * 比对数据内容(多线程)
	 * 
	 * @param startId
	 *            起点Id
	 * @param endId
	 *            结束Id
	 */
	public void compared2DateForMultithread(long startId, long endId);

	/**
	 * 获取单线程比对内容
	 * 
	 * @param startId
	 * @param endId
	 * @param size
	 * @return
	 */
	public List<Map<String, Object>> getCompareTaskForMultithread(long startId, long endId, int size);
	
	/**
	 *     批量比对
	 * @param list
	 * @param templateMap
	 */
	void calculateSimilaryAndUpdateCompareResultBatch(List<CompareResultVO> list , Map<String, CompareTemplateService> templateMap );

	/**
	 * 比较
	 * 
	 * @param result_id
	 */
	public void calculateSimilaryAndUpdateCompareResult(Long result_id);
	
	
	/**
	 * 比较
	 * 
	 * @param result_id
	 */
	public void calculateSimilaryAndUpdateCompareResult(Long result_id,String sourceContent,String targetContent,CompareTemplateService compareResultService) ;
	

	/**
	 * 获取id列表
	 * 
	 * @return
	 */
	public Long getResultIds();

	/**
	 * 初始化比对，将待指定长度的待比对记录相关信息放入临时表中
	 * 
	 * @param size
	 */
	public Long initCompareData(int size);

	/**
	 * 比对结束，清空临时表
	 */
	public void emptyCompareTempTbl();

	/**
	 * wk add 2014-5-14 从prp_compare_result中获取所有的比较结果
	 * 
	 * @param result
	 */
	public Map<String, Object> getAllCompareResult(final Long prpCode1, final Long prpCode2);

	/**
	 * wk add 2014-5-14 通过dataType获取具体实现类beanName
	 * 
	 * @param result
	 */
	public CompareTemplateSetting getCompareBeanNameByDataType(final Integer dataType);

	/**
	 * 将疑似项目比对结果同步到proposal_extend.compare_prp_similary_cnt
	 * 
	 * @throws Exception
	 */
	public void ComparePrpCntSyncTask() throws Exception;

	public List<Map<String, Object>> getCompareTaskList();
	
	/**
	 * 获取所有比较SPRING模版
	 * */
	public Map<String,CompareTemplateService>  getCompareTemplateAll();
	
	
	public void ComparePrpCntSyncTask(final Long prpCode) throws Exception;
	
	public void callUpdatePrpExtendCnt(final String type, final String prpCode) ;

}