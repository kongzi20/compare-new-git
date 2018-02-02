package com.iris.egrant.code.compare.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "COMPARE_RESULT")
public class CompareResult implements Serializable {

	private static final long serialVersionUID = 5219052956756505071L;
	/* 状态：出错 */
	public static int STATUS_ERROR = 2;
	/* 状态：已处理 */
	public static int STATUS_PROCESSED = 1;
	/* 状态：未处理 */
	public static int STATUS_NO_PROCESS = 0;
	/* 每次抓取多少条数据 */
	public static int FETCH_SIZE = 200;

	/* 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COMPARE_RESULT")
	@GenericGenerator(name = "SEQ_COMPARE_RESULT", strategy = "com.iris.egrant.core.dao.hibernate.AssignedSequenceGenerator", parameters = { @Parameter(name = "sequence", value = "SEQ_COMPARE_RESULT") })
	@Column(name = "ID")
	private Long id;

	/* 业务主键 */
	@Column(name = "SOURCE_ID")
	private Long sourceID;

	/* 业务主键 */
	@Column(name = "TARGET_ID")
	private Long targetID;

	/* 相似度值 */
	@Column(name = "RESULT")
	private Double compareResult;

	/* 状态，0待处理，1已处理 ，2出错 */
	@Column(name = "STATUS")
	private Integer status = 0;

	/* 耗时 */
	@Column(name = "RUN_TIME")
	private Long runTime;

	/* 请求时间 */
	@Column(name = "REQUEST_DATE")
	private Date requestDate;

	/* 完成时间 */
	@Column(name = "COMPLETE_DATE")
	private Date completeDate;

	@Column(name = "ERR_MSG")
	private String errMsg;

	@Column(name = "DATA_TYPE")
	private Integer dataType;	//数据类型：1项目内容 2可行性报告 3项目名称 4设备清单 5核心团队 6核准制项目信息

	@Column(name = "TYPE")
	private Integer type;	//类型，1申报书

	public CompareResult() {
		super();
	}

	public Double getCompareResult() {
		return compareResult;
	}

	public void setCompareResult(Double compareResult) {
		this.compareResult = compareResult;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getSourceID() {
		return sourceID;
	}

	public void setSourceID(Long sourceID) {
		this.sourceID = sourceID;
	}

	public Long getTargetID() {
		return targetID;
	}

	public void setTargetID(Long targetID) {
		this.targetID = targetID;
	}


	public Long getRunTime() {
		return runTime;
	}

	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}