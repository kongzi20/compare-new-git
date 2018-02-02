/**
 * 
 */
package com.iris.egrant.code.compare.model;

import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;

/**
 *   值对象 项目比对结果同步
 * @author cg
 *
 */
public class CompareResultSyncVO implements Serializable {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -804497745742948694L;
	
	private String prpCode ;
	
	private String type ;

	public CompareResultSyncVO() {
	 super() ;
	}

	public String getPrpCode() {
		return prpCode;
	}

	public void setPrpCode(String prpCode) {
		this.prpCode = prpCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
