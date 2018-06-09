package com.test.example.schedule.master.compare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.compare.constant.CompareScheduleConstant;
import com.test.example.code.compare.dao.CompareMybatisDao;
import com.test.example.schedule.master.common.AbstractScheduleProvider;
import com.test.example.schedule.master.model.DataSummaryVO;
 

/**
 *    比对原始数据（compare_source）初始化  调度
 * @author cg
 */
@Component("compareDataInitScheduleProvider")
public class CompareDataInitScheduleProvider extends  AbstractScheduleProvider {
	
	@Autowired
	private CompareMybatisDao<?> compareMybatisDao ;
	
	public CompareDataInitScheduleProvider() {
		 super.setAvgSize(CompareScheduleConstant.DATA_INIT_AVG_SIZE);   // 计算数量 单节点/条
		 super.setChannelName( CompareScheduleConstant.DATA_INIT_CHANNEL_NAME );  // pub msg channel name 
		 super.setQueneKey(CompareScheduleConstant.DATA_INIT_QUENE_NAME );    // data quene name , for fill/get 
		 super.setThreadNum(CompareScheduleConstant.DATA_INIT_THREAD_NUM ); // 线程数量 
	}

	@Override
  @Transactional(rollbackFor = Exception.class ,readOnly=true)
	public DataSummaryVO getDataSummaryVO(Long totalSize,Long startKeyCode) {
		return compareMybatisDao.getDataSummaryVO("Compare.getDataInitSummaryVo", totalSize ,startKeyCode) ;
	}

}
