package com.test.example.code.compare.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *
 * @author leijiang
 *
 */
@Entity
@Table(name="compare_source")
public class CompareSource implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5164830619452373836L;

	/**
	 * 主键id
	 */
	@Id
	@Column(name="id")
	private  Long id;

	/**
	 * 业务主键
	 */
	@Column(name="key_code")
	private Long keyCode;

	/**
	 * 业务类型
	 */
	@Column(name="type")
	private Integer type;
//
//	/**
//	 * fileCode
//	 */
//	@Column(name="file_code")
//	private String fileCode;

	/**
	 * 数据来源类型
	 */
	@Column(name="data_type")
	private Integer dataType;

//	/**
//	 * 排序号
//	 */
//	@Column(name="seq_no")
//	private Integer seqNo;

	/**
	 * 创建时间
	 */
	@Column(name="create_date")
	private Date createDate;

	/**
	 * 处理时间
	 */
	@Column(name="complete_date")
	private Date completeDate;

	/**
	 * 数据内容
	 */
	@Column(name="content")
	private String content;

	/**
	 * 状态
	 */
	@Column(name="status")
	private Integer status;

	/**
	 * 错误信息
	 */
	@Column(name="err_msg")
	private String errMsg;

	@Column(name="form_code")
	private Long formCode;

	public Long getFormCode() {
		return formCode;
	}

	public void setFormCode(Long formCode) {
		this.formCode = formCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(Long keyCode) {
		this.keyCode = keyCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}



	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

}
