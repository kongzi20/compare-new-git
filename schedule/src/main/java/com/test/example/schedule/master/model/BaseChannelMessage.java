package com.test.example.schedule.master.model;

import java.io.Serializable;

public class BaseChannelMessage  implements Serializable{
	
	/**
	 * 消息类型：检查线程池
	 */
	public static final int TYPE_CHECK_TP = 1 ;
	
	/**
	 * 消息类型：抓取DB并检查线程池
	 */
	public static final int TYPE_FETCH_AND_CHECK = 2 ;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7365046870203280251L;
	
	private int type ;

	public BaseChannelMessage() {
		super() ;
	}
	
	public BaseChannelMessage(int type) {
		super();
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
