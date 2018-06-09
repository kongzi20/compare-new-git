package com.test.example.schedule.master.rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.example.code.compare.constant.ProposalRuleCheckScheduleConstant;
import com.test.example.code.compare.dao.CompareMybatisDao;
import com.test.example.schedule.master.common.AbstractScheduleProvider;
import com.test.example.schedule.master.model.DataSummaryVO;

@Component("proposalRuleCheckScheduleProvider")
public class ProposalRuleCheckScheduleProvider extends AbstractScheduleProvider {
	
	@Autowired
	private CompareMybatisDao<?> compareMybatisDao ;

	public ProposalRuleCheckScheduleProvider() {
		 super.setAvgSize(ProposalRuleCheckScheduleConstant.AVG_SIZE);   // 计算数量 单节点/条
		 super.setChannelName(ProposalRuleCheckScheduleConstant.CHANNEL_NAME );  // pub msg channel name 
		 super.setQueneKey(ProposalRuleCheckScheduleConstant.QUENE_NAME );    // data quene name , for fill/get 
		 super.setThreadNum(ProposalRuleCheckScheduleConstant.THREAD_NUM ); // 线程数量 
		 super.setNextStartCodeKey(ProposalRuleCheckScheduleConstant.NEXT_START_CODE_KEY);
	}

	@Override
	protected DataSummaryVO getDataSummaryVO(Long totalSize, Long startKeyCode) {
		return  compareMybatisDao.getDataSummaryVO("Compare.getProposalRuleCheckSummaryVo" ,totalSize ,startKeyCode) ;
	}
}
