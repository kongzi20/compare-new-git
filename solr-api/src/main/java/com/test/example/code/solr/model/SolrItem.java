
package com.test.example.code.solr.model;

import org.apache.solr.client.solrj.beans.Field;

/**
 * solrbean
 * @author Administrator
 *
 */
public class SolrItem {

	// 主键
	@Field
	private String id;
	// 项目名称
	@Field
	private String content;
	// 表单类型
	@Field
	private String type;
	//系统类型
	@Field
	private String systype;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSysType() {
		return systype;
	}
	public void setSystype(String systype) {
		this.systype = systype;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}