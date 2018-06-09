package com.test.example.schedule.master.common;

/**
 *   数据处理通用调度接口
 * @author cg
 *
 */
public interface ScheduleProvider {
	
	/**
	 * 标示 对于多步骤任务 标示不跳转至下一个步骤 
	 */
	static final int CURRENT_STEP_FLAG = 0 ; 
	
	/**
	 * 标示 对于多步骤任务 标示跳转至下一个步骤 
	 */
	static final int NEXT_STEP_FLAG = 1  ; 
	/**
	 * 调度
	 */
	int schedule() ;
	
}
