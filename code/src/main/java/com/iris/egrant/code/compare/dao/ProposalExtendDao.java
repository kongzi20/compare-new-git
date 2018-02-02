package com.iris.egrant.code.compare.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.iris.egrant.code.compare.model.ProposalExtend;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;

 

/**
 * 申报附属表读取.
 * 
 * @author yamingd
 * 
 */
@Repository
public class ProposalExtendDao extends SimpleHibernateDao<ProposalExtend, Long> {

	/**
	 * 读取业务类别详细信息.
	 * 
	 * @return
	 */
	public ProposalExtend getPrpExtendInfo(Long prpCode) {
		return super.findUniqueBy("prpCode", prpCode);
	}

	/**
	 * 取申请书扩展信息用于同步.
	 * 
	 * @param psnCode
	 * @return
	 */
	public ProposalExtend getProposalExtend4Sync(Long prpCode) {

		return super.findUnique("from ProposalExtend where prpCode = ? ", prpCode);
	}

	/**
	 * 取申请书扩展信息用于同步.
	 * 
	 * @param psnCode
	 * @return
	 */
	public List<ProposalExtend> getProposalExtend4Split() {

		return super.find("from ProposalExtend where compareOrgStatus = 1 ");
	}
	
	public List<Long> getPrpCode4Split() {

		return super.find("select pe.prpCode from ProposalExtend pe where pe.compareOrgStatus = 1 ");
	}

	/**
	 * 保存申请书 扩展信息.
	 * 
	 * @param person
	 */
	public void createProposalExtend4Sync(ProposalExtend proposalExt) {
		this.getSession().merge(proposalExt);
	}

}
