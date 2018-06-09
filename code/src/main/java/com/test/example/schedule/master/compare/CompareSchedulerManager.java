package com.test.example.schedule.master.compare;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import com.test.example.code.compare.constant.CompareCalProcess;
import com.test.example.code.compare.constant.CompareScheduleConstant;
import com.test.example.schedule.master.common.AbstractGloableScheduleJobBean;
import com.test.example.schedule.master.common.ScheduleProvider;
 
/**
 * 
 *  项目比对业务 全局计算调度器 
 *  1、此job实例由Quartz创建 不由spring创建 
 *  2、此job全局单例不支持并发
 *  3、成员变量请在 init()方法实例化
 * @author cg
 */
public class CompareSchedulerManager  extends AbstractGloableScheduleJobBean   {
	
	
	public static final String CAL_PROCESS_STATUS_RUNNING =  "1" ;
	
	public static final String CAL_PROCESS_STATUS_WAITTING =  "0" ;
	
	@Resource(name="redisTemplateTx")
	private RedisTemplate<String, ?> redisTemplate ;
	
	private ReentrantLock lock = new ReentrantLock() ;
	
	/**
	 * 计算过程 
	 */
	private final static String key = CompareScheduleConstant.CALCULATE_PROCESS_MAP_KEY ;
	
	@Override
	public void globaleScheduler() {
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);
		Map<String,String> calProcessMap = ops.entries();
		if (calProcessMap.size()  == 0){
			this.initCalProcessMap() ;
		}
		CompareCalProcess curCalProcess = this.getCurrentCalProcess() ;
		LOGGER.info("当前计算步骤：" + curCalProcess.getValue()) ;
		ScheduleProvider provider =  null ;
		String beanId =  null ;
		switch (curCalProcess) {
			case DATA_INIT : beanId = "compareDataInitScheduleProvider" ; break;
			case DATA_FILTER : beanId = "compareDataFilterScheduleProvider" ; break;
			case DATA_SIMILIAR : beanId = "compareDataSimiliarScheduleProviver" ; break;
			case DATA_RESULT_SYNC : beanId = "compareDataResultSyncScheduleProviver" ; break;
			default: 	break;
		}
		// 
		Assert.notNull(beanId, "provider beanId不能为空！");
		provider = (ScheduleProvider) super.getApplicationContext().getBean(beanId) ;
		Assert.notNull(provider, "provider 对象不能为空！");
		// 业务调度
		int i = provider.schedule() ; 
		// 比对特有过程 需判断此处理过程是否完成 完成后进入下一个处理过程
		// 其他任务调度无拆分过程  则无需处理返回值
	  if(ScheduleProvider.NEXT_STEP_FLAG == i){
		   turnToNext(curCalProcess) ;
	    }
	}
	
	/**
	 *   更新redis中比对计算过程状态
	 * @param current 当前
	 */
	private void turnToNext(CompareCalProcess current){
		CompareCalProcess next = null ;
		switch (current) {
			case DATA_INIT : next = CompareCalProcess.DATA_FILTER; break;
			case DATA_FILTER :  next = CompareCalProcess.DATA_SIMILIAR  ; break;
			case DATA_SIMILIAR :   next = CompareCalProcess.DATA_RESULT_SYNC ; break;
			case DATA_RESULT_SYNC :  next = CompareCalProcess.DATA_INIT ; break;
			default: 	break;
	   }
		Assert.notNull(next) ;
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);
		try {
			lock.lock() ;
			redisTemplate.watch(key) ;
			redisTemplate.multi() ;
			ops.put(current.getValue(), CompareSchedulerManager.CAL_PROCESS_STATUS_WAITTING) ; // 0
			ops.put(next.getValue(), CompareSchedulerManager.CAL_PROCESS_STATUS_RUNNING) ; // 1
			redisTemplate.exec() ;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}finally{
			lock.unlock() ;
		}
	}

	/**
	 * 获得redis中 比对任务的当前计算步骤
	 * @return
	 */
	private CompareCalProcess getCurrentCalProcess() {
		CompareCalProcess curCalProcess = null  ;
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);
		Map<String,String> m = ops.entries(); 
		Iterator<String> i = m.keySet().iterator() ;
		while (i.hasNext()){
			String key = i.next() ;
			String status =  m.get(key);
			if (CompareSchedulerManager.CAL_PROCESS_STATUS_RUNNING.equals(status)){ // "1"
				curCalProcess = CompareCalProcess.valueOf(key) ;
				break;
			}
		}
		// 以上遍历没有取到 // 取第一个
		if (null == curCalProcess){  
			curCalProcess = CompareCalProcess.DATA_INIT ;
		}
		// 更新状态 当前在计算的过程
		try {
			lock.lock() ;
			redisTemplate.watch(key) ;
			redisTemplate.multi() ;
			ops.put(curCalProcess.getValue(), CompareSchedulerManager.CAL_PROCESS_STATUS_RUNNING) ;
			redisTemplate.exec() ;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}finally{
			lock.unlock() ;
		}
		return curCalProcess;
	}
 
	/**
	 * 初始化redis中 比对计算过程Set
	 */
	private void initCalProcessMap() {
		try {
			lock.lock() ;
			redisTemplate.watch(key) ;
			redisTemplate.multi() ;
			BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);
			CompareCalProcess[] arr = CompareCalProcess.values() ;
			for (CompareCalProcess cp : arr) {
				ops.put(cp.getValue(),CompareSchedulerManager.CAL_PROCESS_STATUS_WAITTING);
			}
			redisTemplate.exec() ;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}finally{
			lock.unlock() ;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
	  this.redisTemplate = (RedisTemplate<String, ?>) super.getApplicationContext().getBean("redisTemplateTx") ;
	}
}
