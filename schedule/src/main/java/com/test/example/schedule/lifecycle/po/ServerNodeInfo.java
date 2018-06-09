package com.test.example.schedule.lifecycle.po;

import java.io.Serializable;
import java.util.Date;

/**
 *  节点信息
 * @author cg
 * 
 */
public class ServerNodeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2106649388917062929L;
	private String id;
	private Date registerTime;
	private Date lastHeartBeatTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Date getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(Date lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public Date getRegisterTime() {
		return registerTime;
	}
	
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Boolean isActive(){
		// 毫秒
		Long lastHeartBeatInterval =  System.currentTimeMillis() - this.lastHeartBeatTime.getTime();
		return lastHeartBeatInterval < 30000;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + id.hashCode();
		hash = 31 * hash + (null == id ? 0 : id.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof ServerNodeInfo) {
			ServerNodeInfo tempObj = (ServerNodeInfo) obj;
			return tempObj.getId().equals(this.getId());
		}

		return false;
	}
	
	@Override
	public String toString() {
		return  "[ID:" + this.getId() + ",RegisterTime:" + this.getRegisterTime() + ",lastHeartBeatTime:" + this.lastHeartBeatTime + "]";
	}

}
