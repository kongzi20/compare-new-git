package com.test.example.code.compare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.test.example.code.compare.model.CompareResultSyncVO;
import com.test.example.code.compare.model.CompareResultVO;
import com.test.example.core.dao.mybatis.BaseMybatisDao;
import com.test.example.schedule.master.model.DataSummaryVO;

@Repository
public class CompareMybatisDao<T> extends BaseMybatisDao<T> {
	/**
	 * 	
	 * @param pageSize
	 * @return
	 */
	public DataSummaryVO getDataSummaryVO(String sqlId,long totalSize ,Long startKeyCode){
		Map<String, Object> paramMap = new HashMap<String, Object>() ;
		paramMap.put("totalSize" ,totalSize);
		paramMap.put("startKeyCode" ,startKeyCode);
		return (DataSummaryVO) super.getOneInfo(sqlId , paramMap) ;
	}

	@SuppressWarnings("unchecked")
	public List<CompareResultVO> getCompareResultList(Long startKeyCode, Long endKeyCode) {
		Map<String, Object> paramMap = new HashMap<String, Object>() ;
		paramMap.put("startKeyCode" ,startKeyCode);
		paramMap.put("endKeyCode" ,endKeyCode);
		return (List<CompareResultVO>) super.getSearchList("Compare.getCompareResultVoList", paramMap) ;
	}

	public List<CompareResultSyncVO> getCompareResultSyncVOList(Long startKeyCode, Long endKeyCode) {
		Map<String, Object> paramMap = new HashMap<String, Object>() ;
		paramMap.put("startKeyCode" ,startKeyCode);
		paramMap.put("endKeyCode" ,endKeyCode);
		return (List<CompareResultSyncVO>) super.getSearchList("Compare.getCompareResultSyncVoList", paramMap) ;
	}

}
