package com.iris.egrant.code.compare.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

/**
 * 申报临时扩展表.
 * 
 * @author zxg
 * 
 */
@Entity
@Table(name = "PROPOSAL_CACHED_EXTEND")
public class ProposalCachedExtend implements Serializable {

	private static final long serialVersionUID = 4563769803800825065L;

	// 申报临时表code
	@Id
	@Column(name = "POS_CODE")
	private Long posCode;

	// 申请书XML
	@Column(name = "PRP_XML")
	@Type(type = "com.iris.egrant.core.dao.orm.OracleXmlType")
	private Document prpXml;

	public Long getPosCode() {
		return posCode;
	}

	public void setPosCode(Long posCode) {
		this.posCode = posCode;
	}

	public Document getPrpXml() {
		return prpXml;
	}

	public void setPrpXml(Document prpXml) {
		this.prpXml = prpXml;
	}

}
