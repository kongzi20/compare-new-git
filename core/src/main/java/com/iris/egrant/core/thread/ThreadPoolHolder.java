package com.iris.egrant.core.thread;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 业务线程池 持有者
 * @author cg
 *
 */
public interface ThreadPoolHolder {
	 void lock() ;
	 void unLock() ;
	 ThreadPoolExecutor getThreadPoolExecutor() ;
}
