package com.test.example.schedule.master.compare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.compare.constant.CompareScheduleConstant;
import com.test.example.code.compare.dao.CompareMybatisDao;
import com.test.example.schedule.master.common.AbstractScheduleProvider;
import com.test.example.schedule.master.model.DataSummaryVO;

@Component("compareDataResultSyncScheduleProviver")
public class CompareDataResultSyncScheduleProviver  extends AbstractScheduleProvider{
	
	@Autowired
	private CompareMybatisDao<?> compareMybatisDao ; 

	public CompareDataResultSyncScheduleProviver() { 	 
		 super.setAvgSize(CompareScheduleConstant.DATA_RESULT_SYNC_SIZE);   // 计算数量 单节点/条
		 super.setChannelName( CompareScheduleConstant.DATA_RESULT_SYNC_CHANNEL_NAME );  // pub msg channel name 
		 super.setQueneKey(CompareScheduleConstant.DATA_RESULT_SYNC_QUENE_NAME );    // data quene name , for fill/get 
		 super.setThreadNum(CompareScheduleConstant.DATA_RESULT_THREAD_NUM ); // 线程数量 
	}

	@Override
	@Transactional(rollbackFor = Exception.class ,readOnly=true)
	protected DataSummaryVO getDataSummaryVO(Long totalSize ,Long startKeyCode ) {
		return compareMybatisDao.getDataSummaryVO("Compare.getDataResultSyncSummaryVo" ,totalSize ,startKeyCode) ;
	}

}
