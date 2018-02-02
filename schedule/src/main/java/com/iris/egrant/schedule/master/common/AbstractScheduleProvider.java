package com.iris.egrant.schedule.master.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import com.iris.egrant.schedule.lifecycle.manager.LifecycleManager;
import com.iris.egrant.schedule.lifecycle.po.ServerNodeInfo;
import com.iris.egrant.schedule.master.model.BaseChannelMessage;
import com.iris.egrant.schedule.master.model.ChannelMessageTO;
import com.iris.egrant.schedule.master.model.DataSummaryVO;
 
/**
 * 
 * @author cg
 * 
 * @param <T>
 */
 // @Transactional(rollbackFor = Exception.class)
public abstract class AbstractScheduleProvider implements ScheduleProvider ,InitializingBean {
	
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LifecycleManager lifecycleManager ;

	@Resource(name="redisTemplateTx")
	private RedisTemplate<String, ?> redisTemplate;
	
	//没有数据 重试次数
	private static final int MAX_DATA_NOT_FOUND_COUNT = 3 ; 
	
	private int dataNotFoundCount = 0 ;
	
	private Long avgSize ;

	private String channelName ;
	
	private String queneKey ;
	
	private int threadNum ;
	
	/**
	 * 默认 比对nextStartCodeKey
	 */
	private String nextStartCodeKey  = "COMPARE_NEXT_START_KEY_CODE";
	
	public AbstractScheduleProvider() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	private void startKeyCodeRedisSave(Long value){
		// 
		BoundValueOperations<String, Long> ops = (BoundValueOperations<String, Long>) redisTemplate.boundValueOps(getNextStartCodeKey());
		redisTemplate.watch(getNextStartCodeKey()) ;
		redisTemplate.multi() ;
		ops.set(value); 
		redisTemplate.exec() ; 
	}
	
	@SuppressWarnings("unchecked")
	private Long startKeyCodeRedisGet(){
		// 
		BoundValueOperations<String, Long> ops = (BoundValueOperations<String, Long>) redisTemplate.boundValueOps(getNextStartCodeKey());
		return ops == null ? 0L : (ops.get() == null ? 0L : ops.get()) ;
	}
	
	@Override
	public int schedule(){
		System.out.println(this);
		// 检查存活节点
		Set<ServerNodeInfo> aliveNodesSet = lifecycleManager.getServerNodeInfoSetAlive() ;
		if (aliveNodesSet.size() == 0){
			LOGGER.warn("没有存活节点！") ;
			return  ScheduleProvider.CURRENT_STEP_FLAG;
		}
		// s.size()*avgSize 一次调度处理数据的理论值
		Long totalSize =  aliveNodesSet.size() * avgSize ;  // 存活节点数 * 平均数据量
		// 检查当前队列数据量
		BoundListOperations<String, ?> ops = redisTemplate.boundListOps(this.getQueneKey())  ;
		if (ops != null && ops.size() > totalSize * 2 ){ //总数的2倍不处理
			// 线程检查消息
			  sendCheckTpMsg(aliveNodesSet) ;
			 	return  ScheduleProvider.CURRENT_STEP_FLAG;
		 }
		// 
		// 抓取数据信息
		DataSummaryVO dataSummaryVO = this.getDataSummaryVO( totalSize ,startKeyCodeRedisGet() ) ;
		LOGGER.info("抓取数据：min：{},max：{},count：{}" , dataSummaryVO.getMinKeyCode() , dataSummaryVO.getMaxKeyCode(),dataSummaryVO.getCount());
		// 未获取到数据 重试判断
		if (dataSummaryVO.getCount()  == 0L){ // DB无数据
				if (ops == null ||  ops.size() == 0){ // DB和Redis均无数据
					// nextStartKeyCode  
					startKeyCodeRedisSave(0L) ;  // 之前的调度  可能存在某些节点死亡  该节点数据段未处理   需要从0抓取重新分配
					dataNotFoundCount ++ ;
				}
				if (dataNotFoundCount >= MAX_DATA_NOT_FOUND_COUNT ){ // 达到重试次数
					startKeyCodeRedisSave(0L) ;
					dataNotFoundCount = 0 ;
				  return ScheduleProvider.NEXT_STEP_FLAG;
				}else{
					// 线程检查消息
					sendCheckTpMsg(aliveNodesSet) ;
					return ScheduleProvider.CURRENT_STEP_FLAG;
				}
		}
		// DB 有数据
		else{
			  // DB无数据计数 置0
			  dataNotFoundCount = 0 ;
				// 设置startKeyCode 下次从此值开始抓取数据
				startKeyCodeRedisSave(dataSummaryVO.getMaxKeyCode() + 1) ;
				// 计算分配
				List<ChannelMessageTO> assignInfoList = this.assignData(aliveNodesSet ,dataSummaryVO ) ;
				LOGGER.info("分配信息:{}" , assignInfoList.toString());
				// pub消息
				if (assignInfoList.size() > 0 ){
					publishMessage(assignInfoList ,channelName) ;
				}else{
					LOGGER.info("没有pub消息！");
				}
				return ScheduleProvider.CURRENT_STEP_FLAG;
		}
	}
	
	private void publishMessage(Object o,String channel) {
		redisTemplate.convertAndSend(channel, o) ;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception{
		Assert.notNull(avgSize, "avgSize不能为空！") ;
		Assert.notNull(channelName, "channelName不能为空！") ;
		Assert.notNull(queneKey, "queneKey不能为空！") ;
		Assert.isTrue(this.threadNum > 0 , "threadNum 必须大于0");
		Assert.notNull(nextStartCodeKey, "nextStartCodeKey不能为空！") ;
	}
	
	/**
	 *   抓取待处理数据的汇总信息
	 * @param long totalSize  一次调度处理数据的理论值
	 * @param  Long startKeyCode 起始值 默认0
	 * @return
	 */
	protected abstract DataSummaryVO getDataSummaryVO(Long totalSize,Long startKeyCode);
	
	 
	/**
	 * pub 线程检查消息
	 * @param serverNodeInfoSet
	 */
	private void sendCheckTpMsg(Set<ServerNodeInfo> serverNodeInfoSet){
		List<ChannelMessageTO> msgList =  new ArrayList<ChannelMessageTO>();
		Iterator<ServerNodeInfo> i = serverNodeInfoSet.iterator() ;
		while (i.hasNext()){ 
			ServerNodeInfo tempSNI = i.next() ;
			msgList.add( new ChannelMessageTO(getQueneKey() ,tempSNI.getId() ,getThreadNum() ,BaseChannelMessage.TYPE_CHECK_TP ));
		}
		
		if(msgList.size() > 0){
			publishMessage(msgList ,channelName) ;
		}
	}
	
	/**
	 *  分配数据  
	 *   默认方法 平均分配
	 * @param serverNodeInfoSet  存活节点集
	 * @param dataSummaryVO 数据汇总信息
	 * @param msgType 消息类型
	 * @return  List<ChannelMessageTO> 返回channel消息List
	 */
	private List<ChannelMessageTO> assignData(Set<ServerNodeInfo> serverNodeInfoSet,DataSummaryVO dataSummaryVO ){
		List<ChannelMessageTO> msgList =  new ArrayList<ChannelMessageTO>();
	 // 	dataList.size() > s.size() * avgSize ;
		Iterator<ServerNodeInfo> i = serverNodeInfoSet.iterator() ;
		//判断数据总量是否达到了平均分配值之和
		Long tempAvgSize = avgSize ;
		if((avgSize * serverNodeInfoSet.size()) > dataSummaryVO.getCount()){
			tempAvgSize = ( dataSummaryVO.getMaxKeyCode() -  dataSummaryVO.getMinKeyCode() ) / serverNodeInfoSet.size()  + 1 ; // 如果数据不够所有节点n*avgSize,则重新计算以下平均值
		}
		Long startKeyCode  = dataSummaryVO.getMinKeyCode(); // 最小的
		Long endKeyCode ;
		while (i.hasNext()){  // 遍历每个存活节点
			ServerNodeInfo tempSNI = i.next() ;
			endKeyCode = startKeyCode + tempAvgSize ;  // 忽略不连续数据
			msgList.add(  new ChannelMessageTO(getQueneKey() ,tempSNI.getId() , getThreadNum() ,startKeyCode ,endKeyCode ,BaseChannelMessage.TYPE_FETCH_AND_CHECK )) ; // 创建channel message对象
			startKeyCode = endKeyCode + 1 ;  // 下一个节点开始的startKeyCode
			if( endKeyCode >= dataSummaryVO.getMaxKeyCode()){
				break ;
			}
		}
		return msgList ;
	} 

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	public Long getAvgSize() {
		return avgSize;
	}

	public void setAvgSize(Long avgSize) {
		this.avgSize = avgSize;
	}

	public String getQueneKey() {
		return queneKey;
	}

	public void setQueneKey(String queneKey) {
		this.queneKey = queneKey;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	
	public String getNextStartCodeKey() {
		return nextStartCodeKey;
	}

	public void setNextStartCodeKey(String nextStartCodeKey) {
		this.nextStartCodeKey = nextStartCodeKey;
	}
	
}