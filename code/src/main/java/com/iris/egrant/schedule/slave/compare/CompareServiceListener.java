package com.iris.egrant.schedule.slave.compare;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;
import com.iris.egrant.core.thread.ThreadPoolHolder;
import com.iris.egrant.schedule.slave.common.AbstractMessageServiceListener;


/**
 *   项目比对各过程 线程池实现方式
 * @author cg
 *
 * @param <T>
 */
public abstract class CompareServiceListener<T> extends AbstractMessageServiceListener<T> {

	public CompareServiceListener() {
		super();
	  super.setThreadPoolHolderBeanId("compareThreadPoolHolder");
	}
}

@Component("compareThreadPoolHolder")
class CompareThreadPoolHolder implements ThreadPoolHolder {

	private ReentrantLock lock = new ReentrantLock() ;
	
	private ThreadPoolExecutor threadPoolExecutor  = (ThreadPoolExecutor) Executors.newFixedThreadPool(1) ;
	
	public CompareThreadPoolHolder() {
		super();
	}
	
	@Override
	public void lock() {
		lock.lock(); 
	}
	@Override
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}
	@Override
	public void unLock() {
		lock.unlock();
	}
	 
}