package com.test.example.code.compare.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMPARE_TEMPLATE_SETTING")
public class CompareTemplateSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1473561022599292720L;

	/**
	 * 主键id
	 */
	@Id
	@Column(name="ID")
	private  Long id;
	
	/**
	 *  类型
	 */
	@Column(name = "TYPE")
	private Integer type;

	/**
	 * 数据类型
	 */
	@Column(name="DATA_TYPE")
	private Integer dataType;

	/**
	 * 实现类BeanName
	 */
	@Column(name="TEMPLATE_BEAN_NAME")
	private String templateBeanName;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getTemplateBeanName() {
		return templateBeanName;
	}

	public void setTemplateBeanName(String templateBeanName) {
		this.templateBeanName = templateBeanName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}