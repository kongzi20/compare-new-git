package com.iris.egrant.code.compare.model;

import java.io.Serializable;
import java.util.List;

import com.iris.egrant.core.cp.model.CompareListInfo;

/**
 * 值对象：封装同一key_code以及compare_list
 * @author cg
 *
 */
public class PrpCompareListVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5715117319408367432L;

	/**
	 * 
	 */
	 

	private Long keyCode ;
	
  private List<CompareListInfo>  compareLists;
  
	public PrpCompareListVO() {
		super();
	}

	public PrpCompareListVO(Long keyCode, List<CompareListInfo> list) {
			this.keyCode = keyCode;
			this.compareLists = list ;
	}

	public Long getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(Long keyCode) {
		this.keyCode = keyCode;
	}

	public List<CompareListInfo> getCompareLists() {
		return compareLists;
	}

	public void setCompareLists(List<CompareListInfo> compareLists) {
		this.compareLists = compareLists;
	}
 
}
