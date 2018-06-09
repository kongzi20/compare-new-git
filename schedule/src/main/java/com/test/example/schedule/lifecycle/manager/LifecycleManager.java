package com.test.example.schedule.lifecycle.manager;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.test.example.schedule.constant.LifeCycleConstant;
import com.test.example.schedule.lifecycle.po.ServerNodeInfo;


public class LifecycleManager {
	
	@Resource(name="redisTemplateTx")
	private RedisTemplate<String, ?> redisTemplate ;
	
	/**
	 *  节点列表Set
	 * @return
	 */
	public Set<ServerNodeInfo> getServerNodeInfoSet(){
		BoundSetOperations<String, ServerNodeInfo>  ops =  getBoundSetOperations(LifeCycleConstant.SERVER_NODE_SET_KEY) ;
		return ops.members();
	}
	
	/**
	 * 存活节点Set
	 * @return
	 */
	public Set<ServerNodeInfo> getServerNodeInfoSetAlive(){
		Set<ServerNodeInfo> s =  getServerNodeInfoSet();  
		Iterator<ServerNodeInfo> i =  s.iterator();
		while (i.hasNext()){
			ServerNodeInfo temp =  i.next() ;
			Long lastHBTime = temp.getLastHeartBeatTime().getTime() ;
			Long curTime = System.currentTimeMillis();
			Boolean isAlive =  (curTime - lastHBTime ) < LifeCycleConstant.NODE_ALIVE_TIMEOUT ;
			if (!isAlive){
				i.remove() ;
			}
		}
		return s ;
	}
	
	/**
	 *  节点列表Ops
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  BoundSetOperations<String, com.test.example.schedule.lifecycle.po.ServerNodeInfo> getBoundSetOperations(String key){
		return (BoundSetOperations<String, ServerNodeInfo>) redisTemplate.boundSetOps(key);  
	}

}
