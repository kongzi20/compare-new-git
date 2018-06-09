package com.test.example.code.compare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.compare.dao.ProposalDao;
import com.test.example.code.compare.dao.ProposalExtendDao;
import com.test.example.code.compare.model.Proposal;
import com.test.example.code.compare.model.ProposalExtend;



@Service("proposalExtendService")
@Transactional(rollbackFor = Exception.class)
public class ProposalExtendServiceImpl  implements ProposalExtendService{
	@Autowired
	private ProposalExtendDao proposalExtendDao;
	@Autowired
	private ProposalDao proposalDao;
	@Override
	public ProposalExtend getProposalExtendByPrpCode(Long prpCode) {
		return proposalExtendDao.get(prpCode);
	}
	@Override
	public Proposal getProposalByCode(Long prpCode) {
		return proposalDao.get(prpCode);
	}
}
