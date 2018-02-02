package com.iris.egrant.code.rule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iris.egrant.code.rule.dao.PrpRuleCheckResultDao;
import com.iris.egrant.code.rule.model.PrpRuleCheckResult;

@Service("ProposalRuleCheckTransService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class ProposalRuleCheckTransServiceImpl implements
		ProposalRuleCheckTransService {

	@Autowired
	private PrpRuleCheckResultDao prpRuleCheckResultDao;
	
	@Override
	public void savePrpRuleCheckResult(PrpRuleCheckResult prpRuleCheckResult) {
		prpRuleCheckResultDao.save(prpRuleCheckResult);
	}

	@Override
	public void deletePrpRuleCheckResult(PrpRuleCheckResult prpRuleCheckResult) {
		prpRuleCheckResultDao.delete(prpRuleCheckResult);
	}
	
	@Override
	public void updatePrpExtendCnt(Long prpCode, String type) {
		prpRuleCheckResultDao.updatePrpExtendCnt(prpCode, type);
	}

	@Transactional(readOnly=true)
	public List<PrpRuleCheckResult> getPrpRuleCheckList(int size) {
		return prpRuleCheckResultDao.getPrpRuleCheckList(size);
	}

}
