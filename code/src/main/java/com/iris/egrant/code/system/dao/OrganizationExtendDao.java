package com.iris.egrant.code.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.iris.egrant.code.system.model.OrganizationExtend;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;

/**
 * 单位详细信息.
 * 
 * 
 * @version $Rev$ $Date$
 */
@Repository
public class OrganizationExtendDao extends SimpleHibernateDao<OrganizationExtend, Long> {
	@Override
	public void save(OrganizationExtend orgExtend) {
		super.save(orgExtend);
	}

	/**
	 * 获取单位详细信息.
	 */
	public OrganizationExtend getOrgExtendInfo(Long orgCode) {
		List<OrganizationExtend> list = this.getOrgExtendList(orgCode);
		if (list != null && list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取最大的版本号
	 */
	public Long getMaxVerson() {

		return super.queryForLong("select max(org_verson) from organization_extend");
	}

	/**
	 * 根据orgCode获取单位信息的数组
	 * 
	 * @param orgCode
	 * @return
	 */
	public List<OrganizationExtend> getOrgExtendList(Long orgCode) {
		List<OrganizationExtend> orgExtendList = super.find(
				"from OrganizationExtend where orgCode=? order by orgVerson desc", orgCode);
		return orgExtendList;
	}
}
