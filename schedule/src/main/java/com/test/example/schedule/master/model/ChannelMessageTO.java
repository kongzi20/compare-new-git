package com.test.example.schedule.master.model;

import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Channel message 传输对象
 * 
 * @author cg
 * 
 */
public class ChannelMessageTO extends BaseChannelMessage implements Serializable { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3177746250133752781L;

	private String dataQueneKey;
	private String serverNodeId;
	private int threadNum ;
	private Long startKeyCode;
	private Long endKeyCode;

	public ChannelMessageTO() {
		super();
	}
	
	public ChannelMessageTO(String dataQueneKey ,String serverNodeId ,int threadNum ,int type) {
		super(type);
		this.dataQueneKey = dataQueneKey;
		this.serverNodeId = serverNodeId ;
		this.threadNum = threadNum;
	}

	public ChannelMessageTO(String dataQueneKey, String serverNodeId, int threadNum,
			Long startKeyCode, Long endKeyCode , int type ) {
		super();
		this.dataQueneKey = dataQueneKey;
		this.serverNodeId = serverNodeId;
		this.threadNum = threadNum;
		this.startKeyCode = startKeyCode;
		this.endKeyCode = endKeyCode;
		this.setType(type);
	}

	public String getDataQueneKey() {
		return dataQueneKey;
	}

	public void setDataQueneKey(String dataQueneKey) {
		this.dataQueneKey = dataQueneKey;
	}

	public Long getStartKeyCode() {
		return startKeyCode;
	}

	public void setStartKeyCode(Long startKeyCode) {
		this.startKeyCode = startKeyCode;
	}

	public Long getEndKeyCode() {
		return endKeyCode;
	}

	public void setEndKeyCode(Long endKeyCode) {
		this.endKeyCode = endKeyCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getServerNodeId() {
		return serverNodeId;
	}

	public void setServerNodeId(String serverNodeId) {
		this.serverNodeId = serverNodeId;
	} 
	
	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	@Override
	public String toString() {
		try {
			return BeanUtils.describe(this).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
