package com.test.example.code.grantsetting.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class FundSettingKey implements Serializable {
	private static final long serialVersionUID = -3236523319933461469L;
	private Long stat_year;
	private Long grant_code;
	private String payment_flag;

	public Long getStat_year() {
		return stat_year;
	}

	public void setStat_year(Long stat_year) {
		this.stat_year = stat_year;
	}

	public Long getGrant_code() {
		return grant_code;
	}

	public void setGrant_code(Long grant_code) {
		this.grant_code = grant_code;
	}

	public String getpayment_flag() {
		return payment_flag;
	}

	public void setpayment_flag(String payment_flag) {
		this.payment_flag = payment_flag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grant_code == null) ? 0 : grant_code.hashCode());
		result = prime * result + ((stat_year == null) ? 0 : stat_year.hashCode());
		result = prime * result + ((payment_flag == null) ? 0 : payment_flag.hashCode());
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
		FundSettingKey other = (FundSettingKey) obj;
		if (grant_code == null) {
			if (other.grant_code != null) {
				return false;
			}
		} else if (!grant_code.equals(other.grant_code)) {
			return false;
		}
		if (stat_year == null) {
			if (other.stat_year != null) {
				return false;
			}
		} else if (!stat_year.equals(other.stat_year)) {
			return false;
		}
		if (payment_flag == null) {
			if (other.payment_flag != null) {
				return false;
			}
		} else if (!payment_flag.equals(other.payment_flag)) {
			return false;
		}
		return true;
	}

}
