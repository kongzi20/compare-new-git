package com.test.example.code.rule.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class PrpRuleCheckResultKey implements Serializable {

	private static final long serialVersionUID = -5602246479362841826L;

	private Long prp_code;

	private Long rule_code;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prp_code == null) ? 0 : prp_code.hashCode());
		result = prime * result + ((rule_code == null) ? 0 : rule_code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PrpRuleCheckResultKey other = (PrpRuleCheckResultKey) obj;
		if (prp_code == null) {
			if (other.prp_code != null) {
				return false;
			}
		} else if (!prp_code.equals(other.prp_code)) {
			return false;
		}
		if (rule_code == null) {
			if (other.rule_code != null) {
				return false;
			}
		} else if (!rule_code.equals(other.rule_code)) {
			return false;
		}
		return true;
	}

	public Long getPrp_code() {
		return prp_code;
	}

	public void setPrp_code(Long prp_code) {
		this.prp_code = prp_code;
	}

	public Long getRule_code() {
		return rule_code;
	}

	public void setRule_code(Long rule_code) {
		this.rule_code = rule_code;
	}

}
