package com.test.example.schedule.lifecycle.manager;


import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;

import com.test.example.schedule.constant.LifeCycleConstant;
import com.test.example.schedule.lifecycle.po.ServerNodeInfo;
 

public class HeartBeatManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterOnStartUpManager.class);
	
	@Autowired
	private LifecycleManager lifecycleManager ;
	
	public void heartBeat(){
		ServerNodeInfo curServerNodeInfo = RegisterOnStartUpManager.serverNodeInfo ;
		BoundSetOperations<String, ServerNodeInfo> ops = lifecycleManager.getBoundSetOperations(LifeCycleConstant.SERVER_NODE_SET_KEY);
		Set<ServerNodeInfo> s  = ops.members(); // 获取
		Iterator<ServerNodeInfo> i = s.iterator();
		while (i.hasNext()){
			ServerNodeInfo temp =	i.next(); 
			if (curServerNodeInfo.equals(temp)){
					ops.remove(temp);
			} 
		}
		// 
		curServerNodeInfo.setLastHeartBeatTime(new Date());
		Long isSuccess = ops.add(curServerNodeInfo);
		if (isSuccess == 1L){
			LOGGER.info("心跳！:" + curServerNodeInfo );
		}else{
			LOGGER.info("心跳失败！");
		}
	}
}