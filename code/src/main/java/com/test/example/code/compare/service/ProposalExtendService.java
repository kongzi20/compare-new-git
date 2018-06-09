package com.test.example.code.compare.service;

import com.test.example.code.compare.model.Proposal;
import com.test.example.code.compare.model.ProposalExtend;

 

public interface ProposalExtendService {
	
	public ProposalExtend getProposalExtendByPrpCode(Long prpCode);
	public Proposal getProposalByCode(Long prpCode) ;
}
