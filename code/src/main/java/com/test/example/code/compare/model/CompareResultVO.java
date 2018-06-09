package com.test.example.code.compare.model;

import java.io.Serializable;

/**
 * 值对象： 相似度比较
 * 
 * @author cg
 *
 */
public class CompareResultVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -933732027428319104L;

	private Long resultId;
	private Long sourceId;
	private Long targetId;
	private Long dataType;
	private String sourceContent;
	private String targetContent;

	public CompareResultVO() {
		super();
	}

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getDataType() {
		return dataType;
	}

	public void setDataType(Long dataType) {
		this.dataType = dataType;
	}

	public String getSourceContent() {
		return sourceContent;
	}

	public void setSourceContent(String sourceContent) {
		this.sourceContent = sourceContent;
	}

	public String getTargetContent() {
		return targetContent;
	}

	public void setTargetContent(String targetContent) {
		this.targetContent = targetContent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
 
}
