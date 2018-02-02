package com.iris.egrant.code.compare.constant;

/**
 *  项目比较 常量
 * @author cg
 *
 */
public class CompareScheduleConstant {
	/**
	 * 比较 计算过程控制
	 */
	public final static String CALCULATE_PROCESS_MAP_KEY = "CALCULATE_PROCESS_MAP_KEY";
	
	private final static String CHANNEL_SUBFIX = "_CHANNEL" ;
	
	/**
	 * publish channel name
	 */
	public final static String DATA_INIT_CHANNEL_NAME = CompareCalProcess.DATA_INIT.getValue() + CHANNEL_SUBFIX;
	public final static String DATA_FILTER_CHANNEL_NAME = CompareCalProcess.DATA_FILTER.getValue() +  CHANNEL_SUBFIX ;
	public final static String DATA_SIMILIAR_CHANNEL_NAME = CompareCalProcess.DATA_SIMILIAR.getValue() +  CHANNEL_SUBFIX ;
	public final static String DATA_RESULT_SYNC_CHANNEL_NAME = CompareCalProcess.DATA_RESULT_SYNC.getValue() +  CHANNEL_SUBFIX;
	
	/**
	 * 计算过程 数据队列
	 */
	private final static String QUENE_SUBFIX = "_QUENE" ;
	public final static String DATA_INIT_QUENE_NAME = CompareCalProcess.DATA_INIT.getValue() + QUENE_SUBFIX;
	public final static String DATA_FILTER_QUENE_NAME = CompareCalProcess.DATA_FILTER.getValue() +  QUENE_SUBFIX ;
	public final static String DATA_SIMILIAR_QUENE_NAME = CompareCalProcess.DATA_SIMILIAR.getValue() +  QUENE_SUBFIX ;
	public final static String DATA_RESULT_SYNC_QUENE_NAME = CompareCalProcess.DATA_RESULT_SYNC.getValue() +  QUENE_SUBFIX;
	
	/**
	 * 计算数量 单节点/条
	 */
	public final static Long DATA_INIT_AVG_SIZE = 5000L;
	public final static Long DATA_FILTER_AVG_SIZE = 2000L;
	public final static Long DATA_SIMILIAR_AVG_SIZE = 2000L;
	public final static Long DATA_RESULT_SYNC_SIZE = 1000L;
	
	/**
	 * 线程池数量
	 */
	public final static int DATA_INIT_THREAD_NUM = 10;
	public final static int DATA_FILTER_THREAD_NUM = 10;
	public final static int DATA_SIMILIAR_THREAD_NUM = 1;
	public final static int DATA_RESULT_THREAD_NUM = 5;
	
}