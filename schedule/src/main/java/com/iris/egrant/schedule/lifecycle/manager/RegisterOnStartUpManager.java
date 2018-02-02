package com.iris.egrant.schedule.lifecycle.manager;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.util.Assert;

import com.iris.egrant.schedule.constant.LifeCycleConstant;
import com.iris.egrant.schedule.lifecycle.po.ServerNodeInfo;

/**
 *   注册/注销
 * @author cg
 *
 */
public class RegisterOnStartUpManager implements InitializingBean , DisposableBean  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterOnStartUpManager.class);
	
	@Autowired
	private LifecycleManager lifecycleManager ;
	
	/**
	 *  本节点信息持有
	 */
	public static ServerNodeInfo serverNodeInfo ;
	
	/**
	 *  注册
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// 当前节点信息
		String nodeName = System.getProperty("node.name");
		Assert.notNull(nodeName,"node.name 启动参数未设置！");
		serverNodeInfo = new ServerNodeInfo();
		serverNodeInfo.setId(nodeName);
		Date curDate = new Date() ; // 当前时间
		serverNodeInfo.setRegisterTime(curDate) ;
		serverNodeInfo.setLastHeartBeatTime(curDate) ;
		// log
		LOGGER.info("节点信息：" + serverNodeInfo );
		//删除
		this.clearIfExist() ;
		//追加 
		BoundSetOperations<String, ServerNodeInfo> ops  = lifecycleManager.getBoundSetOperations(LifeCycleConstant.SERVER_NODE_SET_KEY);
		Long isSuccess = ops.add(serverNodeInfo);
		if ( 1L == isSuccess ){
			LOGGER.info("注册成功！" +  isSuccess + ":" + serverNodeInfo );
		}else{
			LOGGER.info("注册失败！");
		}
	}
	
	 /**
	  * 注销
	  * @throws Exception
	  */
	@Override
	public void destroy() throws Exception {
		 this.clearIfExist() ;
	}
	
	private void clearIfExist(){
			BoundSetOperations<String, ServerNodeInfo> ops  = lifecycleManager.getBoundSetOperations(LifeCycleConstant.SERVER_NODE_SET_KEY);
			Set<ServerNodeInfo> s  = ops.members(); // 获取
			Iterator<ServerNodeInfo> i = s.iterator() ;
			while (i.hasNext()){
				ServerNodeInfo temp =	i.next(); 
				if (serverNodeInfo.equals(temp)){
					ops.remove(temp);
				}
			} 
		}
}
