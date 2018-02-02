package com.iris.egrant.code.project.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.iris.egrant.code.project.model.Project;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.utils.ServiceUtils;

/**
 * 项目表Dao.
 * 
 */
@Repository
public class ProjectDao extends SimpleHibernateDao<Project, Long> {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/*public PageContainer queryExpertPrjJjw(ConditionContainer conditionContainer) {
		Map<String, Object> conditionsMap = conditionContainer.getConditions();
		PageQueryHanlder pageQuery = new PageQueryHanlder();

		StringBuffer sb = new StringBuffer();

		sb.append("select prj.prj_code,prj.prj_no,prj.subject_code1,prj.zh_title,prj.en_title,prj.grant_code,gs.zh_cn_grant_name grant_name,org.name org_name,");
		sb.append("prj.psn_name, cd.zh_cn_caption  xmzt,prj.org_code, prj.stat_year,prj.psn_code ");
		sb.append(" from project prj left join prj_persons pm  on prj.prj_code = pm.prj_code");
		sb.append(" left join const_dictionary cd on cd.code = prj.mis_xmzt and cd.category = 'project_status' ");
		sb.append("  left join organization org on org.org_code = prj.org_code ");
		sb.append(" left join grant_setting gs on gs.grant_code=prj.grant_code ");
		sb.append("  where (prj.psn_code = ? ");
		sb.append("  or (pm.card_type=? and pm.card_value=?))");
		String psnCode = (String) conditionsMap.get("psnCode");
		if (psnCode != null) {
			pageQuery.addParam(Long.valueOf(psnCode));
		}
		if (conditionsMap.containsKey("cardType")) {
			Integer cardType = (Integer) conditionsMap.get("cardType");
			pageQuery.addParam(cardType);
		}
		if (conditionsMap.containsKey("cardCode")) {
			String cardCode = (String) conditionsMap.get("cardCode");
			pageQuery.addParam(cardCode);
		}
		if (conditionsMap.containsKey("yearNum")) {
			sb.append("  and to_number(to_char(sysdate, 'yyyy'))-to_number(to_char(prj.start_date, 'yyyy'))<?");
			sb.append("  and to_number(to_char(sysdate, 'yyyy'))>=to_number(to_char(prj.start_date, 'yyyy'))");
			String yearNum = (String) conditionsMap.get("yearNum");
			pageQuery.addParam(yearNum);
		}

		pageQuery.setQlMode(PageQueryHanlder.MODEL_SQL);
		pageQuery.addQlSegment(sb.toString());
		PageContainer p = pageQuery.loadOut(this, conditionContainer);
		Grid g = p.getGrid();
		if (g != null) {
			List<Map<String, Object>> list = g.getData();
			for (Map<String, Object> map : list) {
				String pcode = ObjectUtils.toString(map.get("PSN_CODE"), "");
				if (!pcode.equals("") && pcode.equals(psnCode)) {
					map.put("JOIN_TYPE", "负责人");
				} else {
					map.put("JOIN_TYPE", "参与人");
				}
			}
		}
		return p;
	}

	public PageContainer queryExpertPrjJjw2(ConditionContainer conditionContainer) {
		Map<String, Object> conditionsMap = conditionContainer.getConditions();
		PageQueryHanlder pageQuery = new PageQueryHanlder();

		StringBuffer sb = new StringBuffer();

		sb.append("select ptp.pd_code prj_code,ptp.pno prj_no,ptp.ctitle zh_title,'' as en_title,ptp.grant_name grant_name,ptp.grant_name org_name,");
		sb.append("ptp.psn_name psn_name,ptp.xmzt xmzt,ptp.org_code,ptp.start_date,ptp.discode1 subject_code1,ptp.input_psn_code  psn_code,ptp.status,ptp.input_psn_name,ptp.input_org_name ");
		sb.append(" from psn_total_proposal ptp ");
		sb.append("  where ptp.pd_type = 2 and ptp.input_psn_name is not null ");
		String psnName = (String) conditionsMap.get("psnName");
		if (psnName != null) {
			sb.append(" and ptp.input_psn_name = ? ");
			pageQuery.addParam(psnName);
		}
		String orgName = (String) conditionsMap.get("orgName");
		if (orgName != null) {
			sb.append("  and ptp.input_org_name = ? ");
			pageQuery.addParam(orgName);
		}

		pageQuery.setQlMode(PageQueryHanlder.MODEL_SQL);
		pageQuery.addQlSegment(sb.toString());
		PageContainer p = pageQuery.loadOut(this, conditionContainer);
		Grid g = p.getGrid();
		if (g != null) {
			List<Map<String, Object>> list = g.getData();
			for (Map<String, Object> map : list) {
				String status = ObjectUtils.toString(map.get("STATUS"), "");
				map.put("JOIN_TYPE", status);
			}
		}
		return p;
	}

	public PageContainer queryExpertPrjJjw4Current(ConditionContainer conditionContainer) {
		Map<String, Object> conditionsMap = conditionContainer.getConditions();
		PageQueryHanlder pageQuery = new PageQueryHanlder();

		StringBuffer sb = new StringBuffer();

		sb.append("select p.prp_code prj_code,p.prp_no prj_no,p.zh_title zh_title,p.en_title en_title,p.subject_code subject_code1,p.grant_code,p.status,cd.zh_cn_caption xmzt,");
		sb.append("org.name org_name,person.zh_name psn_name,gs.zh_cn_grant_name  grant_name,p.psn_code from proposal p ");
		sb.append(" left join proposal_persons pm on p.prp_code = pm.prp_code ");
		sb.append(" left join organization org on org.org_code = p.org_code ");
		sb.append(" left join person person on person.psn_code=p.psn_code ");
		sb.append(" left join grant_setting gs on gs.grant_code=p.grant_code ");
		sb.append(" left join const_dictionary cd on cd.code = p.status and cd.category = 'proposal_status' ");
		sb.append("  where (p.psn_code = ? ");
		sb.append("  or (pm.card_type=? and pm.card_code=?))");
		String psnCode = (String) conditionsMap.get("psnCode");
		if (psnCode != null) {
			pageQuery.addParam(Long.valueOf(psnCode));
		}
		if (conditionsMap.containsKey("cardType")) {
			Integer cardType = (Integer) conditionsMap.get("cardType");
			pageQuery.addParam(cardType);
		}
		if (conditionsMap.containsKey("cardCode")) {
			String cardCode = (String) conditionsMap.get("cardCode");
			pageQuery.addParam(cardCode);
		}
		if (conditionsMap.containsKey("yearNum")) {
			sb.append("  and to_number(to_char(sysdate, 'yyyy'))-to_number(to_char(p.start_date, 'yyyy'))<?");
			sb.append("  and to_number(to_char(sysdate, 'yyyy'))>=to_number(to_char(p.start_date, 'yyyy'))");
			String yearNum = (String) conditionsMap.get("yearNum");
			pageQuery.addParam(yearNum);
		}
		sb.append(" and p.is_accept = 0 and p.is_official = 1");
		pageQuery.setQlMode(PageQueryHanlder.MODEL_SQL);
		pageQuery.addQlSegment(sb.toString());
		PageContainer p = pageQuery.loadOut(this, conditionContainer);
		Grid g = p.getGrid();
		if (g != null) {
			List<Map<String, Object>> list = g.getData();
			for (Map<String, Object> map : list) {
				String pcode = ObjectUtils.toString(map.get("PSN_CODE"), "");
				if (!pcode.equals("") && pcode.equals(psnCode)) {
					map.put("JOIN_TYPE", "负责人");
				} else {
					map.put("JOIN_TYPE", "参与人");
				}
			}
		}
		return p;
	}*/

	public void initContractData(final Long prjCode) throws DaoException {
		getSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				CallableStatement cs = connection.prepareCall("{call pro_contract_init(?)}");
				cs.setLong(1, prjCode);
				cs.execute();
			}
		});
		// throw new DaoException("初始化合同数据失败!", e);
	}
	
	public void initContractDataByAppCode(final Long appCode) throws DaoException {
		getSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				CallableStatement cs = connection.prepareCall("{call pro_contract_appcode(?)}");
				cs.setLong(1, appCode);
				cs.execute();
			}
		});
		// throw new DaoException("初始化合同数据失败!", e);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryRestoreProjectInfo(Long size) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select prjCode, prpNo from(");
		sql.append(" select prj_code as prjCode, prp_no as prpNo, rownum as rn from project t where 1=1");
		sql.append(" and is_Restore in (0, 2, 3, 4) order by t.prj_code asc");
		sql.append(" )v where rn < ? order by v.prjCode asc");
		return this.getSession().createSQLQuery(sql.toString()).setParameter(0, size)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrjPayPlanByAppCode(String app_code) {
		Long a = Long.valueOf( ServiceUtils.decodeFromDes3(app_code).split("\\|")[0]);
		String sql = "select  t.award_shi_amt , t.prj_stat_year , t.grant_code from proposal t left join app_prj_items apj on apj.prp_code = t.prp_code where apj.app_code=?";
		List<Map<String, Object>> validateJson = super.getSession().createSQLQuery(sql).setLong(0, a)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return validateJson;

	}
	@SuppressWarnings("unchecked")
	public String CheckProjectFunding(String grantCodeArr, String statYearArr, String totalawardAmt, String flag) {

		String sql = "";
		List<Object> params = new ArrayList<Object>();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		String sfund = "0";
		String grantName = "";
		String returnStr = "";
		params.clear();

		sql = " select gs.zh_cn_grant_name as grant_name from grant_setting gs where gs.grant_code = ? ";
		params.add(Long.parseLong(grantCodeArr));
		mapList = this.queryForList(sql, params.toArray());
		if (mapList.size() > 0) {
			grantName = mapList.get(0).get("GRANT_NAME").toString();
		}

		// 计算公式是 预算金额（或调整后的预算金额） - 实际已用金额（已拨款金额） - 市下达金额
		sql = " select nvl(sum(decode(t.award_amt_adjusted,'',t.award_amt_budget,'0',t.award_amt_budget,t.award_amt_adjusted)),0) - nvl(sum(t.award_amt_reality),0) - "
				+ Float.parseFloat(totalawardAmt)
				+ " as s_award_amt from fund_setting t left join grant_setting gs on t.grant_code=gs.parent_code where (t.grant_code = ? or gs.grant_code = ?) and t.stat_year = ? and t.payment_flag<>2 " ;
		params.clear();
		params.add(Long.parseLong(grantCodeArr));
		params.add(Long.parseLong(grantCodeArr));
		params.add(statYearArr);
		mapList = this.queryForList(sql, params.toArray());
		if (mapList.size() > 0) {
			sfund = mapList.get(0).get("S_AWARD_AMT") == null ? "" : mapList.get(0).get("S_AWARD_AMT").toString();
		}
		
	 //  add by cg 2014-09-24  立项时 国家省级不检查
			sql = " select count(1) as flag from grant_setting gs where gs.grant_code = ? and nvl(gs.prp_type,99) in (1) and gs.amt_grade in (0,1)  ";
			params.remove(1);
			params.remove(1);
			mapList = this.queryForList(sql, params.toArray());
			//是否 是国家省级项目类别 0 不是
			String is_bs_flag = mapList.get(0).get("FLAG").toString();
		
   
		if ("0".equals(is_bs_flag) && sfund.equals("")) {  //没有预算 
			returnStr = "项目类别【" + grantName + "】没有预算金额<br/>";
		} else if ("0".equals(is_bs_flag) && !sfund.equals("") && Float.parseFloat(sfund) < 0) {      // 超过预算
			returnStr = "项目类别【" + grantName + "】已超出预算" + sfund.substring(1) + "万元<br/>";
		}
		return returnStr;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getContactPsnCnameMsg(Long prjCode) {
		String sql = " select t.contact_psn_email,t.contact_psn_mobile,t.contact_psn_cname from project t where t.prj_code=? ";

		List<Map<String, Object>> list = super.getSession().createSQLQuery(sql).setLong(0, prjCode)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/***
	 * wk add 2014-5-12
	 * 根据dataType初始化表compare_list和表compare_result数据
	 * 
	 * @param keyCode
	 *            业务主键
	 * @param type
	 *            业务类型
	 * @param content
	 *            合并后的内容
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void callExecByDataType(final Long keyCode, final Integer type, final Integer dataType, final String content) throws DaoException {
		System.out.println("keycode:"+keyCode);
		System.out.println("type:"+type);
		System.out.println("dataType:"+dataType);
		System.out.println("content:"+content);
		jdbcTemplate.execute("{call init_compare_list_result(?,?,?,?)}", new CallableStatementCallback() {

			@Override
			public Object doInCallableStatement(java.sql.CallableStatement stat) throws SQLException,
					DataAccessException {
				stat.setLong(1, keyCode);
				stat.setInt(2, type);
				stat.setInt(3, dataType);
				stat.setString(4, content);
				stat.execute();
				return null;
			}
		});
		
		
	}
}
