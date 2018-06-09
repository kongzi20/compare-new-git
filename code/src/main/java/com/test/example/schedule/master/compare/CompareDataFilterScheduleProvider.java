package com.test.example.schedule.master.compare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.example.code.compare.constant.CompareScheduleConstant;
import com.test.example.code.compare.dao.CompareMybatisDao;
import com.test.example.schedule.master.common.AbstractScheduleProvider;
import com.test.example.schedule.master.model.DataSummaryVO;

/**
 *  项目相关度过滤 调度
 * @author cg
 *
 */
@Component("compareDataFilterScheduleProvider")
public class CompareDataFilterScheduleProvider extends 	AbstractScheduleProvider {
	
	@Autowired
	private CompareMybatisDao<?> compareMybatisDao ;
	
	public CompareDataFilterScheduleProvider() {
		 super.setAvgSize( CompareScheduleConstant.DATA_FILTER_AVG_SIZE );   // 计算数量 单节点/条
		 super.setChannelName(CompareScheduleConstant.DATA_FILTER_CHANNEL_NAME); // pub msg channel name 
		 super.setQueneKey(CompareScheduleConstant.DATA_FILTER_QUENE_NAME );    // data quene name , for fill/get
		 super.setThreadNum(CompareScheduleConstant.DATA_FILTER_THREAD_NUM );  // 线程数量 
	}

	@Override
	protected DataSummaryVO getDataSummaryVO(Long totalSize ,Long startKeyCode) {
		 return compareMybatisDao.getDataSummaryVO("Compare.getDataFilterSummaryVo" , totalSize ,startKeyCode) ;   // 此处DataSummaryVO.id为compare_list的key_code
	}

}
