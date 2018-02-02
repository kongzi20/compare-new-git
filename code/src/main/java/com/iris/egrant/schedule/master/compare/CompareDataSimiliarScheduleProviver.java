package com.iris.egrant.schedule.master.compare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.iris.egrant.code.compare.constant.CompareScheduleConstant;
import com.iris.egrant.code.compare.dao.CompareMybatisDao;
import com.iris.egrant.schedule.master.common.AbstractScheduleProvider;
import com.iris.egrant.schedule.master.model.DataSummaryVO;

@Component("compareDataSimiliarScheduleProviver")
public class CompareDataSimiliarScheduleProviver  extends 	AbstractScheduleProvider  {
	
	@Autowired
	private CompareMybatisDao<?> compareMybatisDao ;

	public CompareDataSimiliarScheduleProviver( ) {
		 super.setAvgSize(CompareScheduleConstant.DATA_SIMILIAR_AVG_SIZE);   // 计算数量 单节点/条
		 super.setChannelName( CompareScheduleConstant.DATA_SIMILIAR_CHANNEL_NAME );  // pub msg channel name 
		 super.setQueneKey(CompareScheduleConstant.DATA_SIMILIAR_QUENE_NAME );    // data quene name , for fill/get 
		 super.setThreadNum(CompareScheduleConstant.DATA_SIMILIAR_THREAD_NUM ); // 线程数量 
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class ,readOnly=true)
	protected DataSummaryVO getDataSummaryVO(Long totalSize ,Long startKeyCode) {
		return compareMybatisDao.getDataSummaryVO("Compare.getDataSimiliarSummaryVo" ,totalSize ,startKeyCode) ;
	}
}