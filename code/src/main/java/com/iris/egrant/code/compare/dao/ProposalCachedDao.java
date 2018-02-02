package com.iris.egrant.code.compare.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.iris.egrant.code.compare.model.ProposalCached;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao; 

/**
 * 申报临时表Dao
 * 
 * @author zxg
 * 
 */
@Repository
public class ProposalCachedDao extends SimpleHibernateDao<ProposalCached, Long> {
	 
	/**
	 * 找到指定person为指定orgCode单位填写的status状态的缓存proposal的名字
	 * @author zhoujianhui
	 * @param psnCode
	 * @param orgCode
	 * @param status
	 * @return
	 */
	public List<Object[]> findProCacheNameByPsnOrgCodeAndStatus(Long psnCode, Long orgCode, List<String> status) {
		String hql = "select p.zhTitle as zhTitle, p.status as status from  ProposalCached p where p.psnCode= ? and p.orgCode=? and p.status in(:status)";
		Query query = this.createQuery(hql, psnCode, orgCode).setParameterList("status", status);
		return query.list();
	}

	/**
	 * 将指定person的指定status状态的缓存的proposal置为删除状态
	 * 
	 * @param psnCode
	 * @param status
	 */
	public void setProCacheDelStatusBypsnCodeAndStatus(Long psnCode, String status) {
		String hql = "update  ProposalCached p  set p.status = '99' where p.psnCode= ? and p.status = ?";
		Query query = this.createQuery(hql, psnCode, status);
		query.executeUpdate();

	}

	/**
	 * 查询申报人所有指定状态的申请书
	 * */
	@SuppressWarnings("unchecked")
	public List<ProposalCached> findProposalCached(Long psnCode, List<String> prpStatus) {
		return createQuery("from ProposalCached where psnCode=:psnCode and status not in (:status)")
				.setLong("psnCode", psnCode).setParameterList("status", prpStatus).list();
	}

	/**
	 * 查询申报人为单位填报的某种业务类别的指定状态的申请书
	 * */
	@SuppressWarnings("unchecked")
	public List<ProposalCached> findProposalCached(Long orgCode, Long psnCode, Long grantCode, List<String> prpStatus) {
		return createQuery(
				"from ProposalCached where orgCode=:orgCode and psnCode=:psnCode and grantCode=:grantCode and status not in (:status)")
				.setLong("orgCode", orgCode).setLong("psnCode", psnCode).setLong("grantCode", grantCode)
				.setParameterList("status", prpStatus).list();
	}
	/**
	 * 找出符合条件的proposal_cached表中proposal的pos_code
	 * 
	 * @author zhoujianhui
	 * @param psnCode
	 * @param orgCode
	 * @param grantCode
	 * @param status
	 * @return  
	 */
	public List<Object> findPosCodByPsnOrgGrantCodeAndProStatus(Long psnCode, Long orgCode, Long grantCode, List<String>status){
		String hqlWithGrantCode = "select p.posCode as posCode from ProposalCached p where p.psnCode = ? and p.orgCode = ? and p.grantCode = ? and p.status in (:status)";
		String hqlWithOutGrantCode = "select p.posCode as posCode from ProposalCached p where p.psnCode = ? and p.orgCode = ? and p.status in (:status)";
		List<Object> results = (grantCode == null ?  this.createQuery(hqlWithOutGrantCode, psnCode, orgCode).setParameterList("status", status).list() : this.createQuery(hqlWithGrantCode, psnCode, orgCode, grantCode).setParameterList("status", status).list());
		return results;
	}
}
