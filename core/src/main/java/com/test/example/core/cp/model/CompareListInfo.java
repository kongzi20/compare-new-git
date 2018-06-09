package com.test.example.core.cp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMPARE_LIST")
public class CompareListInfo implements Serializable {

	private static final long serialVersionUID = 4131958976797720576L;

	/* 业务主键 */
	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "key_code")
	private Long keyCode;
	
	/* 项目内容 */
	@Column(name = "CONTENT")
	private String content;

	/* 类型 */
	@Column(name = "TYPE")
	private Integer type;
	
	/*数据类型：1项目内容 2可行性报告 3项目名称 4设备清单 5核心团队 6核准制项目信息*/
	@Column(name = "DATA_TYPE")
	private Integer dataType;	
	
	/**
	 * 状态
	 */
	@Column(name="status")
	private Integer status;

	public CompareListInfo() {
		super();
	}

	public CompareListInfo(Long key_code, String content) {
		super();
		this.keyCode = key_code;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(Long keyCode) {
		this.keyCode = keyCode;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
