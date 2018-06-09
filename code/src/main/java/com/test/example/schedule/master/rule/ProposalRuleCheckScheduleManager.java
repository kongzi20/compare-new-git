package com.test.example.schedule.master.rule;

import com.test.example.schedule.master.common.AbstractGloableScheduleJobBean;
import com.test.example.schedule.master.common.ScheduleProvider;

/**
 * 
 *   规则计算 全局调度器 
 *  1、此job实例由Quartz创建 不由spring创建 
 *  2、此job全局单例不支持并发 
 *  3、成员变量 请在 init()方法实例化
 * @author cg
 */
public class ProposalRuleCheckScheduleManager extends AbstractGloableScheduleJobBean {
	
	private ScheduleProvider proposalRuleCheckScheduleProvider;

	public ProposalRuleCheckScheduleManager() {
	 super() ;
	}

	@Override
	protected void globaleScheduler() {
		proposalRuleCheckScheduleProvider.schedule() ;
	}
	
	@Override
	public void init() {
		proposalRuleCheckScheduleProvider = (ScheduleProvider) getApplicationContext().getBean("proposalRuleCheckScheduleProvider") ;
	}

}
