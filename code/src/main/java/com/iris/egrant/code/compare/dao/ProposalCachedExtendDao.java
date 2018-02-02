package com.iris.egrant.code.compare.dao;

import org.springframework.stereotype.Repository; 

import com.iris.egrant.code.compare.model.ProposalCachedExtend;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;

/**
 * 申报临时扩展表Dao
 * 
 * @author zxg
 * 
 */
@Repository
public class ProposalCachedExtendDao extends SimpleHibernateDao<ProposalCachedExtend, Long> {

	/**
	 * 根据posCode获取申报书Xml
	 * 
	 * @param posCode
	 * @return
	 */
	public ProposalCachedExtend getPrpInfo(Long posCode) {
		return super.findUniqueBy("posCode", posCode);
	}

	/**
	 * 保存申报书
	 * 
	 * @param prpCachedExtend
	 */
	@Override
	public void save(ProposalCachedExtend prpCachedExtend) {
		super.save(prpCachedExtend);
	}
}
