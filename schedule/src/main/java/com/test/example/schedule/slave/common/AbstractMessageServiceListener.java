package com.test.example.schedule.slave.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.test.example.core.thread.ThreadPoolHolder;
import com.test.example.schedule.lifecycle.manager.RegisterOnStartUpManager;
import com.test.example.schedule.master.model.BaseChannelMessage;
import com.test.example.schedule.master.model.ChannelMessageTO;

/**
 *   
 * @author cg
 */
public abstract class AbstractMessageServiceListener<T> implements MessageListener , ApplicationContextAware {
	
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private ApplicationContext applicationContext ;
	
	private String threadPoolHolderBeanId ; 
	
	@Resource(name="redisTemplateTx")
	private RedisTemplate<String ,T> redisTemplateTx ;
	
	@Resource(name="redisTemplateNoTx")
	private RedisTemplate<String ,T> redisTemplateNoTx ;
	
	private String dataQueneKey ;
	
	/**
	 *  TODO
	 */
	private int threadNum =  1 ;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class )
	public void onMessage(Message message, byte[] pattern) {
		System.out.println(this);
		byte[] rawByteBody = message.getBody();
		List<ChannelMessageTO> messageList  = (List<ChannelMessageTO>) redisTemplateTx.getValueSerializer().deserialize(rawByteBody) ; //  
		Long startKeyCode = 0L;
		Long endKeyCode = 0L;
		ChannelMessageTO channelMsg  = null ; 
		//分配给本节点的数据
		for (ChannelMessageTO cmt : messageList) {
			if (RegisterOnStartUpManager.serverNodeInfo.getId().equals(cmt.getServerNodeId())){ // 
				 channelMsg = cmt ;
			 }
		}
		 Assert.notNull(channelMsg , "channelMsg不能是空！");
		 LOGGER.info(channelMsg.toString());
		 //线程数
		 setThreadNum(channelMsg.getThreadNum()) ;
		 //数据队列
		 setDataQueneKey(channelMsg.getDataQueneKey()) ;
		 //消息类型
		 int msgType = channelMsg.getType() ;
		 if(BaseChannelMessage.TYPE_FETCH_AND_CHECK == msgType){
			 // 
			 startKeyCode = channelMsg.getStartKeyCode() ;
			 endKeyCode = channelMsg.getEndKeyCode() ;
			// 防守
		  if (startKeyCode == 0L ||endKeyCode == 0L || getDataQueneKey() == null ) { 
			  LOGGER.info("节点：{}，分配数据异常！" , RegisterOnStartUpManager.serverNodeInfo.getId()) ;
			  return ;
		   }
		    // 抓取数据 
		  // TODO 需处理一次性读取数据过多 发生OOM的情形
		  List<T>  dataList = fetchDb( startKeyCode , endKeyCode) ; 
		  if (CollectionUtils.isEmpty(dataList)){
			   LOGGER.info("节点：{}，fetchDb数据为空！" , RegisterOnStartUpManager.serverNodeInfo.getId()) ;
			   return ;
		     } 
		    // 入队列  
	     pushDataQuene(dataList) ;
		  }
	    // 检查Redis数据队列、线程池线程数
     checkDataQueneAndThreadPoolSize();
	}
	
	/**
	 * 检查
	 *  数据队列数据 
	 *  线程池线程数
	 */
	private void checkDataQueneAndThreadPoolSize() {
		Long dataQueneSize = getDataQueneSize() ;
		ThreadPoolHolder threadPoolHolder = (ThreadPoolHolder) getApplicationContext().getBean(getThreadPoolHolderBeanId()) ;
		// 需追加线程数
		int tpSizeToFill = getThreadNum() - threadPoolHolder.getThreadPoolExecutor().getActiveCount(); 
		if (dataQueneSize <= 0 || tpSizeToFill <= 0){
			return ;
		}
		try{
			threadPoolHolder.lock();
			threadPoolHolder.getThreadPoolExecutor().setCorePoolSize(getThreadNum());
			for(int i = 0 ; i < tpSizeToFill ; i++){
				 threadPoolHolder.getThreadPoolExecutor().execute(new ServiceTask<T>(this));
			 }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			threadPoolHolder.unLock();
		}
		
	}
 
 /**
  *   数据库批量抓取数据 
  * @param startKeyCode 主健开始值
  * @param endKeyCode 主健结束值
  * @return
  */
	protected abstract  List<T> fetchDb(Long startKeyCode ,Long endKeyCode) ;
	
 /**
 *    批量数据处理
 * @param objList
 */
	protected abstract  void doService(List<T> objList) ;
	 
	
	/**
	 *    获取List
	 * @param dataQueneKey
	 * @param dataList
	 * @return
	 */
	protected List<T> getDataList(){
		 List<T> list = new ArrayList<T>() ;
		 BoundListOperations<String, T>  ops = redisTemplateNoTx.boundListOps(getDataQueneKey());
		  for (int i = 0 ; i < 10 ; i++){
			  T  o = ops.rightPop() ;
			 if ( o != null){
			  list.add(o);
			  }
		  }
	   return list ;
	}
	
	/**
	 *   批量入Redis数据队列
	 * @param dataQueneKey
	 * @param dataList
	 * @return Long 当前队列长度
	 */
	@SuppressWarnings("unchecked")
	private void pushDataQuene(List<T> dataList){
		BoundListOperations<String, T>  ops = redisTemplateNoTx.boundListOps(getDataQueneKey());
		for (T obj : dataList) {  // TODO
			 ops.leftPush(obj); 
		 }
	}
	
	/**
	 *  获取Redis数据队列长度
	 * @return
	 */
	private Long getDataQueneSize(){
		return redisTemplateNoTx.boundListOps(getDataQueneKey()).size();
	}
	
	public RedisTemplate<String ,?> getRedisTemplate() {
		return redisTemplateTx;
	}

	public void setRedisTemplate(RedisTemplate<String ,T> redisTemplate) {
		this.redisTemplateTx = redisTemplate;
	}

	public String getDataQueneKey() {
		return dataQueneKey;
	}

	public void setDataQueneKey(String dataQueneKey) {
		this.dataQueneKey = dataQueneKey;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public String getThreadPoolHolderBeanId() {
		return threadPoolHolderBeanId;
	}

	public void setThreadPoolHolderBeanId(String threadPoolHolderBeanId) {
		this.threadPoolHolderBeanId = threadPoolHolderBeanId;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	} 
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	  this.applicationContext = applicationContext ;
	}
	
}

class ServiceTask<T> implements Runnable{
	  private AbstractMessageServiceListener<T> msgServiceListener ;
	  private static final int FETCH_DATA_RETRY_COUNT  = 10 ; // 获取数据尝试次数
	  private int noDataCnt = 0 ;	
	  
	  
	  public ServiceTask(AbstractMessageServiceListener<T> msgServiceListener) {
			super();
			this.msgServiceListener = msgServiceListener;
	   }

@Override
	public void run() {
	  while(true){
			  // check fail times
				if (noDataCnt >= FETCH_DATA_RETRY_COUNT ){
					break ;
				}
		    // fetch redis
				List<T> objList = msgServiceListener.getDataList();
				if (objList.size() == 0 ){
					noDataCnt ++ ;
					try {
						Thread.sleep(1000) ;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}else{
					noDataCnt = 0 ; // 
			  // do service
					try {
						msgServiceListener.doService(objList);
					} catch (Exception e) {
							msgServiceListener.LOGGER.error("doService()方法错误,参数:%s" ,objList);
						  e.printStackTrace();
					}
				}
	    }
}
} 