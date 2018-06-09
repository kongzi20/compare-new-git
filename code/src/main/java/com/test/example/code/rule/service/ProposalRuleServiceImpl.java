package com.test.example.code.rule.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.example.code.compare.constant.AppSettingConstants;
import com.test.example.code.compare.service.AppSettingContext;
import com.test.example.code.forminit.model.FormBaseLibrary;
import com.test.example.code.forminit.service.InitializeService;
import com.test.example.code.grantsetting.dao.GrantSettingDao;
import com.test.example.code.grantsetting.model.GrantSetting;
import com.test.example.code.rule.dao.ElCompnent;
import com.test.example.code.rule.dao.ProposalRuleDao;
import com.test.example.code.rule.dao.ProposalRuleParamDao;
import com.test.example.code.rule.dao.PrpRuleCheckResultDao;
import com.test.example.code.rule.dao.RuleTemplateDao;
import com.test.example.code.rule.model.ParamTemplate;
import com.test.example.code.rule.model.ProposalRule;
import com.test.example.code.rule.model.ProposalRuleParam;
import com.test.example.code.rule.model.RuleTemplate;
import com.test.example.core.exception.DaoException;
import com.test.example.core.exception.ServiceException;
import com.test.example.core.utils.IrisStringUtils;

 
@Service("proposalRuleService")
@Transactional(rollbackFor = Exception.class)
public class ProposalRuleServiceImpl implements ProposalRuleService {

	//@Autowired
	//private WfDefineBatisDao<Map<String, Object>> wfDefineBatisDao;
	
	@Autowired
	private ProposalRuleDao proposalRuleDao;
	@Autowired
	private ProposalRuleParamDao proposalRuleParamDao;
	@Autowired
	private RuleTemplateDao ruleTemplateDao;
	@Autowired
	private InitializeService initializeService;// 初始化
	@Autowired
	private ElCompnent elCompnent;
	@Autowired
	private ProposalRuleParamService proposalRuleParamService;
	@Autowired
	private PrpRuleCheckResultDao prpRuleCheckResultDao;
	@Autowired
	private GrantSettingDao grantSettingDao;

	private static String PARAM_SYS_VALUE_NAME = "sys_value_name";
	private static String PARAM_USER_VALUE_NAME = "user_value_name";
	private static String PARAM_GRANT_CODE = "grant_code";
	private static String PARAM_FIELD_NAME = "field_name";
	private static String PARAM_FIELD_NAME_VALUE = "field_name_value";

	/**
	 * 获取分页数据
	 */
/*	@Override
	public PageContainer getRuleList(ConditionContainer c) throws Exception {
		c.getConditions().put("psn_code", SecurityUtils.getCurrentUserId());
		c.getConditions().put("role_id", SecurityUtils.getCurrentUserRoleId());
		String queryStr = "RuleSetting.getRuleList";
		String countStr = "RuleSetting.getRuleCount";
		PageContainer page = this.wfDefineBatisDao.getSearchPage(queryStr, countStr, c);
		List<Map<String, Object>> data = page.getGrid().getData();
		// 替换参数
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			long ruleId = Long.valueOf(map.get("id").toString());
			ProposalRule pRule = this.proposalRuleDao.get(ruleId);
			this.proposalRuleDao.getSession().evict(pRule);
			// 替换规则
			pRule = replaceParam(pRule);
			// 修改map中message的值。
			page.getGrid().getData().get(i).put("MESSAGE", pRule.getMessage());
		}
		return page;
	}*/

	/**
	 * 使用参数中的值替换规则中的参数
	 * 
	 * @param wfRule
	 * @return
	 * @throws Exception
	 */
	public ProposalRule replaceParam(ProposalRule rule) throws ServiceException {
		List<ProposalRuleParam> params = this.proposalRuleParamDao.getParamsById(rule.getId());
		String msg = rule.getMessage();
		// 使用值替换掉参数
		for (ProposalRuleParam param : params) {
			// 如果valueName属性为空，说明可能不是下拉框，为输入框，将expression的值付给valueName。
			if (param.getUserCustomValueName() == null || "".equals(param.getUserCustomValueName()))
				param.setUserCustomValueName(param.getUserCustomValue());
			// 将参数替换为下拉框的text或输入框的值
			if (StringUtils.isNotBlank(param.getUserCustomValueName()) && StringUtils.isNotBlank(msg)) {
				Map<String, Object> parammap = new HashMap<String, Object>();
				parammap.put(param.getName(), param.getUserCustomValueName());
				msg = buildEl(msg, parammap);
			}
		}
		rule.setMessage(msg);
		return rule;
	}

	/**
	 * ajax替换使用
	 */
	@Override
	public String ajaxChangeMessage(String message, String[] idArr, Map<String, Object> params) throws Exception {
		for (String id : idArr) {
			String expression = (String) params.get("expression_" + id);
			String valueName = (String) params.get("valueName_" + id);
			String paramName = (String) params.get("param_name_" + id);
			// 如果下拉框的text等为空，则将下拉框或input输入框的value赋给前者，最终使用下拉框的text等来替换参数。
			if ("".equals(valueName))
				valueName = expression;
			if ("".equals(valueName))
				valueName = "**";
			if (StringUtils.isNotBlank(valueName) && StringUtils.isNotBlank(message)) {
				Map<String, Object> parammap = new HashMap<String, Object>();
				parammap.put(paramName, valueName);

				message = buildEl(message, parammap);
			}
		}
		// message = buildTwoEl(message);
		return message;
	}

	/**
	 * 使用正则替换参数
	 * 
	 * @param el
	 * @param param
	 * @return
	 */
	private String buildEl(String el, Map<String, Object> param) {
		// 如果含有两个@@符号，则先将[@@ @@]表达式中的值计算出来。
		// if(el.indexOf("@@") != -1) el = buildTwoEl(el,param);
		Pattern pattern = Pattern.compile("\\[@[\\w]+@\\]");
		Matcher m = pattern.matcher(el);
		String key;
		String paramKey;
		while (m.find()) {
			key = m.group().toLowerCase();
			paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
			if (param.containsKey(paramKey)) {
				el = IrisStringUtils.regexReplaceString(el, "\\[@" + paramKey + "@\\]", param.get(paramKey));
			}
		}
		return el;
	}

	/**
	 * 
	 * @param el
	 * @param param
	 * @return
	 */
	private String buildTwoEl(String el) {
		Pattern pattern = Pattern.compile("\\[@@[\\w]+@@\\]");
		Matcher m = pattern.matcher(el);
		String str;
		String elscript;
		while (m.find()) {
			str = m.group().toLowerCase();
			elscript = str.substring(3, str.length() - 3);// 去掉[@@ @@]
			// elscript = buildEl(elscript,param);
			ElCompnent elCompnent = new ElCompnent();
			Integer elvalue = 0;
			try {
				elvalue = (Integer) elCompnent.getElResult(elscript);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			el = IrisStringUtils.regexReplaceString(el, str, elvalue);
		}
		return "";
	}

	/**
	 * 获取规则实体
	 */
	@Override
	public ProposalRule getRuleById(Long id) throws Exception {
		return this.proposalRuleDao.get(id);
	}

	/**
	 * 保存规则
	 */
	@Override
	public ProposalRule saveCommonRule(String id, Map<String, Object> params) throws Exception {
		RuleTemplate ruleTemp = this.ruleTemplateDao.get(Long.parseLong(id));
		ProposalRule rule = new ProposalRule();
		rule.ruleCopyFromTemplate(ruleTemp);
		// 设置更新时间和更新人
		rule.setUpdateDate(new Date());
		rule.setUpdatePsnCode(0L/*SecurityUtils.getCurrentUserId()*/);
		// 保存规则
		this.addProposalRule(rule);
		// 首先获取模板对应的参数列表
		Set<ParamTemplate> paramTemps = ruleTemp.getParamTemps();
		// 遍历模板参数然后将模板参数拷贝到规则参数表
		for (ParamTemplate paramTemp : paramTemps) {
			ProposalRuleParam prParam = new ProposalRuleParam();
			prParam.copyParamTemplateToProposalRuleParam(paramTemp);
			prParam.setRuleId(rule.getId());
			if ("1".equals(paramTemp.getParamMode())) {
				prParam.setUserCustomValue(IrisStringUtils.toString(params.get("expression_" + paramTemp.getId())));
				prParam.setUserCustomValueName(IrisStringUtils.toString(params.get("valueName_" + paramTemp.getId())));
			}
			this.proposalRuleParamDao.save(prParam);
		}

		return rule;
	}

	public void addProposalRule(ProposalRule rule) {
		this.proposalRuleDao.addRule(rule);
	}

	@Override
	public void updateRule(ProposalRule rule) throws Exception {
		this.proposalRuleDao.save(rule);
	}

	/**
	 * 保存规则
	 * 
	 * @throws DaoException
	 * @throws NumberFormatException
	 */
	@Override
	public ProposalRule saveCustomRule(Map<String, Object> map) throws NumberFormatException, DaoException {

		String param_zh_cn_name = (String) map.get("param_zh_cn_name");
		String param_name = (String) map.get("param_name");
		String param_xpath = (String) map.get("param_xpath");
		String param_sccaption_xpath = (String) map.get("param_sccaption_xpath");
		if (!param_xpath.startsWith("/")) {
			param_xpath = "/" + param_xpath;
		}
		if (!param_xpath.startsWith("/data")) {
			param_xpath = "/data" + param_xpath;
		}
		if (!param_sccaption_xpath.startsWith("/")) {
			param_sccaption_xpath = "/" + param_sccaption_xpath;
		}
		if (!param_sccaption_xpath.startsWith("/data")) {
			param_sccaption_xpath = "/data" + param_sccaption_xpath;
		}
		String grant_code = (String) map.get("grant_code");
		String operators_value = (String) map.get("operators_value");
		String operators_name = (String) map.get("operators_name");
		String custom_value = (String) map.get("custom_value");
		String custom_name = (String) map.get("custom_name");
		custom_name = custom_name == null || "".equals(custom_name) ? custom_value : custom_name;
		String custom_key = (String) map.get("custom_key");
		String custom_type = (String) map.get("custom_type");
		String rule_desc = (String) map.get("rule_desc");
		String rule_message = (String) map.get("rule_message");
		// String rule_type = (String)map.get("rule_type");
		String validate_stage = (String) map.get("validate_stage");
		String show_stage = (String) map.get("show_stage");
		// String rule_category = (String)map.get("rule_category");
		String rule_code = (String) map.get("rule_code");
		String cmp_type = (String) map.get("cmp_type");
		String user_validate_script = (String) map.get("user_validate_script");
		String rule_category_value = (String) map.get("rule_category_value");
		String rule_category_name = (String) map.get("rule_category_name");
		String warn_level_value = (String) map.get("warn_level_value");

		// 给传来的小数加上.00
		if (user_validate_script.indexOf("num") != -1 && user_validate_script.indexOf("_0") == -1 && !"0".equals(custom_value)) {
			if (custom_name.indexOf(".") == -1 || custom_value.indexOf(".") == -1) {
				custom_name = custom_name + ".00";
				custom_value = custom_value + ".00";
			}
		}

		Long ruleTmp = "proposal".equals(rule_category_value) ? new Long(25) : new Long(26);
		RuleTemplate ruleTemp = this.ruleTemplateDao.get(ruleTmp);
		ProposalRule rule = null;
		if (rule_code == null || "".equals(rule_code.trim())) {
			rule = new ProposalRule();
			rule.ruleCopyFromTemplate(ruleTemp);
		} else {
			rule = proposalRuleDao.get(Long.valueOf(rule_code));
			ruleTemp=this.ruleTemplateDao.get(rule.getRuleTmpCode());
			List<ProposalRuleParam> params = proposalRuleParamDao.getParamsById(Long.valueOf(rule_code));
			for (ProposalRuleParam param : params) {
				proposalRuleParamDao.delete(param);
			}
		}
		// 设置更新时间和更新人
		rule.setUpdateDate(new Date());

		if (!"manual".equals(operators_value)) {
			if ("dateBetween".equals(custom_type)) {
				String[] custom_values = custom_name.split("&&");
				rule.setFaverMsg("从" + custom_values[0] + "至" + custom_values[1]);
			}
			if ("numBetween".equals(custom_type)) {
				String[] custom_values = custom_name.split("&&");
				rule.setFaverMsg("大于" + custom_values[0] + "且小于" + custom_values[1]);
			} else {
				rule.setFaverMsg(operators_name + " " + custom_name);
			}
		} else {
			rule.setFaverMsg(custom_name);
		}
		GrantSetting gs = grantSettingDao.get(Long.parseLong(grant_code));
		rule.setAmtGrade(gs.getAmtGrade());
		rule.setPrpType(gs.getPrpType());
		rule.setAreaNo(gs.getAreaNo());
		rule.setCmpType(cmp_type);
		rule.setRealityMsg("[@sys_sccaption@]");
		rule.setUpdatePsnCode(0L);
		if (cmp_type == null || "".equals(cmp_type))
			rule.setExpressionDetail(buildCustomRuleExpressWrapper(operators_value, grant_code, param_name, "input".equals(custom_type) ? user_validate_script : custom_type,
					custom_value, custom_name, cmp_type));
		else
			rule.setExpressionDetail(buildCustomRuleExpressWrapper(operators_value, grant_code, param_name, "input".equals(custom_type) ? user_validate_script : custom_type,
					custom_value, custom_name, cmp_type));
		rule.setGrantCode(Long.valueOf(grant_code));
		rule.setInputParamMode(0);
		// rule.setRuleCategory(rule_category);
		// proposal 企业条件
		rule.setRuleCategory(rule_category_value);
		rule.setName(rule_category_name);
		rule.setValidateStage(validate_stage);
		rule.setRuleDesc(rule_desc);
		rule.setOperatorTypeName(operators_name);
		rule.setMessage(rule_message);
		rule.setUserFieldSccaption(custom_name);
		rule.setUserFieldValue(custom_value);
		rule.setFieldName(param_name);
		rule.setFieldZhCnName(param_zh_cn_name);
		rule.setOperatorType(operators_value);
		rule.setYear(AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR));
		rule.setValidateType("manual".equals(operators_value) ? "1" : "0");
		rule.setWarnLevel(warn_level_value);

		// 保存xpath 以便判断是否已经创建了规则。
		rule.setXpath(param_xpath);
		// 设置显示时机
		rule.setShowStage(show_stage);

		// if("manual".equals(operators_value)){
		rule.setMsgZhCnCmp(rule.getMsgZhCnCmp().replace("[@field_zh_cn_name@]", param_zh_cn_name).replace("[@field_name@]", "[@sys_sccaption@]"));
		// }
		// 保存规则
		this.addProposalRule(rule);
		// 首先获取模板对应的参数列表
		Set<ParamTemplate> paramTemps = ruleTemp.getParamTemps();
		// 遍历模板参数然后将模板参数拷贝到规则参数表
		for (ParamTemplate paramTemp : paramTemps) {
			ProposalRuleParam prParam = new ProposalRuleParam();
			prParam.copyParamTemplateToProposalRuleParam(paramTemp);
			prParam.setRuleId(rule.getId());
			prParam.setTmpType(prParam.getName());
			if (PARAM_GRANT_CODE.equals(prParam.getName())) {
				// do nothing but copy template^_^
			} else if (PARAM_FIELD_NAME_VALUE.equals(prParam.getName())) {
				prParam.setUserCustomValue(custom_value);
				prParam.setUserCustomValueName(custom_name);
				prParam.setZhCnName("用户设置的" + param_zh_cn_name);
				prParam.setUserTagType(custom_type);
				prParam.setUserTagKey(custom_key);
				prParam.setXpath(param_xpath);
				prParam.setUserValidateScript(user_validate_script);
				prParam.setName("custom_value");
			} else if (PARAM_FIELD_NAME.equals(prParam.getName())) {
				prParam.setSysParamValue("select trim(extractvalue(pce.prp_xml,'" + param_xpath
						+ "')) as sys_value from proposal_cached_extend pce where pce.pos_code in(select pos_code from proposal p where p.prp_code =[@data_id@])");
				prParam.setSysParamType("2");
				prParam.setName("sys_value");
				prParam.setZhCnName(param_zh_cn_name);
				prParam.setXpath(param_xpath);

			} else if (PARAM_SYS_VALUE_NAME.equals(prParam.getName())) {
				prParam.setSysParamValue("select trim(extractvalue(pce.prp_xml,'" + param_sccaption_xpath
						+ "')) as sys_sccaption from proposal_cached_extend pce where pce.pos_code in(select pos_code from proposal p where p.prp_code =[@data_id@])");
				prParam.setSysParamType("2");
				prParam.setName("sys_sccaption");
				prParam.setZhCnName(param_zh_cn_name);
				prParam.setXpath(param_sccaption_xpath);

			} else if (PARAM_USER_VALUE_NAME.equals(prParam.getName())) {
				prParam.setSysParamValue(custom_name);
				prParam.setUserTagType(custom_type);
				prParam.setUserTagKey(custom_key);
				prParam.setZhCnName("用户设置的" + param_name + "字段描述");
				prParam.setName("custom_sccaption");
				prParam.setXpath(param_xpath);

			}
			this.proposalRuleParamDao.save(prParam);
		}
		return rule;
	}

	// 对级联且必填的校验
	private String buildCustomRuleExpressWrapper(String operators_value, String grant_code, String param_name, String custom_type, String custom_value, String custom_name,
			String cmp_type) {

		String expStr = null;
		if (cmp_type != null && !"".equals(cmp_type)) {
			expStr = buildCustomRuleExpressByScc(operators_value, grant_code, param_name, custom_type, custom_value, custom_name);
		} else {
			expStr = buildCustomRuleExpress(operators_value, grant_code, param_name, custom_type, custom_value, custom_name);
		}
		return "'[@sys_value@]'.equals('-9999999999') ? true : (" + expStr + ")";
	}

	private String buildCustomRuleExpress(String operators_value, String grant_code, String param_name, String custom_type, String custom_value, String custom_name) {
		if (!"manual".equals(operators_value)) {
			if ("in".equals(operators_value)) {
				String custom_value_ = custom_value.replace("，", ",");
				String custom_name_ = custom_name.replace("，", ",");
				String[] customs = custom_value_.split(",");
				String[] custom_names = custom_name_.split(",");
				String rs = "";
				for (int i = 0; i < customs.length; i++) {
					if (i == 0) {
						rs += "',[@sys_value@],'.replace('，',',').lastIndexOf('," + customs[i] + ",')!=-1  ";
					} else {
						rs += " or  ',[@sys_value@],'.replace('，',',').lastIndexOf('," + customs[i] + ",')!=-1 ";
					}
				}
				return rs;
			} else if ("not in".equals(operators_value)) {
				String custom_value_ = custom_value.replace("，", ",");
				String[] customs = custom_value_.split(",");
				String rs = "";
				for (int i = 0; i < customs.length; i++) {
					if (i == 0) {
						rs += "',[@sys_value@],'.replace('，',',').lastIndexOf('," + customs[i] + ",') ==-1 ";
					} else {
						rs += " and  ',[@sys_value@],'.replace('，',',').lastIndexOf('," + customs[i] + ",') ==-1 ";
					}
				}
				return rs;
			} else if ("not like".equals(operators_value)) {
				return "'[@sys_value@]'.lastIndexOf('[@custom_value@]') ==-1";
			} else if ("like".equals(operators_value)) {
				return "'[@sys_value@]'.lastIndexOf('[@custom_value@]')!=-1";
			} else if ("dateBetween".equals(operators_value)) {
				String[] custom_values = custom_name.split("&&");
				return "'" + custom_values[0] + "'<='[@sys_value@]' and '[@sys_value@]'<='" + custom_values[1] + "'";
			} else if (custom_type.indexOf("num") == -1) {
				return "'[@sys_value@]'" + operators_value + "'[@custom_value@]' or '[@sys_sccaption@]'" + operators_value + "'" + custom_name + "'";
			}
			if (custom_type.indexOf("_0_0") != -1) {
				return "(new Long('[@sys_value@]').longValue())" + operators_value + "(new Long('0[@custom_value@]').longValue()) ";
			} else {
				if (">".equals(operators_value))
					return "('[@sys_value@]'.indexOf('-') == -1 and '[@custom_value@]'.indexOf('-') == -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')>'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.]','')>'[@custom_value@]'  : false ))"
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '[@custom_value@]'.indexOf('-') != -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')<'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.\\-]','')<'[@custom_value@]'  : false)) "
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '[@custom_value@]'.indexOf('-') == -1 ) ? false "
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') == -1 and '[@custom_value@]'.indexOf('-') != -1 ) ? true : false";
				if (">=".equals(operators_value))
					return "('[@sys_value@]'.indexOf('-') == -1 and '[@custom_value@]'.indexOf('-') == -1 ) ?(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')>'[@custom_value@]'.indexOf('.'))  ?  ('[@custom_value@]'.indexOf('.')==-1 ? ('[@sys_value@]'.replaceAll('[^0-9\\.]','') > '[@custom_value@]') : true)	 :(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.]','')>='[@custom_value@]'  : false ))"
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '[@custom_value@]'.indexOf('-') != -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')<'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.\\-]','')<='[@custom_value@]'  : false ))"
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '[@custom_value@]'.indexOf('-') == -1 ) ? false "
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') == -1 and '[@custom_value@]'.indexOf('-') != -1 ) ? true : false";
				if ("==".equals(operators_value))
					return "'[@sys_value@]'.replaceAll('[^0-9\\.\\-]','')=='[@custom_value@]' ";
				if ("<".equals(operators_value))
					return "('[@sys_value@]'.indexOf('-') == -1 and '[@custom_value@]'.indexOf('-') == -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')<'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.]','')<'[@custom_value@]'  : false ))"
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '[@custom_value@]'.indexOf('-') != -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')>'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.\\-]','')>'[@custom_value@]'  : false)) "
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '[@custom_value@]'.indexOf('-') == -1 ) ? true "
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') == -1 and '[@custom_value@]'.indexOf('-') != -1 ) ? false : true";
				// return
				// " ('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')<'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.]','')<'[@custom_value@]'  : false)";
				if ("<=".equals(operators_value))
					return "('[@sys_value@]'.indexOf('-') == -1 and '[@custom_value@]'.indexOf('-') == -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')<'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.]','')<='[@custom_value@]'  : false))  "
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '[@custom_value@]'.indexOf('-') != -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')>'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.\\-]','')>='[@custom_value@]'  : false ))"
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '[@custom_value@]'.indexOf('-') == -1 ) ? true "
							+ " : "
							+ "('[@sys_value@]'.indexOf('-') == -1 and '[@custom_value@]'.indexOf('-') != -1 ) ? false : true";

				if (operators_value.indexOf("numBetween") != -1) {
					String[] custom_values = custom_name.split("&&");
					String a = "(('[@sys_value@]'.indexOf('-') == -1 and '" + custom_values[0]
							+ "'.indexOf('-') == -1 ) ?(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')>'" + custom_values[0]
							+ "'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')=='" + custom_values[0]
							+ "'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.]','')>='" + custom_values[0] + "'  : false ))" + " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '" + custom_values[0]
							+ "'.indexOf('-') != -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')<'" + custom_values[0]
							+ "'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')=='" + custom_values[0]
							+ "'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.\\-]','')<='" + custom_values[0] + "'  : false ))" + " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '" + custom_values[0] + "'.indexOf('-') == -1 ) ? false " + " : "
							+ "('[@sys_value@]'.indexOf('-') == -1 and '" + custom_values[0] + "'.indexOf('-') != -1 ) ? true : false) ";
					String b = "(('[@sys_value@]'.indexOf('-') == -1 and '" + custom_values[1]
							+ "'.indexOf('-') == -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')<'" + custom_values[1]
							+ "'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')=='" + custom_values[1]
							+ "'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.]','')<='" + custom_values[1] + "'  : false))  " + " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '" + custom_values[1]
							+ "'.indexOf('-') != -1 ) ? (('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')>'" + custom_values[1]
							+ "'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','').indexOf('.')=='" + custom_values[1]
							+ "'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.\\-]','')>='" + custom_values[1] + "'  : false ))" + " : "
							+ "('[@sys_value@]'.indexOf('-') != -1 and '" + custom_values[1] + "'.indexOf('-') == -1 ) ? true " + " : "
							+ "('[@sys_value@]'.indexOf('-') == -1 and '" + custom_values[1] + "'.indexOf('-') != -1 ) ? false : true )";
					return a + "        and         " + b;

				}
				// return
				// " ('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')<'[@custom_value@]'.indexOf('.'))  ?  true :(('[@sys_value@]'.replaceAll('[^0-9\\.]','').indexOf('.')=='[@custom_value@]'.indexOf('.')) ? '[@sys_value@]'.replaceAll('[^0-9\\.]','')<='[@custom_value@]'  : false)";
				if ("!=".equals(operators_value))
					return " ('[@sys_value@]'.replaceAll('[^0-9\\.\\-]','')!='[@custom_value@]";
				else
					return "false";
			}

		} else {
			return "'[@grant_code@]'=='" + grant_code + "'";
		}

	}

	private String buildCustomRuleExpressByScc(String operators_value, String grant_code, String param_name, String custom_type, String custom_value, String custom_name) {
		if (!"manual".equals(operators_value)) {
			if ("in".equals(operators_value)) {
				String custom_name_ = custom_name.replace("，", ",");
				String[] customs = custom_name_.split(",");
				String rs = "";
				for (int i = 0; i < customs.length; i++) {
					if (i == 0) {
						rs += "',[@sys_sccaption@],'.replace('，',',').lastIndexOf('," + customs[i] + ",')!=-1  ";
					} else {
						rs += " or  ',[@sys_sccaption@],'.replace('，',',').lastIndexOf('," + customs[i] + ",')!=-1 ";
					}
				}
				return rs;
			} else if ("not in".equals(operators_value)) {
				String custom_value_ = custom_name.replace("，", ",");
				String[] customs = custom_value_.split(",");
				String rs = "";
				for (int i = 0; i < customs.length; i++) {
					if (i == 0) {
						rs += "',[@sys_sccaption@],'.replace('，',',').lastIndexOf('," + customs[i] + ",') ==-1 ";
					} else {
						rs += " and  ',[@sys_sccaption@],'.replace('，',',').lastIndexOf('," + customs[i] + ",') ==-1 ";
					}
				}
				return rs;
			} else if ("not like".equals(operators_value)) {
				return "'[@sys_sccaption@]'.lastIndexOf('" + custom_name + "') ==-1";
			} else if ("like".equals(operators_value)) {
				return "'[@sys_sccaption@]'.lastIndexOf('" + custom_name + "')!=-1";
			} else
				return "'[@sys_sccaption@]'" + operators_value + "'" + custom_name + "'";

		} else {
			return "'[@grant_code@]'=='" + grant_code + "'";
		}

	}

	/**
	 * 读取申请书.
	 * 
	 */
	@Override
	// @PreAuthorize("hasRole('A_MENU_PRP_01_01')")/*方法级别权限控制样本*/
	public Map<String, Object> getPrpLoad(Map<String, Object> params) throws ServiceException, UnknownHostException {
		params.put("formType", "proposal");
		params.put("org_code", new Long("10008608"));
		params.put("psn_code", new Long("100016830"));
		Long grantCode = Long.valueOf(params.get("grant_code").toString());

		Map<String, Object> resultMap = new HashMap<String, Object>();
		FormBaseLibrary fbl = null;
		params.put("actionType", 1);// 刷新级别为add
		fbl = initializeService.getFormBaseLibraryByGrantId(grantCode, null, null, params);
		resultMap.put("statYear", AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR));
		resultMap.put("fbl", fbl);
		resultMap.put("xmlData", fbl.getXmlData());
		return resultMap;

	}

/*	@Override
	public PageContainer getCustomRuleByGrantCode(ConditionContainer c) {
		String queryStr = "RuleSetting.getCustomRuleList";
		String countStr = "RuleSetting.getCustomRuleCount";
		PageContainer page = this.wfDefineBatisDao.getSearchPage(queryStr, countStr, c);
		return page;
	}*/

/*	@Override
	public PageContainer getPreviewRuleByGrantCode(ConditionContainer c) throws Exception {
		String queryStr = "RuleSetting.getPreviewRuleList";
		String countStr = "RuleSetting.getPreviewRuleCount";
		PageContainer page = this.wfDefineBatisDao.getSearchPage(queryStr, countStr, c);
		return page;
	}*/

	@Override
	public ProposalRule getRuleByGrandCodeAndXpath(Long grantCode, String xpath) throws Exception {
		String hql = " from ProposalRule p where p.grantCode = ? and p.xpath = ? and year = ?";
		List<ProposalRule> list = this.proposalRuleParamDao.find(hql, grantCode, xpath, AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR));
		if (list.size() <= 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public List<ProposalRule> getRuleListByGrantCode(Long grantCode) throws Exception {
		String hql = "from ProposalRule p where p.grantCode = ?  and year = ? ";
		List<ProposalRule> list = this.proposalRuleDao.find(hql, grantCode, AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR));
		return list;
	}

	/*@Override
	public PageContainer getPreviewRuleList(ConditionContainer c) throws Exception {
		c.getConditions().put("psn_code", SecurityUtils.getCurrentUserId());
		c.getConditions().put("role_id", SecurityUtils.getCurrentUserRoleId());
		return proposalRuleDao.getPreviewRuleList(c);
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public void deleteRuleCheckResults(Long id) {
		int year = Integer.valueOf(AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR));
		String sql = "select pr.prp_code from prp_rule_check_result pr inner join proposal p on p.prp_code = pr.prp_code"
				+ " where pr.rule_code= ? and p.status not in ('08', '09') and  p.stat_year= ?";
		List<Map<String, Object>> list = prpRuleCheckResultDao.queryForList(sql, new Object[] { id, year });
		if (list.size() > 0) {
			sql = "delete from prp_rule_check_result prc where (prc.rule_code,prc.prp_code) in " + " (select pr.rule_code,pr.prp_code from prp_rule_check_result pr "
					+ " inner join proposal p on p.prp_code = pr.prp_code" + " where pr.rule_code= ? and p.status not in ('08', '09') and  p.stat_year= ? )";
			prpRuleCheckResultDao.update(sql, new Object[] { id, year });

			for (Map<String, Object> map : list) {
				String prpCode = map.get("PRP_CODE").toString();
				prpRuleCheckResultDao.updatePrpExtendCnt(Long.valueOf(prpCode), "2");
			}
		}
	}

	@Override
	public void addRuleCheckResults(Long id, String ruleType) {
		prpRuleCheckResultDao.addRuleResult(id, ruleType);
	}

	@Override
	public List<ProposalRule> getProposalRulesByValidateStage(String validateStage, String grantCode) throws ServiceException {
		return this.proposalRuleDao.findRuleByValidateStage(validateStage, grantCode);
	}

	@Override
	public List<String> validate(Map<String, Object> params, List<ProposalRule> proposalRules) throws ServiceException {
		if (!params.containsKey("psn_code")) {
			params.put("psn_code", 0L/* SecurityUtils.getCurrentUserId()*/);
		}
		params.put("stat_year", AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR));
		List<String> errMsgList = new ArrayList<String>();

		// 应急处理 by jzw
		if (proposalRules != null) {

			for (ProposalRule prprule : proposalRules) {
				Map<String, Object> tempParams=proposalRuleParamService.parseEngRuleParam(params, proposalRuleParamDao.getParamsById(prprule.getId()));
				Boolean elResult = elCompnent.getElResult(prprule,tempParams);
				// 如果返回false，则说明不符合，然后返回
				if (!elResult) {
					String message = prprule.getMessage();
					//prprule = replaceParam(prprule);
					message = buildEl(prprule.getMessage(), tempParams);
					errMsgList.add(message);
					//prprule.setMessage(message);
				}
			}
		}
		return errMsgList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findValidateStageNames(String codes) throws ServiceException {
		StringBuffer sql = new StringBuffer();
		sql.append("select cd.zh_cn_caption as validateStageName  from const_dictionary cd where cd.category = 'validate_stage' and cd.code in (" + codes + ") order by cd.code");
		List<Map<String, Object>> list = proposalRuleDao.queryForList(sql.toString());
		List<String> validateStageNames = new ArrayList<String>();
		for (Map<String, Object> map : list) {
			String validateStageName = (String) map.get("VALIDATESTAGENAME");
			validateStageNames.add(validateStageName);
		}
		return validateStageNames;
	}

	@Override
	public List getRuleSettings(Long formCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("select *  from GRANT_RULE_SETTING grs where (grs.form_code=" + formCode + "  or form_code is null ) ");
		List<Map<String, Object>> list = proposalRuleDao.queryForList(sql.toString());
		return list;
	}

}
