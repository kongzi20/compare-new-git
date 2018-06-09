package com.test.example.code.compare.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.compare.dao.CompareDao;
import com.test.example.code.compare.dao.CompareListInfoDao;
import com.test.example.code.compare.dao.CompareResultDao;
import com.test.example.code.compare.model.CompareResult;
import com.test.example.code.compare.model.CompareResultVO;
import com.test.example.code.compare.model.CompareTemplateSetting;
import com.test.example.core.sf.ServiceFactory;
import com.test.example.core.utils.DateFormator;
import com.test.example.core.utils.DateUtils;

/**
 * 相似度结果服务类
 * 
 * @author Administrator
 * 
 */
@Service("compareResultService")
public class CompareResultServiceImpl implements CompareResultService {

	@Autowired
	private CompareListInfoDao compareListInfoDao;

	@Autowired
	private CompareResultDao compareResultDao;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "serviceFactory")
	private ServiceFactory serviceFactory;

	@Autowired
	private CompareDao compareDao;

	private boolean isRun = false;

	@Override
	public void compared2Data() {
		compared2DateForMultithread(0, Long.MAX_VALUE);
	}

	@Override
	public void compared2DateForMultithread(long startId, long endId) {
		while (true) {
			List<Map<String, Object>> tempDatas = compareResultDao.getCompareTaskForMultithread(startId, endId, CompareResult.FETCH_SIZE);
			// 无记录，则退出
			if (tempDatas.size() <= 0) {
				break;
			}
			for (Map<String, Object> temp : tempDatas) {
				// 比较内容相似度，并维护比对对象状态
				calculateSimilaryAndUpdateCompareResult(Long.valueOf(temp.get("RESULT_ID").toString()));
				startId = Long.valueOf(temp.get("RN").toString());
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public List<Map<String, Object>> getCompareTaskForMultithread(long startId, long endId, int size) {
		return compareResultDao.getCompareTaskForMultithread(startId, endId, CompareResult.FETCH_SIZE);
	}

	/**
	 * 比较内容相似度，结果更新到对象中
	 * 
	 * @param result
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void calculateSimilaryAndUpdateCompareResult(Long result_id) {
		CompareResult result = compareResultDao.get(result_id);
		String sourceContent = compareListInfoDao.get(result.getSourceID()).getContent(); // 源项目内容
		String targetContent = compareListInfoDao.get(result.getTargetID()).getContent(); // 目标项目内容

		Long startTime = System.currentTimeMillis(); // 开始时间
		double similarity = 0;
		try {
			// 1.通过dataType获取对应实现类
			CompareTemplateSetting cts = compareDao.getCompareBeanNameByDataType(result.getDataType());
			CompareTemplateService ctService = this.getServiceFactory().getService(cts.getTemplateBeanName(), CompareTemplateService.class);

			// 2.调用实现类比较方法获取相似度
			similarity = ctService.compareContent(sourceContent, targetContent);
			// 结束时间
			Long endTime = System.currentTimeMillis();
			result.setRunTime(endTime - startTime); // 耗时

			// 3.维护比对结果对象状态
			if (Double.isNaN(similarity)) {
				similarity = 0.0f;
			} else {
				similarity = (double) Math.round(similarity * 10000) / 10000; // 保留小数点后4位
			}

			result.setCompareResult(similarity); // 相似度结果
			 result.setStatus(CompareResult.STATUS_PROCESSED); // 已处理状态
			result.setCompleteDate(new Date()); // 完成时间
			if (similarity > 0 || result.getDataType() == 6) {
				callExecByDataType(result.getSourceID(), result.getTargetID(), result.getType(), result.getDataType(), similarity);
			}
		} catch (Exception e) {
			result.setStatus(CompareResult.STATUS_ERROR); // 错误状态
			result.setErrMsg(e.getMessage()); // 错误信息
		} finally {
			// 4.保存
			compareResultDao.save(result);
		}
	}

	/**
	 * 
	 */
	@Transactional(rollbackFor = Exception.class/* , propagation = Propagation.REQUIRES_NEW*/)
	public void calculateSimilaryAndUpdateCompareResultBatch(List<CompareResultVO> list , Map<String, CompareTemplateService> templateMap ){
		for (CompareResultVO compareResultVO : list) {
			String itemId = ObjectUtils.toString(compareResultVO.getResultId());
			try {
				String sourceContent = compareResultVO.getSourceContent()  == null ? "" :  compareResultVO.getSourceContent() ;
				String targetContent = compareResultVO.getTargetContent()  == null ? "" :   compareResultVO.getTargetContent() ;
				String dataType = ObjectUtils.toString(compareResultVO.getDataType());
				// 比较内容相似度，并维护比对对象状态
				calculateSimilaryAndUpdateCompareResult(Long.valueOf(itemId), sourceContent, targetContent, templateMap.get(dataType));
			} catch (Exception e) { 
				// LOGGER.error("执行项目内容比对任务失败", e);
			}
		}
	}
	
	/**
	 * 比较内容相似度，结果更新到对象中
	 * 
	 * @param result
	 */
	 @Transactional(rollbackFor = Exception.class/*, propagation = Propagation.REQUIRES_NEW*/)
	public void calculateSimilaryAndUpdateCompareResult(Long result_id, String sourceContent, String targetContent, CompareTemplateService ctService) {
		CompareResult result = compareResultDao.get(result_id);
		// String sourceContent = compareListInfoDao.get(result.getSourceID()).getContent(); // 源项目内容
		// String targetContent = compareListInfoDao.get(result.getTargetID()).getContent(); // 目标项目内容

		if (ctService == null) {
			 // System.out.println("result_id = [" + result_id + "] CompareTemplateService is null!!!");
			return;
		}

		if (result.getStatus() == 1) { // 防守
		 // 	System.out.println("result_id = [" + result_id + "] 已经比较完成，不再进行比较!!!");
			return;
		}

		Long startTime = System.currentTimeMillis(); // 开始时间
		double similarity = 0;
		try {
			// 1.通过dataType获取对应实现类
			// CompareTemplateSetting cts = compareDao.getCompareBeanNameByDataType(result.getDataType());
			// CompareTemplateService ctService = this.getServiceFactory().getService(cts.getTemplateBeanName(),
			// CompareTemplateService.class);

			// 2.调用实现类比较方法获取相似度
			similarity = ctService.compareContent(sourceContent, targetContent);
			// 结束时间
			Long endTime = System.currentTimeMillis();
			result.setRunTime(endTime - startTime); // 耗时

			// 3.维护比对结果对象状态
			if (Double.isNaN(similarity)) {
				similarity = 0.0f;
			} else {
				similarity = (double) Math.round(similarity * 10000) / 10000; // 保留小数点后4位
			}

			result.setCompareResult(similarity); // 相似度结果
			result.setStatus(CompareResult.STATUS_PROCESSED); // 已处理状态
			result.setCompleteDate(new Date()); // 完成时间
			if (similarity > 0 || result.getDataType() == 6) {
				callExecByDataType(result.getSourceID(), result.getTargetID(), result.getType(), result.getDataType(), similarity);
			}
		} catch (Exception e) {
			result.setStatus(CompareResult.STATUS_ERROR); // 错误状态
			result.setErrMsg(e.getMessage()); // 错误信息
		} finally {
			// 4.保存
			compareResultDao.save(result);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private void callExec(final long sourceId, final long targetId, final int type, final double similarity) {
		jdbcTemplate.execute("{call proc_compare(?,?,?,?)}", new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(java.sql.CallableStatement stat) throws SQLException, DataAccessException {
				stat.setLong(1, sourceId);
				stat.setLong(2, targetId);
				stat.setInt(3, type);
				stat.setDouble(4, similarity);
				stat.execute();
				return null;
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void callExecByDataType(final long sourceId, final long targetId, final int type, final int datatype, final double similarity) {
		jdbcTemplate.execute("{call proc_compare_by_data_type(?,?,?,?,?)}", new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(java.sql.CallableStatement stat) throws SQLException, DataAccessException {
				stat.setLong(1, sourceId);
				stat.setLong(2, targetId);
				stat.setInt(3, type);
				stat.setInt(4, datatype);
				stat.setDouble(5, similarity);
				stat.execute();
				return null;
			}
		});
	}

	@Override
	public Long getResultIds() {
		return compareResultDao.getResultIds();
	}

	@Override
	public Long initCompareData(int size) {
		return compareResultDao.initCompareData(size);
	}

	@Override
	public void emptyCompareTempTbl() {
		compareResultDao.emptyCompareTempTbl();
	}

	@Override
	public Map<String, Object> getAllCompareResult(Long prpCode1, Long prpCode2) {
		return compareResultDao.getAllCompareResult(prpCode1, prpCode2);
	}

	@Override
	public CompareTemplateSetting getCompareBeanNameByDataType(Integer dataType) {
		return compareDao.getCompareBeanNameByDataType(dataType);
	}

	@Override
	public void ComparePrpCntSyncTask() throws Exception {
		if (isRun) {
			return;
		}
		try {
			isRun = true;
			List<Map<String, Object>> prpCodes = compareResultDao.findComparingPrp();
			if (prpCodes.size() > 1000) {
				prpCodes = prpCodes.subList(0, 1000);
			}

			for (Map<String, Object> info : prpCodes) {
				String type = ObjectUtils.toString(info.get("TYPE"));
				String prpCode = ObjectUtils.toString(info.get("PRP_CODE"));
				callUpdatePrpExtendCnt(type, prpCode);

				/*System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS) + "================同步比对结果：type=" + type + "，prp_code=" + prpCode + ", date=" + new Date()
						+ "================");*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			isRun = false;
		}
	}

	public void ComparePrpCntSyncTask(final Long prpCode) throws Exception {
		if (isRun) {
			return;
		}
		try {
			isRun = true;
			List<Map<String, Object>> prpCodes = compareResultDao.findComparingPrp(prpCode);

			for (Map<String, Object> info : prpCodes) {
				String type = (String) info.get("TYPE");
				// String prpCod = (String) info.get("PRP_CODE");
				callUpdatePrpExtendCnt(type, prpCode.toString());

			/*	System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS) + "================同步比对结果：type=" + type + "，prp_code=" + prpCode + ", date=" + new Date()
						+ "================");*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			isRun = false;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(rollbackFor = Exception.class , propagation = Propagation.REQUIRES_NEW)
	public void callUpdatePrpExtendCnt(final String type, final String prpCode) {
		jdbcTemplate.execute("{call sp_update_prpextend_check_cnt(?,?)}", new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(java.sql.CallableStatement stat) throws SQLException, DataAccessException {
				stat.setString(1, prpCode);
				stat.setString(2, type);
				stat.execute();
				return null;
			}
		});
	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public List<Map<String, Object>> getCompareTaskList() {
		return compareResultDao.getCompareTaskList();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public Map<String, CompareTemplateService> getCompareTemplateAll() {

		List<CompareTemplateSetting> compareList = (List<CompareTemplateSetting>) compareResultDao.createQuery(" from CompareTemplateSetting ").list();

		if (compareList.isEmpty())
			return null;

		Map<String, CompareTemplateService> compareMap = new HashMap<String, CompareTemplateService>();

		for (CompareTemplateSetting setting : compareList) {
			CompareTemplateService compareTemplateService = this.getServiceFactory().getService(setting.getTemplateBeanName(), CompareTemplateService.class);
			compareMap.put(setting.getDataType().toString(), compareTemplateService);
		}

		return compareMap;

	}

}