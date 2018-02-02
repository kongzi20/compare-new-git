package com.iris.egrant.schedule.master.model;

/**
 * 值对象：待处理数据汇总统计 
 * 
 * @author cg
 * 
 */
public class DataSummaryVO {

	private Long minKeyCode;
	private Long maxKeyCode;
	private Long count;

	public Long getMinKeyCode() {
		return minKeyCode;
	}

	public void setMinKeyCode(Long minKeyCode) {
		this.minKeyCode = minKeyCode;
	}

	public Long getMaxKeyCode() {
		return maxKeyCode;
	}

	public void setMaxKeyCode(Long maxKeyCode) {
		this.maxKeyCode = maxKeyCode;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
