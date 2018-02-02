package com.iris.egrant.code.grantsetting.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务类别扩展表.
 */
@Entity
@Table(name = "FUND_SETTING")
public class FundSetting implements Serializable {

	private static final long serialVersionUID = 2097810619403803503L;

	// 业务类别CODE 主键
	@Id
	private FundSettingKey fundSettingKey;
	@Column(name = "AWARD_AMT_BUDGET")
	private Double awardAmtBudget;

	@Column(name = "AWARD_AMT_ADJUSTED")
	private Double awardAmtAdjusted;
	
	@Column(name = "AWARD_AMT_REALITY")
	private Double awardAmtReality;
	
	@Column(name = "BF_AWARD_AMT_BUDGET")
	private Double bfAwardAmtBudget;
	
	@Column(name = "BF_AWARD_AMT_REALITY")
	private Double bfAwardAmtReality;
	
	@Column(name = "FUND_CODE")
	private Long fundCode;
	
	@Column(name = "AWARD_REMARK")
	private String awardRemark;
	@Column(name = "FUND_SOURCE")
	private String fundSource;
	
	public String getFundSource() {
		return fundSource;
	}

	public void setFundSource(String fundSource) {
		this.fundSource = fundSource;
	}

	public Double getAwardAmtBudget() {
		return awardAmtBudget;
	}

	public void setAwardAmtBudget(Double awardAmtBudget) {
		this.awardAmtBudget = awardAmtBudget;
	}

	public Double getAwardAmtAdjusted() {
		return awardAmtAdjusted;
	}

	public void setAwardAmtAdjusted(Double awardAmtAdjusted) {
		this.awardAmtAdjusted = awardAmtAdjusted;
	}

	public Double getAwardAmtReality() {
		return awardAmtReality;
	}

	public void setAwardAmtReality(Double awardAmtReality) {
		this.awardAmtReality = awardAmtReality;
	}

	public FundSettingKey getFundSettingKey() {
		return fundSettingKey;
	}

	public void setFundSettingKey(FundSettingKey fundSettingKey) {
		this.fundSettingKey = fundSettingKey;
	}

	public Long getFundCode() {
		return fundCode;
	}

	public void setFundCode(Long fundCode) {
		this.fundCode = fundCode;
	}

	public Double getBfAwardAmtBudget() {
		return bfAwardAmtBudget;
	}

	public void setBfAwardAmtBudget(Double bfAwardAmtBudget) {
		this.bfAwardAmtBudget = bfAwardAmtBudget;
	}

	public Double getBfAwardAmtReality() {
		return bfAwardAmtReality;
	}

	public void setBfAwardAmtReality(Double bfAwardAmtReality) {
		this.bfAwardAmtReality = bfAwardAmtReality;
	}

	public String getAwardRemark() {
		return awardRemark;
	}

	public void setAwardRemark(String awardRemark) {
		this.awardRemark = awardRemark;
	}
	
}
