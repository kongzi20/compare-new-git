package com.test.example.code.grantsetting.dao;

import org.springframework.stereotype.Repository;

import com.test.example.code.grantsetting.model.GrantSetting;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;

/**
 * 业务类别操作DAO.
 * 
 * @author zxg
 * 
 */
@Repository("grantSettingDao")
public class GrantSettingDao extends SimpleHibernateDao<GrantSetting, Long> {/*

	*//**
	 * 获取第一级资助类别列表
	 * 
	 * @return
	 *//*

	public List<GrantSetting> getGrantList() {

		String hql = " from GrantSetting where   enabled=1 and parentCode is null order by grantNo,seqNo";
		Query query = getSession().createQuery(hql);

		@SuppressWarnings("unchecked")
		List<GrantSetting> list = query.list();
		return list;
	}

	*//**
	 * 通过父节点获取子资助类别列表
	 * 
	 * @return
	 *//*

	public List<GrantSetting> getGrantSettingListByParentCodeNo(Long parentCode) {
		String hql = "from GrantSetting where   enabled=1 and parentCode=? order by grantNo,seqNo";
		List<GrantSetting> gsList = this.find(hql, new Object[] { parentCode });
		return gsList;
	}

	*//**
	 * 判断资助类别是否被开通
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws HibernateException
	 *//*

	public int getGrantEnabled(List<Long> grantList) {

		String hql = " from GrantSetting where grantCode in (:grantCodeList) and enabled ='0'";
		Query query = getSession().createQuery(hql);
		query.setParameterList("grantCodeList", grantList);

		@SuppressWarnings("unchecked")
		List<GrantSetting> list = query.list();
		return list.size();
	}

	*//**
	 * 通过grantNo读取资助类别
	 * 
	 * @return
	 */ 
	public GrantSetting getGrantInfoByGrantNo(String grantNo) {

		return findUniqueBy("grantNo", grantNo);
	}

/**
	 * 读取业务类别列表.
	 * 
	 * @return
	 *//*

	public List<Long> getGrantCodes(Long grantCode) {

		// grantLevel 3是代表组织间合作协议和非组织间合作协议，这个不属于真实资助类别
		GrantSetting gs = super.get(Long.valueOf(grantCode));
		String hql = " select grantCode from GrantSetting where grantNo like :grantNo  and grantLevel<>3 and enabled=1 order by grantNo,seqNo";
		Query query = getSession().createQuery(hql);
		query.setParameter("grantNo", gs.getGrantNo() + "%");
		@SuppressWarnings("unchecked")
		List<Long> list = query.list();
		return list;
	}

	*//**
	 * 根据父资助类别读取子资助类别列表
	 * 
	 * @param grantCode
	 * @return
	 *//*
	public List<GrantSetting> getGrantListByParentCode(String grantCode) {

		if (!NumberUtils.isDigits(grantCode)) {
			return new ArrayList<GrantSetting>();
		}
		return super.findBy("parentCode", Long.valueOf(grantCode));
	}

	public long getCountForChild(Long grantCode) {
		String hql = " from GrantSetting where  enabled=1 and parentCode=?";
		return super.countHqlResult(hql, new Object[] { grantCode });
	}

	*//**
	 * Des: 查询所用顶级业务类别.<br>
	 * Arlon.Yang created this method at 2011-8-24
	 *//*
	public PageContainer getTopGrantList(ConditionContainer conditionContainer) {
		Map<String, Object> conditionsMap = conditionContainer.getConditions();
		PageQueryHanlder pageQuery = new PageQueryHanlder();

		StringBuffer sb = new StringBuffer();
		sb.append("select  gs.grant_code as grant_code, gsc.gs_code as gs_code, gs.zh_cn_grant_name as grant_name, gs.grant_no as grant_no, to_char(gsc.end_date,'yyyy-mm-dd hh24:mi') as end_date, to_char(gsc.start_date,'yyyy-mm-dd hh24:mi') as start_date ");
		sb.append(" from grant_setting gs left outer join grant_schedule gsc on gs.grant_code=gsc.grant_code ");
		if (conditionsMap.containsKey("parentCode")) {
			String parentCode = (String) conditionsMap.get("parentCode");
			if ("null".equals(parentCode)) {
				sb.append("where gs.parent_code is null");
			} else if (NumberUtils.isDigits(parentCode)) {
				sb.append("where gs.parent_code = ?");
				pageQuery.addParam(Long.valueOf(parentCode));
			} else {
				sb.append("where gs.parent_code is not null");
			}

			if (!"null".equals(parentCode)
					&& SecurityUtils.getCurrentUserRoleId() != RoleConstants.INFORMATION_CENTER_INT) {// 除了信息中心，其他处室查询有管理权限的类别
				Long orgCode = SecurityUtils.getCurrentOrgCode();
				sb.append(" and gs.default_admin_org_code = ?");
				pageQuery.addParam(orgCode);
			}
		}
		pageQuery.setQlMode(PageQueryHanlder.MODEL_SQL);
		pageQuery.addQlSegment(sb.toString());
		PageContainer p = pageQuery.loadOut(this, conditionContainer);
		return p;
	}

	*//**
	 * Des: 根据grantCode查询GrantSchedule.<br>
	 * Arlon.Yang created this method at 2011-8-24
	 *//*
	@SuppressWarnings("unchecked")
	public GrantSetting getGrantSettingByGrantCode(String grantCode) {
		if (grantCode == null || "".equals(grantCode.trim())) {
			return null;
		}
		String hql = "select p from GrantSetting p left join fetch p.grantSchedule pc where p.grantCode = ?";
		List<GrantSetting> grantSettings = this.getSession().createQuery(hql).setString(0, grantCode).list();
		if(grantSettings.size() > 0) {
			return grantSettings.get(0);
		}
		return null;
	}

	*//**
	 * Des: 根据grantCode查询下一级业务类别.<br>
	 * Arlon.Yang created this method at 2011-8-25
	 *//*
	@SuppressWarnings("unchecked")
	public List<GrantSetting> getChildrenByGrantCode(String grantCode) {
		if (grantCode == null || "".equals(grantCode.trim())) {
			return null;
		}
		String hql = "select p from GrantSetting p left join fetch p.grantSchedule pc where p.parentCode = ?";
		List<GrantSetting> grantSettings = this.getSession().createQuery(hql).setString(0, grantCode).list();
		return grantSettings;
	}

	*//**
	 * Des: 更新起止时间.<br>
	 * 
	 * @param grantCode
	 * @param startTime
	 * @param endTime
	 * @param subList
	 *            Arlon.Yang created this method at 2011-8-25
	 *//*
	@SuppressWarnings("unchecked")
	public void updatexampleSchedule(String grantCode, String startTime, String endTime) {
		if (grantCode != null && !"".equals(grantCode.trim())) {
			String hql = " from GrantSchedule where grantCode=?";
			List<GrantSchedule> list = this.getSession().createQuery(hql).setString(0, grantCode).list();
			if (!CollectionUtils.isEmpty(list)) {
				GrantSchedule gs = list.get(0);
				gs.setGrantCode(new Long(grantCode));
				gs.setStartDate(DateUtils.toDate(startTime, DateFormator.YEAR_MONTH_DAY));
				gs.setEndDate(DateUtils.toDate(endTime, DateFormator.YEAR_MONTH_DAY));
				gs.setUpdateDate(DateUtils.now());
				gs.setUpdatePsnCode(SecurityUtils.getCurrentUserId());
				this.getSession().update(gs);
			} else {
				GrantSchedule gs = new GrantSchedule();
				gs.setGrantCode(new Long(grantCode));
				gs.setStartDate(DateUtils.toDate(startTime, DateFormator.YEAR_MONTH_DAY));
				gs.setEndDate(DateUtils.toDate(endTime, DateFormator.YEAR_MONTH_DAY));
				gs.setUpdateDate(DateUtils.now());
				gs.setUpdatePsnCode(SecurityUtils.getCurrentUserId());
				this.getSession().save(gs);
			}
		}
	}

	*//**
	 * 根据 grantcode 取其子类别个数
	 * 
	 * @param grantCode
	 * @return
	 *//*
	public Long getChildrenGrantCount(String grantCode) {
		if (grantCode == null || "".equals(grantCode.trim())) {
			return null;
		}
		String hql = "select count(p) from GrantSetting p where p.parentCode = ?";
		Object grantCount = this.getSession().createQuery(hql).setLong(0, Long.valueOf(grantCode)).uniqueResult();
		return (Long) grantCount;
	}

	*//**
	 * 获取所有的业务类别
	 * 
	 * @return
	 * @throws ServiceException
	 *//*
	@SuppressWarnings("unchecked")
	public List<GrantSetting> getAllGrantList() throws DaoException {

		StringBuilder sb = new StringBuilder();
		sb.append("select gs from  GrantSetting gs where gs.enabled=1 order by gs.grantNo");
		return super.getSession().createQuery(sb.toString()).list();

	}

	public PageContainer list4limitbyGrant(ConditionContainer conditionContainer) {
		Map<String, Object> conditionsMap = conditionContainer.getConditions();
		PageQueryHanlder pageQuery = new PageQueryHanlder();

		StringBuffer sb = new StringBuffer();
		sb.append("select gl.id,gs.grant_code,gs.zh_cn_grant_name grant_name, org.name org_name,gl.stat_year,gl.num from grant_limit gl,organization org,grant_setting gs ");
		sb.append(" where gl.org_code=org.org_code and gl.grant_code=gs.grant_code ");
		if (conditionsMap.containsKey("grantCode")) {
			String grantCode = (String) conditionsMap.get("grantCode");
			sb.append(" and gs.grant_code =? ");
			pageQuery.addParam(Long.valueOf(grantCode));

		}
		pageQuery.setQlMode(PageQueryHanlder.MODEL_SQL);
		pageQuery.addQlSegment(sb.toString());
		PageContainer p = pageQuery.loadOut(this, conditionContainer);
		return p;
	}

	*//**
	 * 获取所有最底级业务类别
	 * *//*
	@SuppressWarnings("unchecked")
	public List<GrantSetting> getAllLeafGrantList() throws DaoException {
		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		
		sb.append("select gs from  GrantSetting gs where gs.enabled=1 and gs.prpFormId<>0 and (gs.grantNo<>'00' or gs.grantNo is not null)");
		if (SecurityUtils.isAuthority("A_PRIVILEGE_XK")) {// 处室
			sb.append(" and gs.defaultAdminOrgCode = ?");
			params.add(SecurityUtils.getCurrentOrgCode());
		}
		sb.append(" order by gs.zhCnGrantName");
		return super.createQuery(sb.toString(), params).list();
	}

	public Long getParentGrantCode(String parentName) {
		
		String sql = "select gs.grant_code from grant_setting gs where gs.zh_cn_grant_name=? and gs.grant_level=1 and rownum<=1";
		Object obj = super.getSession().createSQLQuery(sql).setString(0, parentName).uniqueResult();
		if (obj != null) {
			Long grantCode = Long.valueOf(obj.toString());
			return grantCode;
		}
		return null;
	}

	public Long getGrantCodes(String grantName) {
		String sql = "select gs.grant_code from grant_setting gs where gs.zh_cn_grant_name=? and gs.grant_level=2 and rownum<=1";
		Object obj = super.getSession().createSQLQuery(sql).setString(0, grantName).uniqueResult();
		if (obj != null) {
			Long grantCode = Long.valueOf(obj.toString());
			return grantCode;
		}
		return null;
	}

	public Long isExistGrantCodes(Long parentCode, Long grantCode) {
		String sql = "select gs.grant_code from grant_setting gs where gs.parent_code=? and gs.grant_code=?";
		Object obj = super.getSession().createSQLQuery(sql)
				.setLong(0, parentCode)
				.setLong(1, grantCode)
				.uniqueResult();
		if (obj != null) {
			Long Code = Long.valueOf(obj.toString());
			return Code;
		}
		return null;
	}

*/}
