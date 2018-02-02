package com.iris.egrant.code.system.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iris.egrant.code.compare.dao.ProposalCachedDao;
import com.iris.egrant.code.compare.model.ProposalCached;
import com.iris.egrant.code.system.constant.RoleConstants;
import com.iris.egrant.code.system.model.Department;
import com.iris.egrant.code.system.model.Organization;
import com.iris.egrant.code.system.model.Person;
import com.iris.egrant.code.system.model.Person4Sync;
import com.iris.egrant.code.system.model.PersonBaseInfo;
import com.iris.egrant.code.system.model.PersonExtend;
import com.iris.egrant.code.system.model.PersonSubInfo4Sync;
import com.iris.egrant.code.system.model.SimpleRole;
import com.iris.egrant.code.system.model.UserRole;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.utils.CollectionUtils;
import com.iris.egrant.core.utils.IrisStringUtils;

/**
 * 个人信息DAO.
 * 
 * 
 * 
 */
@Repository
public class PersonDao extends SimpleHibernateDao<Person, Long> {
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private ProposalCachedDao proposalCachedDao;

	// protected List<String> prpStatus = new ArrayList<String>(); // 可删除的申请书状态

	public PersonDao() {
		super();
		// prpStatus.add(ProposalConstants.PRP_EDIT); // 填写中
		// prpStatus.add(ProposalConstants.PRP_HAS_BACK); // 退回修改
		// prpStatus.add(ProposalConstants.PRP_HAS_DELETE); // 已经删除
	}

	/**
	 * 根据证件类型ID获取证件类型名称
	 * 
	 * @param cardId
	 * @return
	 */
	public String getCardName(Integer cardId) {
		String sql = "select zh_cn_caption from const_dictionary where code=? and category='id_type'";
		return (String) this.getSession().createSQLQuery(sql).setInteger(0, cardId).uniqueResult();
	}

	/**
	 * 更新人员生日信息
	 * 
	 * @param psnCode
	 * @param birthday
	 */
	public void updatePersonBirthday(Long psnCode, Date birthday) {
		String sql = "update person_sub set birthday=? where psn_code=?";
		this.getSession().createSQLQuery(sql).setDate(0, birthday).setLong(1, psnCode).executeUpdate();
	}

	// ********************************used for sync db*****************************************************
	/**
	 * service for synchro data.
	 * 
	 * @param psnId
	 */
	public void markSynSuccess(Long psnId) {
		String sql = "update person_syn set flag = 1 where psn_code = ?";
		this.getSession().createSQLQuery(sql).setLong(0, psnId).executeUpdate();
	}

	public void stopSyn(Long psnId) {
		String sql = "update person_syn set flag = 2 where psn_code = ?";
		this.getSession().createSQLQuery(sql).setLong(0, psnId).executeUpdate();
	}

	/**
	 * service for synchro data.
	 * 
	 * @param psnId
	 */
	public boolean isUpdatedState(Long psnId) {
		String sql = "select count(*) from person_syn where flag = 0 and psn_code = ?";
		Object obj = this.getSession().createSQLQuery(sql).setLong(0, psnId).uniqueResult();
		BigDecimal count = (BigDecimal) obj;
		return count.longValue() == 1L;
	}

	/**
	 * fetch psncodes wait to sync
	 * 
	 * @author lineshow created on 2011-10-26
	 * @param startId
	 * @param size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BigDecimal> getsyncablePsnCodes(Long startId, int size) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.psn_code from person p where  p.psn_code in ( ");
		sql.append("select ps.psn_code from person_syn ps where ps.psn_code > ? and ps.flag = 0 ");
		sql.append(" )");
		return this.getSession().createSQLQuery(sql.toString()).setLong(0, startId).setMaxResults(size).list();
	}

	/**
	 * fetch person all-around info
	 * 
	 * @author lineshow created on 2011-10-26
	 * @param psnCodes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Person> getPersonInfoInBatch4Sync(List<Long> psnCodes) {
		StringBuilder hql = new StringBuilder();
		hql.append("from Person p left join fetch p.psnExt where p.enable = true and p.psnCode in(:psncode)");
		List<Person> persons = this.getSession().createQuery(hql.toString()).setParameterList("psncode", psnCodes)
				.list();
		for (Person person : persons) {
			try {
				Hibernate.initialize(person.getOrgs());
			} catch (HibernateException e) {
				person.setOrgs(new HashSet<Organization>());
			}
			try {
				Hibernate.initialize(person.getChargOrgs());
			} catch (HibernateException e) {
				person.setChargOrgs(new HashSet<Organization>());
			}
			try {
				Hibernate.initialize(person.getChargDepts());
			} catch (HibernateException e) {
				person.setChargDepts(new HashSet<Department>());
			}
			try {
				Hibernate.initialize(person.getRoles());
			} catch (HibernateException e) {
				person.setRoles(new HashSet<SimpleRole>());
			}
		}
		return persons;
	}

	// ********************************used for sync db*****************************************************

	/**
	 * exist person have same property.
	 * 
	 * @param person
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Person existConflictPsn(Person person) {
		StringBuilder hql = new StringBuilder();
		hql.append("from Person  p where  p.psnCode <> ? and p.cardType = ? and p.cardCode = ? and p.enable = true");
		List<Person> personList = this.getSession().createQuery(hql.toString()).setLong(0, person.getPsnCode())
				.setInteger(1, person.getCardType()).setString(2, person.getCardCode()).list();
		return personList.isEmpty() ? null : personList.get(0);
	}

	/**
	 * save persons in bulk.
	 * 
	 * @param list
	 */
	public void savePsns(List<Person> list) {
		for (Person person : list) {
			save(person);
		}
	}

	@Override
	public void save(Person person) {
		this.getSession().save(person);
		PersonSubInfo4Sync personSub = getPersonSubInfo4Sync(person.getPsnCode());
		if (personSub == null) {
			personSub = new PersonSubInfo4Sync(person.getPsnCode());
			personSub.setValidCard(false);
			personSub.setValidEmail(false);
			personSub.setValidMobile(false);
			this.getSession().save(personSub);
		}
	}

	/**
	 * 通过用户名、EMAIL获取用户.
	 * 
	 * @param name
	 * @param email
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Person> getPerson(String name, String email) {
		List<Object> params = new ArrayList<Object>();
		params.add(name);
		params.add(email);
		return this.createQuery("select p from Person p,PersonSubInfo4Sync p1 where p1.psnCode=p.psnCode and p1.validEmail=1 and trim(p.zhName) = ? and lower(p.email) = ? and p.enable = true and nvl(p.status,'A') <> 'D'", params)
				.list();
	}
	/**
	 * load all info about person at once ,note! return data only be readed.
	 * 
	 * @param psnId
	 * @return
	 */
	public Person getPersonDetail(Long psnId) {
		Person person = this.get(psnId);
		if (person == null) {
			return person;
		}
		Hibernate.initialize(person.getRoles());
		try {
			Hibernate.initialize(person.getPsnExt());
			PersonExtend pe = person.getPsnExt();
			this.getSession().setReadOnly(pe, true);
		} catch (ObjectNotFoundException e) {
			person.setPsnExt(null);
		}
		Hibernate.initialize(person.getOrgs());
		Hibernate.initialize(person.getDepts());
		return person;
	}

	/**
	 * 保存人员.
	 * 
	 * @param person
	 */
	public void createPersonInfo(Object person) {
		this.getSession().merge(person);
	}

	/**
	 * 取人员信息用于同步.
	 * 
	 * @param psnCode
	 * @return
	 */
	// TODO：这个代码是否应该移植到Person4SyncDao中去，尽量代码能够清晰.(修改好了把TODO删除)lqh add.
	public Person4Sync getPerson4Sync(Long psnCode) {
		Object obj = this.getSession().get(Person4Sync.class, psnCode);
		return obj == null ? null : (Person4Sync) obj;
	}

	/**
	 * 取人员从属信息表用于同步.
	 * 
	 * @param psnCode
	 * @return
	 */
	// TODO：这个代码是否应该移植到Person4SyncDao中去，尽量代码能够清晰.(修改好了把TODO删除)lqh add.
	public PersonSubInfo4Sync getPersonSubInfo4Sync(Long psnCode) {
		Object obj = this.getSession().get(PersonSubInfo4Sync.class, psnCode);
		return obj == null ? null : (PersonSubInfo4Sync) obj;
	}

	/**
	 * 设置人员成为专家，已经存在则返回.
	 * 
	 * @param psnCode
	 */
	public void setPerson2Reviewer(Long psnCode, Map<String, Object> map) {
		String sql = "select count(*) from evreviewer where psn_code = ?";
		String pwd = IrisStringUtils.toString(map.get("encrypt_pdfpass"));
		String istrain = IrisStringUtils.toString(map.get("istrain"));
		String issecond = IrisStringUtils.toString(map.get("expert"));
		String isoversea = IrisStringUtils.toString(map.get("is_overseas_expert"));
		Object count = this.getSession().createSQLQuery(sql).setLong(0, psnCode).uniqueResult();
		if (!"0".equals(count.toString())) {
			sql = "update  evreviewer set pwd=?,istalentpsn=?,issecond=?,isoversea=? where psn_code=?";
			this.getSession().createSQLQuery(sql).setString(0, pwd).setString(1, istrain).setString(2, issecond)
					.setString(3, isoversea).setLong(4, psnCode).executeUpdate();
			return;
		} else {
			sql = "insert into evreviewer(psn_code,pwd,istalentpsn,issecond,isoversea) values(?,?,?,?,?)";
			this.getSession().createSQLQuery(sql).setLong(0, psnCode).setString(1, pwd).setString(2, istrain)
					.setString(3, issecond).setString(4, isoversea).executeUpdate();
			return;
		}

	}

	/**
	 * 取消专家身份.
	 * 
	 * @param psnCode
	 */
	public void cancelFromReviewer(Long psnCode) {
		String sql = "delete from evreviewer where psn_code = ?";
		this.getSession().createSQLQuery(sql).setLong(0, psnCode).executeUpdate();
	}

	/**
	 * 给用户分配角色.
	 * 
	 * @param psnCode
	 * @param List
	 *            <roleCode>
	 */
	public void assignRole2Person(Long psnCode, List<Long> rolesCode) {
		String sql = "insert into sys_user_role sur(user_id,role_id,ins_id) values(?,?,?)";
		for (Long roleCode : rolesCode) {
			List<Object> params = new ArrayList<Object>();
			params.add(psnCode);
			params.add(roleCode);
			params.add(0L);
			this.createSqlQuery(sql, params).executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Person> getPerson(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("from Person where 1=1");

		if (params.containsKey("psnCnames")) {
			sb.append(" and zhName in(:psnCnames)");
		}
		if (params.containsKey("email")) {
			sb.append(" and lower(email) = :email");
		}
		if (params.containsKey("exceptPsncode")) {
			sb.append(" and psnCode not in(:exceptPsncode)");
		}
		return super.getSession().createQuery(sb.toString()).setProperties(params).list();
	}

	/**
	 * get administator info via having organization-name as query-condition.
	 * 
	 * @author lineshow created on 2011-10-13
	 * @param orgName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Person getAdminByOrgName(String orgName) {
		StringBuilder hql = new StringBuilder();
		hql.append("from Person p left join fetch p.chargOrgs pc left join fetch p.psnExt where  p.enable = true and pc.name = ? ");
		List<Person> persons = this.createQuery(hql.toString(), orgName).list();
		if (CollectionUtils.isEmpty(persons)) {
			return null;
		} else {
			return persons.get(0);
		}
	}

	/**
	 * make sure: current email is conflict with person in system
	 * 
	 * @author lineshow created on 2011-10-25
	 * @param psnCode
	 * @param email
	 * @return
	 */
	public boolean conflictEmail(Long psnCode, String email) {
		String hql = "select count(*) from Person p where p.enable = true and p.psnCode <> ? and lower(p.email) = ? and exists(from PersonSubInfo4Sync ps where ps.validEmail = true and ps.psnCode = p.psnCode)";
		Object o = this.getSession().createQuery(hql).setLong(0, psnCode).setString(1, email).uniqueResult();
		return !o.toString().equals("0");
	}

	/**
	 * make sure: current mobile is conflict with person in system.
	 * 
	 * @author lineshow created on 2011-10-25
	 * @param psnCode
	 * @param email
	 * @return
	 */
	public boolean conflictMobile(Long psnCode, String mobile) {
		String hql = "select count(*) from Person p where p.enable = true and p.psnCode <> ? and p.mobile = ? and exists(from PersonSubInfo4Sync ps where ps.validMobile = true and ps.psnCode = p.psnCode)";
		Object o = this.getSession().createQuery(hql).setLong(0, psnCode).setString(1, mobile).uniqueResult();
		return !o.toString().equals("0");
	}

	/**
	 * make sure: current card is conflict with person in system
	 * 
	 * @author lineshow created on 2011-10-25
	 * @param psnCode
	 * @param email
	 * @return
	 */
	public boolean conflictCard(Long psnCode, Integer cardType, String cardCode) {
		String hql = "select count(*) from Person p where p.enable = true and p.psnCode <> ? and p.cardType = ? and p.cardCode = ? and exists(from PersonSubInfo4Sync ps where ps.validCard = true and ps.psnCode = p.psnCode)";
		Object o = this.getSession().createQuery(hql).setLong(0, psnCode).setInteger(1, cardType)
				.setString(2, cardCode).uniqueResult();
		return !o.toString().equals("0");
	}

	public boolean conflictCard(Integer cardType, String cardCode) {
		String hql = "select count(*) from Person p where p.enable = true and p.cardType = ? and p.cardCode = ?";
		// and exists(from PersonSubInfo4Sync ps where ps.validCard = true and ps.psnCode = p.psnCode)
		Object o = this.getSession().createQuery(hql).setInteger(0, cardType).setString(1, cardCode).uniqueResult();
		return !o.toString().equals("0");
	}

	/**
	 * fetch all person belong to the department base deptCode.
	 * 
	 * @author lineshow created on 2011-10-22
	 * @param deptCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Person> getPsnsBydept(Long deptCode) {
		String hql = "select d.psns from  Department d where d.deptCode = ?";
		return this.getSession().createQuery(hql).setLong(0, deptCode).list();
	}

	/**
	 * 获取单位管理员.
	 * 
	 * @param orgCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Person getAdminOfOrg(Long orgCode) {
		String hql = "select p from Person p left join fetch p.chargOrgs pc where pc.orgCode = ?";
		List<Person> persons = this.getSession().createQuery(hql).setLong(0, orgCode).list();
		if (org.apache.commons.collections.CollectionUtils.isEmpty(persons)) {
			return null;
		}
		return persons.get(0);
	}

	/**
	 * get conflict person base on card attribute.
	 * 
	 * @author lineshow created on 2011-12-12
	 * @param cardType
	 * @param cardCode
	 * @return
	 */
	public Person getPersonByCard(Integer cardType, String cardCode) {
		return getPersonByCardAndName(cardType, cardCode, null);
	}

	/**
	 * 
	 * 根据证件号码和姓名查询到唯一记录。在未来版本中可能删除，参考findPerson(ConditionContainer conditonContainer)方法
	 * 
	 * @param cardType
	 * @param cardCode
	 * @param psnName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public Person getPersonByCardAndName(Integer cardType, String cardCode, String psnName) {
		String hql = "select p from Person p where p.enable = true and p.cardType = ? and p.cardCode = ?";
		if (StringUtils.isNotBlank(psnName)) {
			hql += " and p.zhName = ?";
		}
		Query query = this.getSession().createQuery(hql).setInteger(0, cardType).setString(1, cardCode);
		if (StringUtils.isNotBlank(psnName)) {
			query.setString(2, psnName);
		}
		try {
			Object obj = query.uniqueResult();
			return obj == null ? null : (Person) obj;
		} catch (Exception e) {
			List<Person> personList = query.list();
			return personList.get(0);
		}
	}

	/**
	 * 
	 * 查询可用人员分页显示列表
	 * 
	 * @return
	 */
	/*public PageContainer findPerson(ConditionContainer conditonContainer) {
		PageQueryHanlder pageQuery = new PageQueryHanlder();
		Map<String, Object> conditionMap = conditonContainer.getConditions();
		pageQuery.setQlMode(PageQueryHanlder.MODE_HQL);
		pageQuery
				.addQlSegment("select p.psnCode as psnCode, p.email as email, p.mobile as mobile, p.profTitle as profTitle, p.orgName as orgName from Person p where p.enable = true");
		// pageQuery.addQlSegment(" from Person p where p.enable = true");

		if (conditionMap.containsKey("psnName")) {
			pageQuery.addQlSegment(" and p.zhName = :psnName");
			pageQuery.addParamInMap("psnName", conditionMap.get("psnName"));
		}
		if (conditionMap.containsKey("cardType")) {
			pageQuery.addQlSegment(" and p.cardType = :cardType");
			pageQuery.addParamInMap("cardType", Integer.valueOf(conditionMap.get("cardType").toString()));
		}
		if (conditionMap.containsKey("cardCode")) {
			pageQuery.addQlSegment(" and p.cardCode = :cardCode");
			pageQuery.addParamInMap("cardCode", conditionMap.get("cardCode"));
		}
		return pageQuery.loadOut(this, conditonContainer);
	}*/

	/**
	 * get conflict person base on email.
	 * 
	 * @author lineshow created on 2011-12-12
	 * @param cardType
	 * @param cardCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Person getPersonByEmail(String email) {
		String hql = "select p from Person p where p.enable = true and lower(p.email) = ?";
		Query query = this.getSession().createQuery(hql).setString(0, email);
		try {
			Object obj = query.uniqueResult();
			return obj == null ? null : (Person) obj;
		} catch (Exception e) {
			List<Person> personList = query.list();
			return personList.get(0);
		}
	}

	/**
	 * deemed to be exist person or not base on name and email
	 * 
	 * @author lineshow created on 2011-12-16
	 * @param name
	 * @param email
	 * @return
	 */
	public boolean existInSystem(String name, String email) {
		String hql = "select count(*) from Person p where p.enable = true and p.zhName = ? and lower(p.email) = ? ";
		Object count = this.getSession().createQuery(hql).setString(0, name).setString(1, email).uniqueResult();
		return !"0".equals(count.toString());
	}

	/**
	 * is administrator of department.
	 * 
	 * @author lineshow created on 2011-12-23
	 * @param psnCode
	 * @return
	 */
	public boolean isAdminorOfDept(Long psnCode) {
		String hql = "select count(*) from person_dept_admin pda where pda.psn_code  = ?";
		Object count = this.getSession().createSQLQuery(hql).setLong(0, psnCode).uniqueResult();
		return !"0".equals(count.toString());
	}

	/**
	 * obtain all title of a technical post.
	 * 
	 * @author lineshow created on 2012-1-5
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAllTitle() {
		String sql = "select cd.code,cd.zh_cn_caption from const_dictionary cd where cd.category='title'";
		return this.getSession().createSQLQuery(sql).list();
	}

	/**
	 * get persons based on mobile.
	 * 
	 * @author lineshow created on 2012-1-21
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Person> getPersonByMobile(String mobile) {
		Criteria criteria = this.getSession().createCriteria(Person.class);
		return criteria.add(Restrictions.eq("mobile", mobile)).list();
	}

	/**
	 * 该用户手机号是否验证通过.
	 * 
	 * @author lineshow created on 2012-1-21
	 * @param psnCode
	 * @return
	 */
	public Boolean validMobileUser(Long psnCode) {
		Criteria criteria = this.getSession().createCriteria(PersonSubInfo4Sync.class);
		Object obj = criteria.add(Restrictions.eq("psnCode", psnCode)).add(Restrictions.eq("validMobile", true))
				.uniqueResult();
		return obj != null;
	}

	@SuppressWarnings("unchecked")
	public Person getPerson4Sync(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("from Person where 1=1");

		if (params.containsKey("zhName")) {
			sb.append(" and zhName in(:zhName)");
		}
		if (params.containsKey("email")) {
			sb.append(" and lower(email) = :email");
		}
		if (params.containsKey("idNo")) {
			sb.append(" and cardCode not in(:idNo)");
		}
		List<Person> list = super.getSession().createQuery(sb.toString()).setProperties(params).list();
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}

	}

	/*public PageContainer getPsnGroups(ConditionContainer conditonContainer) {
		PageQueryHanlder pageQuery = new PageQueryHanlder();
		Map<String, Object> conditionMap = conditonContainer.getConditions();
		int roleId = SecurityUtils.getCurrentUserRoleId();

		pageQuery.setQlMode(PageQueryHanlder.MODE_HQL);
		StringBuilder hql = new StringBuilder();
		hql.append("select  p.zhName as zhName,r.name as groupName,o.cname as orgName,ur.id.userId as userId,ur.id.rolId as rolId from  Person p,PersonOffice po,OrgOffice o,UserRole ur,Role r where p.psnCode=po.id.psnCode and po.id.offOrgCode=o.offOrgCode and po.id.psnCode=ur.id.userId and ur.id.rolId=r.id ");
		pageQuery.addQlSegment(hql.toString());
		if (conditionMap.containsKey("psnname") && StringUtils.isNotEmpty(MapUtils.getString(conditionMap, "psnname"))) {
			pageQuery.addQlSegment("and p.zhName like '%'||:zhName||'%'");
			pageQuery.addParamInMap("zhName", conditionMap.get("psnname"));

		}

		if (conditionMap.containsKey("psnorg")) {

			if (StringUtils.isNotEmpty(MapUtils.getString(conditionMap, "psnorg"))) {
				pageQuery.addQlSegment("and po.id.offOrgCode=:psnorg  ");
				pageQuery.addParamInMap("psnorg", MapUtils.getLong(conditionMap, "psnorg"));

			}
		} else {

			if (roleId != RoleConstants.INFORMATION_CENTER_INT) { // 学科学部用户

				String subHql = "select  subo.offOrgCode  from  OrgOffice subo,OrgOfficeUnitno subou where subo.offOrgCode=subou.offOrgCode and subou.mngunitno is not null and exists (select 1 "
						+

						" from PersonOffice tp,OrgOfficeUnitno tu  where tp.id.offOrgCode=tu.offOrgCode and tp.id.psnCode="
						+ SecurityUtils.getCurrentUserId() + " and subou.unitno like tu.unitno||'%')";

				pageQuery.addQlSegment("and po.id.offOrgCode  in(" + subHql + ")");

			}

		}

		if (conditionMap.containsKey("psngroup")) {

			if (StringUtils.isNotEmpty(MapUtils.getString(conditionMap, "psngroup"))) {
				pageQuery.addQlSegment("and ur.id.rolId=:psngroup");
				pageQuery.addParamInMap("psngroup", MapUtils.getLong(conditionMap, "psngroup"));

			}
		} else {
			pageQuery
					.addQlSegment("and ur.id.rolId in(  select  cd.id.code  from ConstDictionary cd where cd.id.category='role_group_"
							+ roleId + "')");

		}

		return pageQuery.loadOut(this, conditonContainer);
	}*/

	public List getPsnData(Long psnCode, Integer roleCode) {
		String hql = "select p.zhName as zhName,p.firstName as firstName,p.lastName as lastName,r.name as roleName from Person p,Role r where p.psnCode = ? and r.id = ?";
		return super.createQuery(hql, psnCode, roleCode.longValue()).list();
	}

	@SuppressWarnings("unchecked")
	public PersonBaseInfo getPerosnBaseInfo(Long psnCode) {
		String hql = "select p from PersonBaseInfo p where p.psnCode = ?";
		Query query = this.getSession().createQuery(hql).setLong(0, psnCode);
		try {
			Object obj = query.uniqueResult();
			return obj == null ? null : (PersonBaseInfo) obj;
		} catch (Exception e) {
			List<PersonBaseInfo> personList = query.list();
			return personList.get(0);
		}
	}

	/**
	 * 检查是否可删除此申报人。删除申报人需要检查此申报人的所有申请书状态，必须是填写中、退回修改、已删除状态才能删除
	 * 
	 * @param psnCode
	 *            检查申报人是否可以删除
	 * @param prpStatus
	 *            可删除申报人的申请书状态
	 * */
	public boolean checkRemovePerson(Long psnCode, List<String> prpStatus) throws DaoException {
		boolean flag = false;
		// 查询不可删除的申请书
		List<ProposalCached> proposalCacheds = proposalCachedDao.findProposalCached(psnCode, prpStatus);
		if (CollectionUtils.isEmpty(proposalCacheds)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 检查是否可删除此申报人。删除申报人需要检查此申报人为指定单位填写的所有申报书状态，必须是填写中、退回修改、已删除状态才能删除
	 * 
	 * @param psnCode
	 *            检查申报人是否可以删除
	 * @param prpStatus
	 *            可删除申报人的申请书状态
	 * */
	public boolean checkRemovePerson(Long orgCode, Long psnCode, Long grantCode, List<String> prpStatus)
			throws DaoException {
		// 查询不可删除的申请书
		List<ProposalCached> proposalCacheds = proposalCachedDao.findProposalCached(orgCode, psnCode, grantCode,
				prpStatus);
		boolean flag = false;
		if (CollectionUtils.isEmpty(proposalCacheds)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 清除person表相关联的所有表数据
	 * */
	private void clearPersonAllInfo(Long orgCode, List<Long> psnCodes) {
		if (null == psnCodes || psnCodes.size() == 0) {
			return;
		}
		getSession().createQuery("delete from SysUser t where t.id in :psnCodes")
				.setParameterList("psnCodes", psnCodes).executeUpdate(); // 删除用户登陆信息
		getSession().createQuery("delete from UserRole t where t.id.userId in :psnCodes")
				.setParameterList("psnCodes", psnCodes).executeUpdate(); // 删除用户角色
		getSession().createQuery("delete from Person p where p.psnCode in :psnCodes")
				.setParameterList("psnCodes", psnCodes).executeUpdate(); // 删除个人信息
		getSession().createQuery("delete from PersonExtend t where t.psnCode in :psnCodes")
				.setParameterList("psnCodes", psnCodes).executeUpdate(); // 删除个人信息扩展
		getSession().createQuery("delete from PsnOrgState t where t.person.psnCode in :psnCodes")
				.setParameterList("psnCodes", psnCodes).executeUpdate(); // 删除个人与单位临时关系
		getSession().createQuery("delete from PersonSubInfo4Sync t where t.psnCode in :psnCodes")
				.setParameterList("psnCodes", psnCodes).executeUpdate(); // 删除个人信息子表
		getSession().createSQLQuery("delete from person_org where psn_code in :psnCodes and org_code = :orgCode")
				.setParameterList("psnCodes", psnCodes).setLong("orgCode", orgCode).executeUpdate(); // 删除个人与单位的关系
	}

	/**
	 * 删除单位人员关系.
	 * 
	 * @param orgCode
	 *            删除单位下的人员
	 * @param psnCodes
	 *            将要被删除的人员code
	 * @throws DaoException
	 */
	public void deletePersons(Long orgCode, List<Long> psnCodes) throws DaoException {
		List<Long> clearList = new ArrayList<Long>(); // 将要被删除的psnCode列表
		for (Long psnCode : psnCodes) {
			Person person = get(psnCode);
			if (null == person || (person.getImportPsn() != null && person.getImportPsn() == 1)) { // 没有个人信息 或为导入信息
				continue;
			}
			if (!person.getEnable()) { // 未激活的用户，需要删除所有关联表数据
				clearList.add(person.getPsnCode());
			} else { // 已激活的用户，需要删除用户申报人角色和单位与申报人的关系

				List<UserRole> userRoles = userRoleDao.getUserRoles(psnCode);
				if (userRoles.size() == 1) { // 如果只有一个角色，则解除和单位的关系
					clearList.add(person.getPsnCode());
				} else { // 删除申报权限
					userRoleDao.batchExecute("delete from UserRole t where t.id.userId = ? and t.id.rolId=?",
							person.getPsnCode(), new Long(RoleConstants.PROPOSER_USER)); // 删除申请人角色
					person.removeOrgs(); // 删除个人与单位的关系
					person.removeChargDepts();
					person.removeDepts();
					// getSession()
					// .createSQLQuery("delete from person_org where psn_code = :psnCode and org_code = :orgCode")
					// .setLong("psnCode", person.getPsnCode()).setLong("orgCode", orgCode).executeUpdate(); //

				}

			}
		}
		clearPersonAllInfo(orgCode, clearList);
	}
	
	public Long getPersonBynameAndCard(String name, String cardType, String cardNo){
		String sql = "select p.psn_code\n" +
						"  from person p\n" + 
						"  left join const_dictionary cd\n" + 
						"    on cd.category = 'id_type'\n" + 
						"   and p.card_type = cd.code\n" + 
						" where lower(p.card_code) = lower(?)\n" + 
						"   and cd.zh_cn_caption = trim(?)";
		Object object = super.getSession().createSQLQuery(sql).setString(0, cardNo)
				.setString(1, cardType).uniqueResult();
		if(object == null){
			return null;
		}
		return Long.parseLong(object.toString());
	}
	
	@SuppressWarnings({ "unchecked" })
	public String getDeptCodeByUser(Long currentUserId) {
		String sql = "select dept_code from person_dept  where psn_code =?";
		List<Map<String, Object>> list = this.getSession().createSQLQuery(sql).setParameter(0, currentUserId)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list == null || list.size() == 0) {
			return null;
		} else 
			return list.get(0).get("DEPT_CODE").toString();
	}

	public boolean existInSystemByLoginName(String loginName) {
		String sql = "select count(0) from sys_user su left join person  p on su.id = p.psn_code\n" +
						"where p.penable = 1 and su.enabled = 1 and su.login_name = ?";
		int count = super.queryForInt(sql, new Object[]{loginName});
		return count>0;
	}
}
