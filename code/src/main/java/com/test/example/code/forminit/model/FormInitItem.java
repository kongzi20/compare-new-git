package com.test.example.code.forminit.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The persistent class for the FORM_INIT_ITEM database table.
 * 
 */
@Entity
@Table(name = "FORM_INIT_ITEM")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class FormInitItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键.
	 */
	@Id
	@Column(name = "ITEM_CODE", precision = 4)
	private Long itemCode;

	/**
	 * 刷新类型SQL，XML,为空代表不需刷新.
	 */
	@Column(name = "TYPE", length = 20)
	private String type;

	/**
	 * SQL语句，刷新方式为XML时返回一个XML字符串，其他时候返回正常结果集
	 */
	@Column(name = "CONTENT", length = 500)
	private String content;

	/**
	 * 分页面排列顺序.
	 */
	@Column(name = "SEQ_NO", precision = 3)
	private Long seqNo;

	/**
	 * xml刷新级别 0-不用刷新 1-add 2-edit 3-view.
	 */
	@Column(name = "INIT_FLAG", precision = 1)
	private Long initFlag;

	/**
	 * 申请书xml里需要刷新的节点.
	 */
	@Column(name = "XPATH", length = 100)
	private String xpath;

	/**
	 * 节点名称，SQL导入的方式为返回的记录集每行的节点名,且上层节点名为node+‘s’,为空代表只刷新xpath节点 XML方式导入时为从XML中取出的节点的Xpath.
	 */
	@Column(name = "NODE", length = 20)
	private String node;

	public Long getItemCode() {
		return itemCode;
	}

	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public Long getInitFlag() {
		return initFlag;
	}

	public void setInitFlag(Long initFlag) {
		this.initFlag = initFlag;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

}