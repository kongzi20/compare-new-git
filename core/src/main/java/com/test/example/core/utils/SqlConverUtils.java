package com.test.example.core.utils;

import java.util.HashMap;
/*import java.util.HashSet;
import java.util.Iterator;
import java.util.List;*/
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/*import com.test.example.code.compare.constant.AppSettingConstants;
import com.test.example.code.compare.service.AppSettingContext;*/

/**
 * 本工具类是实现sql语句中变量的替换.
 * 
 * 
 * @version $Rev$ $Date$
 */
public class SqlConverUtils {

	/**
	 * 通过业务类传过来的参数对SQL中对应的参数进行替换,SQL中参数格式为[@参数名@].
	 * 
	 * @param sql
	 *            sql语句
	 * @param map
	 *            用于替换sql语句中特殊字符"[@ @]" 的map
	 * @param params
	 *            执行sql语句时传入的占位符变量
	 * @return
	 */
	/*public static String transSql(String sql, Map<String, Object> map, List<Object> params) {

		// 常量map.存放常用值.如: psnCode,orgCode等
		Map<String, Object> constMap = new HashMap<String, Object>();
		// roleId
		constMap.put("roleid", SecurityUtils.getCurrentUserRoleId());
		constMap.put("role_id", SecurityUtils.getCurrentUserRoleId());
		// psnCode
		constMap.put("psncode", SecurityUtils.getCurrentUserId());
		constMap.put("psn_code", SecurityUtils.getCurrentUserId());
		// 单位管理员单位code
		constMap.put("orgcode", SecurityUtils.getAdminOrgCode());
		constMap.put("org_code", SecurityUtils.getAdminOrgCode());
		// 单位code
		constMap.put("currentorgcode", SecurityUtils.getCurrentOrgCode());
		constMap.put("current_org_code", SecurityUtils.getCurrentOrgCode());
		// 部门code
		constMap.put("dept_code", SecurityUtils.getCurrentDeptCode());
		// 当前年份
		constMap.put("curryear", AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR));
		// 除了申请相关的年度(如资助计划书和进展结题报告等)
		constMap.put("select_year", AppSettingContext.getValue(AppSettingConstants.SELECT_YEAR));
		constMap.put("assignee", SecurityUtils.getCurrAssignee());
		if (map != null) {
			// 加入常量map，如果有相同的key则会覆盖
			// 以传入进来的map为主
			constMap.putAll(map);
		}
		mapKey2Lowercase(constMap);
		
		// 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
		Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);
		Matcher m = p.matcher(sql);
		String key;
		String paramKey;

		while (m.find()) {
			key = m.group().toLowerCase();
			paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
			sql = testStringUtils.regexReplaceString(sql, "\\[@" + paramKey + "@\\]", "?");
			params.add(constMap.get(paramKey));
		}
		return sql;
	}*/

	/**
	 * 转换为hibernate 可识别ql字符串并给出参数集map.
	 * 
	 * @author lineshow created on 2011-12-8
	 * @param ql
	 * @param map
	 *            Map<key,Object>
	 * @param params
	 *            list<key>
	 * @return new ql
	 */
	/*public static String compress2RuledQl(String ql, Map<String, Object> map, List<Object> params) {
		Map<String, Object> constMap = new HashMap<String, Object>();

		constMap.put("psncode", SecurityUtils.getCurrentUserId());// 人员code
		constMap.put("psn_code", SecurityUtils.getCurrentUserId());
	
		constMap.put("roleCode", SecurityUtils.getCurrentUserRoleId());// 角色code
		constMap.put("role_code", SecurityUtils.getCurrentUserRoleId());
		
		constMap.put("roleId", SecurityUtils.getCurrentUserRoleId());
		constMap.put("role_id", SecurityUtils.getCurrentUserRoleId());
		// 单位管理员单位code
		constMap.put("orgcode", SecurityUtils.getAdminOrgCode());
		constMap.put("org_code", SecurityUtils.getAdminOrgCode());
		// 单位code
		constMap.put("currentorgcode", SecurityUtils.getCurrentOrgCode());
		constMap.put("current_org_code", SecurityUtils.getCurrentOrgCode());
		constMap.put("curryear", AppSettingContext.getValue(AppSettingConstants.SYNCDATA_STAT_YEAR)); // 当前年份

		if (map == null) {
			map = constMap;
		} else {
			constMap.putAll(map);
			map.putAll(constMap);
		}
		mapKey2Lowercase(map);
		List<String> keyList = testStringUtils.extractFromSpecStr(ql, null, -1);
		Set<String> setList = new HashSet<String>(keyList);
		Iterator<String> keyIt = setList.iterator();
		Map<String, Object> conditionMap = new HashMap<String, Object>();

		while (keyIt.hasNext()) {
			String key = keyIt.next().toLowerCase();
			String replacedKey = "\\[@" + key + "@\\]";
			String replaceKey = "null";
			if (map.containsKey(key)) {
				replaceKey = ":" + key;
				// ql = testStringUtils.regexReplaceString(ql, replacedKey, replaceKey);
				conditionMap.put(key, map.get(key));
			} else {
				keyIt.remove();
			}
			ql = testStringUtils.regexReplaceString(ql, replacedKey, replaceKey);
		}

		map.clear();
		map.putAll(conditionMap); // 对应ql的参数集合
		params.addAll(map.values());

		return ql;
	}*/

	/**
	 * 替换字符串中存在的[@参数名@]格式.
	 * 
	 * @param str
	 * @param map
	 * @return
	 */
	public static String strConvert(String str, Map<?, ?> map) {
		if (map == null) {
			return str;
		}
		Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);// 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
		Matcher m = p.matcher(str);
		String key;
		String paramKey;

		while (m.find()) {
			key = m.group();
			paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]，同时将密码明文用<span></span>括起来
			if (map.get(paramKey) != null && !"".equals(map.get(paramKey))) {
				str = testStringUtils.regexReplaceString(
						str,
						"\\[@" + paramKey + "@\\]",
						StringUtils.equalsIgnoreCase(paramKey, ServiceConstants.PASSWORD) ? "<span>"
								+ map.get(paramKey) + "</span>" : map.get(paramKey));
			} else if (map.get(paramKey.toUpperCase()) != null && !"".equals(map.get(paramKey.toUpperCase()))) {
				str = testStringUtils.regexReplaceString(
						str,
						"\\[@" + paramKey + "@\\]",
						StringUtils.equalsIgnoreCase(paramKey, ServiceConstants.PASSWORD) ? "<span>"
								+ map.get(paramKey.toUpperCase()) + "</span>" : map.get(paramKey.toUpperCase()));
			}

		}
		return str;

	}

	/**
	 * sqlMap替换。主要用于sql配置查询的结果集替换（大写替换）.
	 * 
	 * @param result
	 *            需要替换的map。
	 * @param sqlMap
	 *            执行sql语句返回的键值对结果集(里面的key是大写)
	 * @return
	 */
	public static Map<String, Object> sqlMapReplace(Map<String, Object> result, Map<?, ?> sqlMap) {
		Set<String> keySet = result.keySet();
		for (Object element : keySet) {
			String key = (String) element;
			String value = (String) result.get(key);
			value = strConvert(value, sqlMap);
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 将map key lowercase处理.
	 * 
	 * @author lineshow created on 2011-12-8
	 * @param oldMap
	 * @return
	 */
	public static Map<String, Object> mapKey2Lowercase(Map<String, Object> oldMap) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : oldMap.entrySet()) {
			newMap.put(entry.getKey().toLowerCase(), entry.getValue() instanceof java.lang.String ? entry.getValue()
					.toString().trim() : entry.getValue());
		}
		oldMap.clear();
		oldMap.putAll(newMap);
		return oldMap;
	}

}
