package com.iris.egrant.code.compare.service;

import com.iris.egrant.code.compare.model.Proposal;
import com.iris.egrant.code.compare.model.ProposalExtend;

 

public interface ProposalExtendService {
	
	public ProposalExtend getProposalExtendByPrpCode(Long prpCode);
	public Proposal getProposalByCode(Long prpCode) ;
}
