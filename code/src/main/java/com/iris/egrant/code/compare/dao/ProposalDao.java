package com.iris.egrant.code.compare.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iris.egrant.code.compare.constant.AppSettingConstants;
import com.iris.egrant.code.compare.constant.ProposalConstants;
import com.iris.egrant.code.compare.model.Proposal;
import com.iris.egrant.code.compare.model.ProposalCached;
import com.iris.egrant.code.compare.model.ProposalCachedExtend;
import com.iris.egrant.code.compare.service.AppSettingContext;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.utils.XMLHelper;
 
/**
 * 申报审核类.
 * 
 * @author yamingd
 * 
 */
@Repository
public class ProposalDao extends SimpleHibernateDao<Proposal, Long> {

	@Autowired
	private ProposalCachedDao proposalCachedDao;
	@Autowired
	private ProposalCachedExtendDao proposalCachedExtendDao;

	/**
	 * the department is associated with propsoal or not
	 * 
	 * @author lineshow created on 2011-10-20
	 * @param deptCode
	 * @return
	 */
	public boolean involveDepartment(Long deptCode) {
		String sql = "select count(*) from proposal pro where pro.dept_code = ?";
		BigDecimal count = (BigDecimal) this.getSession().createSQLQuery(sql).setLong(0, deptCode).uniqueResult();
		return count.intValue() != 0;
	}

	@SuppressWarnings("unchecked")
	public List<Proposal> findAllByCodes(List<Long> prpCodes) {
		String hql = "select t from Proposal t  where t.prpCode in ( :prpCodes )";
		Query query = super.createQuery(hql);
		query.setParameterList("prpCodes", prpCodes);
		return query.list();
	}

	/**
	 * check that exist proposal which wait for approved by department administrator in bussiness-process-management.
	 * 
	 * @author lineshow created on 2011-11-15
	 * @param deptCode
	 * @return
	 */
	public boolean existInBpm2Dept(Long deptCode) {
		StringBuilder hql = new StringBuilder();
		hql.append("select count(*) from Proposal p where p.deptCode = ? and status = '")
				.append(ProposalConstants.PRP_BE_DEPT_SUBMIT).append("'");
		Long count = (Long) this.createQuery(hql.toString(), deptCode).uniqueResult();
		return count != 0;
	}

	/**
	 * fetch all proposal belonged to department
	 * 
	 * @author lineshow created on 2011-11-15
	 * @param deptCode
	 * @return
	 */
	public List<Proposal> findAllByDept(Long deptCode) {
		StringBuilder hql = new StringBuilder();
		hql.append("from Proposal p where p.deptCode = ?");
		return this.createQuery(hql.toString(), deptCode).list();
	}

	/**
	 * get Propsoal by posCode
	 * 
	 * @author lineshow created on 2011-11-15
	 * @param deptCode
	 * @return
	 */
	public Proposal getProposalByPoscode(Long pos_code) {
		StringBuilder hql = new StringBuilder();
		hql.append("from Proposal p where p.posCode = ?");
		return (Proposal) this.createQuery(hql.toString(), pos_code).uniqueResult();
	}

	/**
	 * replace deptcode of proposal which belonged to olddept with newdept
	 * 
	 * @author lineshow created on 2011-11-16
	 * @param oldDeptCode
	 * @param newDeptCode
	 */
	public void updateDeptOfProposal(Long oldDeptCode, Long newDeptCode) {
		StringBuilder hql = new StringBuilder();
		hql.append("update Proposal p set p.deptCode = ? where p.deptCode = ?");
		this.getSession().createQuery(hql.toString()).setLong(0, newDeptCode).setLong(1, oldDeptCode).executeUpdate();
	}

	/**
	 * 调用自定义函数生成申报书科学部编号
	 * 
	 * @param grantCode
	 * @param subGrantCode
	 * @param helpGrantCode
	 * @param subjectCode
	 * @param statYear
	 * @return
	 * @throws SQLException
	 */
	public String getGenPrpNO(Long grantCode, Long subGrantCode, Long helpGrantCode, String subjectCode, String statYear)
			throws SQLException {

		String prpNo = "";
		SQLQuery sq = this.getSession().createSQLQuery("select FN_PROPOSAL_GEN_NO(?,?,?,?,?) from dual");
		sq.setLong(0, grantCode == null ? 0 : grantCode);
		sq.setLong(1, subGrantCode == null ? 0 : subGrantCode);
		sq.setLong(2, helpGrantCode == null ? 0 : helpGrantCode);
		sq.setString(3, subjectCode);
		sq.setString(4, statYear);
		prpNo = (String) sq.uniqueResult();

		return prpNo;
	}

	/**
	 * 调用自定义函数生成申报书版本号，生成规则：pos_code(7位)+流水号（3位）
	 * 
	 * @param posCode
	 * @param prpVersion
	 * @param grantCode
	 * @param subGrantCode
	 * @param helpGrantCode
	 * @param grantNo
	 * @return
	 */
	public String getGenPrpVersion(Long posCode, String prpVersion, String grantCode, String subGrantCode,
			String helpGrantCode, String grantNo) {
		String version = "";
		try {

			SQLQuery sq = this.getSession().createSQLQuery("select FN_PROPOSAL_GEN_VERSION(?,?,?,?,?,?,?) from dual");
			sq.setLong(0, posCode != null ? posCode : 0);
			sq.setString(1, prpVersion == null ? "" : prpVersion);
			sq.setLong(2, NumberUtils.isDigits(grantCode) ? Long.valueOf(grantCode) : 0);
			sq.setLong(3, NumberUtils.isDigits(subGrantCode) ? Long.valueOf(subGrantCode) : 0);
			sq.setLong(4, NumberUtils.isDigits(helpGrantCode) ? Long.valueOf(helpGrantCode) : 0);
			sq.setString(5, grantNo == null ? "" : grantNo);

			String statYear = AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR);
			sq.setString(6, statYear);
			version = (String) sq.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			version = prpVersion;
		}
		return version;
	}

	/**
	 * 获得是否集中受理标志
	 * 
	 * @param grantCode
	 * @param subGrantCode
	 * @param helpGrantCode
	 * @param subjectCode
	 * @param statYear
	 * @return
	 * @throws SQLException
	 */
	public Integer getJSSLFlag(String grantNo, String subGrantNo, String helpGrantNo) throws SQLException {

		Integer flagJZSL = 0;
		SQLQuery sq = this.getSession().createSQLQuery("select FN_PROPOSAL_IS_JZSL(?,?,?) from dual");
		sq.setString(0, grantNo == null ? "" : grantNo);
		sq.setString(1, subGrantNo == null ? "" : subGrantNo);
		sq.setString(2, helpGrantNo == null ? "" : helpGrantNo);
		// sq.setString(5, prpNo);
		flagJZSL = Integer.valueOf(sq.uniqueResult().toString());

		return flagJZSL;
	}

	public List<Map<String, Object>> getPsnCodes() {
		SQLQuery sq = this.getSession().createSQLQuery("select psn_code form project");
		List list = sq.list();
		return list;
	}

	/**
	 * 获取proposal表psn_code为0的记录.
	 * 
	 * @return
	 */
	public List<Proposal> findAllPrps() {
		StringBuilder hql = new StringBuilder();
		hql.append("from Proposal p where p.psnCode=0");
		return this.createQuery(hql.toString()).list();
	}

	public void updateProposal(Long prpCode, Long psnCode) throws DaoException {
		StringBuilder hql = new StringBuilder();
		hql.append("update Proposal p set p.psnCode=? where p.prpCode=?");
		super.createQuery(hql.toString(), psnCode, prpCode).executeUpdate();
	}

	public void updateProposal(Proposal proposal) throws DaoException {
		this.getSession().update(proposal);
	}

	/**
	 * 执行申请项目的同步标识存储过程.
	 * 
	 * @param proposal
	 * @throws Exception
	 */
/*	public void callPrpProc(Proposal proposal) throws Exception {

		CallableStatement cs = super.getSession().connection().prepareCall("{call proposal_proc(?,?,?)}");
		cs.setLong(1, proposal.getPrpCode());
		cs.setString(2, proposal.getStatus());
		cs.setString(3, proposal.getSubGrantCode() == null ? "" : proposal.getSubGrantCode().toString());
		cs.execute();

	}*/

	/** 离线数据搞入申请评议 **/
	public void markReview(Long prpCode) {
		try {
			this.getSession().createSQLQuery("call ATVCONTAINER_INIT(2, " + prpCode + ")");
		} catch (Exception e) {
			logger.error("-------call ATVCONTAINER_INIT error" + prpCode, e);
		}
	}

	/**
	 * 判断申请书是否可以确认
	 * 
	 * @param prpCodes
	 * @return
	 */
	public boolean whetherCanApprove(List<Long> prpCodes) {
		String hql = "select t from Proposal t where t.prpCode in ( :prpCodes ) and t.status != :status";
		Query query = super.createQuery(hql);
		query.setParameterList("prpCodes", prpCodes);
		query.setParameter("status", ProposalConstants.PRP_BE_ORG_SUBMIT);
		return query.list().size() > 0 ? false : true;
	}

	/**
	 * 判断申请书是否可以确认
	 * 
	 * @param prpCodes
	 * @return
	 */
	public boolean whetherCanApprove(List<Long> prpCodes,String status) {
		String hql = "select t from Proposal t where t.prpCode in ( :prpCodes ) and t.status != :status";
		Query query = super.createQuery(hql);
		query.setParameterList("prpCodes", prpCodes);
		query.setParameter("status", status);
		return query.list().size() > 0 ? false : true;
	}
	/****
	 * @author Administrator 查询当前单位审核通过的申请书
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> searchAuditCommentGrantInfo(Map<String, Object> params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select p.grant_code, g.zh_cn_grant_name as grant_name, count(0) total");
		sb.append(" from proposal p");
		sb.append(" left join grant_setting g on p.grant_code = g.grant_code");
		sb.append(" where 1 = 1");
		sb.append(" and p.status is not null and p.status not in ('00','01','10','98','99') ");
		if (params.containsKey("orgCode")) {
			sb.append(" and p.org_code = :orgCode");
		}
		sb.append(" group by p.grant_code, g.zh_cn_grant_name");
		list = this.getSession().createSQLQuery(sb.toString()).setProperties(params)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	/**
	 * 根据psnCode获取人员的申请书
	 * 
	 * @author liulijie
	 * @param psnCode
	 *            人员编码
	 * */
	@SuppressWarnings("unchecked")
	public List<ProposalCached> findProposalByPsnCode(Long psnCode) {
		Query query = createQuery("from ProposalCached where psnCode = :psnCode");
		query.setLong("psnCode", psnCode);
		return query.list();
	}

	/**
	 * 根据psnCode和orgCode获取申请书
	 * 
	 * @param orgCode
	 *            单位编码
	 * @param psnCode
	 *            人员编码
	 * */
	@SuppressWarnings("unchecked")
	public List<ProposalCached> findProposal4OrgByPsnCode(Long orgCode, Long psnCode) {
		Query query = createQuery("from ProposalCached where orgCode = :orgCode and psnCode = :psnCode");
		query.setLong("orgCode", orgCode);
		query.setLong("psnCode", psnCode);
		return query.list();
	}

	/**
	 * 根据单位人员某种类别的申请书
	 * 
	 * @author liulijie
	 * @param orgCode
	 *            单位编码
	 * @param psnCode
	 *            人员编码
	 * @param grantCode
	 *            申报书业务类别
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findProposal(Long orgCode, Long psnCode, Long grantCode) throws DaoException {
		List<Object> paramsList = new ArrayList<Object>();
		paramsList.add(orgCode);
		paramsList.add(psnCode);
		paramsList.add(grantCode);
		Query query = createSqlQuery(
				"select t.zh_title as title, c.zh_cn_caption as statusName from Proposal_Cached t left join const_dictionary c on t.status = c.code and c.category = 'proposal_status' where org_code=? and psn_code=? and grant_code=?",
				paramsList).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		query.setReadOnly(true);
		return query.list();
	}
	
	
	/**
	 * 找到被授权人员填写的被授权类别的未被删除的申报书
	 * 
	 * @author zhoujianhui
	 * @param orgCode
	 *            单位编码
	 * @param psnCode
	 *            人员编码
	 * @param grantCode
	 *            申报书业务类别
	 * @param status  删除状态标志
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAuthoredExistProposal(Long orgCode, Long psnCode, Long grantCode, String status)  {
		List<Object> paramsList = new ArrayList<Object>();
		paramsList.add(orgCode);
		paramsList.add(psnCode);
		paramsList.add(grantCode);
		paramsList.add(status);
		Query query = createSqlQuery(
				"select t.zh_title as title, c.zh_cn_caption as statusName from Proposal_Cached t left join const_dictionary c on t.status = c.code and c.category = 'proposal_status' where org_code=? and psn_code=? and grant_code=? and status <> ?",
				paramsList).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		query.setReadOnly(true);
		return query.list();
	}
	
	
	

//	/**
//	 * 找到指定person为指定orgCode单位填写的status状态的proposal的名字
//	 * @author zhoujianhui
//	 * @param psnCode
//	 * @param orgCode
//	 * @param status
//	 * @return
//	 */
//	public List<Object[]> findProNameByPsnOrgCodeAndStatus(Long psnCode, Long orgCode, List<String> status) {
//		String hql = "select p.zhTitle as zhTitle from  Proposal p where p.psnCode= ? and p.orgCode=? and p.status in(:status)";
//		Query query = this.createQuery(hql, psnCode, orgCode).setParameterList("status", status);
//		return query.list();
//	}

//	/**
//	 * 将指定人的被退回、在填写、未审核的申报书置为删除状态。
//	 * 
//	 * @param psnCode
//	 */
//	public void deletePorposalByPsn(Long psnCode) {
//		StringBuilder hql = new StringBuilder();
//		hql.append("update Proposal p set p.status = '99' where p.psnCode=? and p.status in('00','10','01')");
//		super.createQuery(hql.toString(), psnCode).executeUpdate();
//	}

	/**
	 * 根据申请人psnCode和可删除申请书状态删除此申报人的申请书
	 * 
	 * @param psnCode
	 *            删除此申报人的申请书
	 * @param prpStatus
	 *            可删除申报书的状态
	 * */
	public void deleteProposal(Long psnCode, List<String> prpStatus) throws DaoException {
		List<ProposalCached> proposals = findProposalByPsnCode(Long.valueOf(psnCode));
		for (ProposalCached p : proposals) {
			if (prpStatus.contains(p.getStatus()) && !p.getStatus().equals(ProposalConstants.PRP_HAS_DELETE)) { // 是可删除的申请书
				
				Proposal pro = findUniqueBy("posCode", p.getPosCode());
				//代录申请书删除项目负责人 将项目的项目负责人还原至代录人  防止单位管理员误操作
				if(p.getSubmitPsnCode()!=null && p.getSubmitPsnCode().longValue()!=p.getPsnCode().longValue()){
					
					if(pro!=null)
						pro.setPsnCode(pro.getSubmitPsnCode());
					p.setPsnCode(p.getSubmitPsnCode());
					ProposalCachedExtend pce  = proposalCachedExtendDao.get(p.getPosCode());
					 Document doc = XMLHelper.parseDoc(pce.getPrpXml());
					Node n  = doc.selectSingleNode("/data/proposal/psn_code");
					n.setText(p.getPsnCode().toString());
					try {
						pce.setPrpXml( XMLHelper.parseDoc(doc));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					proposalCachedExtendDao.save(pce);
				}else{
					p.setStatus(ProposalConstants.PRP_HAS_DELETE);
					proposalCachedDao.save(p);
					if (pro != null) {
						pro.setStatus(ProposalConstants.PRP_HAS_DELETE);
						save(pro);
					}
				}
			}
		}
	}

}
