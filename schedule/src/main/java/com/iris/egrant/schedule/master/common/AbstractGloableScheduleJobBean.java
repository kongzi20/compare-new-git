package com.iris.egrant.schedule.master.common;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class AbstractGloableScheduleJobBean  extends QuartzJobBean  {

	protected  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext ;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// 获取ioc
		Scheduler scheduler = context.getScheduler();
		try {
			applicationContext = (ApplicationContext) (scheduler.getContext().get("applicationContextKey"));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		// init
		init() ;
		// 执行业务逻辑
		globaleScheduler() ;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	public abstract void init();
	
	/**
	 * 调度方法
	 */
	protected abstract void globaleScheduler();

	
}