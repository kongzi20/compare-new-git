package com.test.example.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

 
/**
 * 公共的业务层工具类.
 */
public class ServiceUtils {

	private static final Log LOGGER = LogFactory.getLog(ServiceUtils.class);

	private static final String UNICODE_EMAIL_CODE = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$";
	private static final String COMMON_EMAIL_CODE = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d)|(([a-z]|\\d)([a-z]|\\d|-|\\.|_|~)*([a-z]|\\d)))\\.)+(([a-z])|(([a-z])([a-z]|\\d|-|\\.|_|~)*([a-z])))\\.?$";
	private static final Pattern UNICODE_EMAIL_PATTENRN = Pattern.compile(UNICODE_EMAIL_CODE);
	private static final Pattern COMMON_EMAIL_PATTENRN = Pattern.compile(COMMON_EMAIL_CODE);

	/*
	 * 针对URL要求的 des3加密.
	 */
	public static String encodeToDes3(String str) {
		return ServiceUtils.encodeToDes3(str, ServiceConstants.ENCRYPT_KEY);
	}

	/**
	 * 针对URL要求的des3解密 .
	 */
	public static String decodeFromDes3(String str) {

		return ServiceUtils.decodeFromDes3(str, ServiceConstants.ENCRYPT_KEY);
	}

	/**
	 * 针对URL要求的 des3加密，指定加密KEY.
	 */
	public static String encodeToDes3(String str, String encryptKey) {
		try {
			if (StringUtils.isBlank(str)) {
				return null;
			}
			return java.net.URLEncoder.encode(EncryptionUtils.encrypt(encryptKey, str), "utf-8");
		} catch (Exception e) {
			LOGGER.warn("des3加密失败:" + str);
			return null;
		}
	}

	/**
	 * 针对URL要求的des3解密 ，指定加密KEY.
	 */
	public static String decodeFromDes3(String str, String encryptKey) {
		try {
			if (StringUtils.isBlank(str)) {
				return null;
			}
			String tmp = null;
			tmp = str.replace("+", "%2B");
			tmp = tmp.replace("=", "%3D");
			tmp = tmp.replace("%25", "%");
			return EncryptionUtils.decrypt(encryptKey, java.net.URLDecoder.decode(tmp, "utf-8"));
		} catch (Exception e) {
			LOGGER.warn("des3解密失败:" + str);
			return null;
		}
	}

	/**
	 * 获取List中的随机对象.
	 */
	public static <T> T randomArray(List<T> list) {
		if (list != null && list.size() > 0) {
			int size = list.size();
			if (size == 1) {
				return list.get(0);
			}
			int posit = RandomUtils.nextInt(size - 1);
			return list.get(posit);
		}
		return null;
	}

	/**
	 * 获取List中的随机对象.
	 * 
	 * @param exList
	 *            排除的对象，此处必须为list的子集.
	 */
	public static <T> T randomArray(List<T> list, List<T> exList) {
		if (list != null && list.size() > 0) {
			if (exList != null) {
				// 集合必须更大
				if (list.size() > exList.size()) {
					T data = randomArray(list);
					while (exList.contains(data)) {
						data = randomArray(list);
					}
					return data;
				}
			} else {
				return randomArray(list);
			}
		}
		return null;
	}

	/**
	 * 获取异常堆栈串.
	 */
	public static String getErrorTranceStr(Throwable e) {
		// 获取错误信息
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	/**
	 * 普通EMAIL校验.
	 */
	public static boolean isEmailValidate(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		if (!COMMON_EMAIL_PATTENRN.matcher(email).find()) {
			return false;
		}
		return true;
	}

	/**
	 * 带unicode的EMAIL校验，支持中文.
	 */
	public static boolean isUnicodeEmailValidate(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		if (!UNICODE_EMAIL_PATTENRN.matcher(email).find()) {
			return false;
		}
		return true;
	}

	/**
	 * 解析姓名的拼音.
	 */
	public static Map<String, String> parsePinYin(String cname) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			String firstName = "";
			String lastName = "";
			try {
				if (StringUtils.isNotBlank(cname)) {
					HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
					format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
					char[] names = cname.trim().toCharArray();
					for (int i = 0; i < names.length; i++) {
						String[] name;
						try {
							name = PinyinHelper.toHanyuPinyinStringArray(names[i], format);
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							name = null;
							e.printStackTrace();
						}
						if (i == 0) {
							if (name != null && name.length > 0) {
								lastName = name[0];
							}
						} else {
							if (name != null && name.length > 0) {
								firstName += " " + name[0];
							}
						}
					}
					if (!"".equals(firstName)) {
						firstName =  StringUtils.substring(firstName, 1, 21);
					}
				}
			} catch (Exception e) {
				LOGGER.warn("解析姓名的拼音失败:" + cname, e);
			}
			if (firstName != null && firstName.indexOf(" ") > -1) {
				String[] collFirstName = firstName.split(" ");
				StringBuffer fnBuf = new StringBuffer();
				for (String fn : collFirstName) {
					fnBuf.append(String.valueOf(fn.charAt(0)).toUpperCase()).append(fn.substring(1)).append(" ");
				}
				firstName = fnBuf.toString().trim();
			} else if (firstName != null && firstName.length() > 0) {
				firstName = String.valueOf(firstName.charAt(0)).toUpperCase() + firstName.substring(1);
			}
			if (lastName != null && lastName.indexOf(" ") > -1) {
				String[] collLastName = lastName.split(" ");
				StringBuffer lnBuf = new StringBuffer();
				for (String ln : collLastName) {
					lnBuf.append(String.valueOf(ln.charAt(0)).toUpperCase()).append(ln.substring(1)).append(" ");
				}
				lastName = lnBuf.toString().trim();
			} else if (lastName != null && lastName.length() > 0) {
				lastName = String.valueOf(lastName.charAt(0)).toUpperCase() + lastName.substring(1);
			}
			map.put("firstName", firstName);
			map.put("lastName", lastName);
			return map;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将逗号分隔的长整型字符串解析.
	 */
	public static List<Long> splitStrToLong(String str) {
		if (StringUtils.isNotBlank(str) && str.matches(ServiceConstants.IDPATTERN)) {
			String[] strs = str.split(",");
			List<Long> list = new ArrayList<Long>();
			for (String lng : strs) {
				list.add(Long.valueOf(lng));
			}
			return list;
		}
		return null;
	}

	/**
	 * 将逗号分隔的整型字符串解析.
	 */
	public static List<Integer> splitStrToInteger(String str) {
		if (StringUtils.isNotBlank(str) && str.matches(ServiceConstants.IDPATTERN)) {
			String[] strs = str.split(",");
			List<Integer> list = new ArrayList<Integer>();
			for (String lng : strs) {
				list.add(Integer.valueOf(lng));
			}
			return list;
		}
		return null;
	}

	/**
	 * 将逗号分隔的长整型加密字符串解析.
	 */
	public static List<Long> splitDesStrToLong(String str) {

		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] strArray = str.split(",");
		if (strArray.length > 0) {
			List<Long> list = null;
			for (String desid : strArray) {
				Long id = null;
				if (NumberUtils.isNumber(desid)) {
					id = Long.valueOf(desid);
				} else {
					String strId = ServiceUtils.decodeFromDes3(desid);
					if (NumberUtils.isNumber(strId)) {
						id = Long.valueOf(strId);
					}
				}
				if (id != null) {
					list = list == null ? new ArrayList<Long>() : list;
					list.add(Long.valueOf(id));
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 将逗号分隔的加密整型字符串解析.
	 */
	public static List<Integer> splitDesStrToInteger(String str) {

		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] strArray = str.split(",");
		if (strArray.length > 0) {
			List<Integer> list = null;
			for (String desid : strArray) {
				list = list == null ? new ArrayList<Integer>() : list;
				Integer id = null;
				if (NumberUtils.isNumber(desid)) {
					id = Integer.valueOf(desid);
				} else {
					String strId = ServiceUtils.decodeFromDes3(desid);
					if (NumberUtils.isNumber(strId)) {
						id = Integer.valueOf(strId);
					}
				}
				if (id != null) {
					list.add(Integer.valueOf(id));
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 将逗号分隔的长整型字符串解析.
	 */
	public static Set<Long> splitStrToLongSet(String str) {
		if (StringUtils.isNotBlank(str) && str.matches(ServiceConstants.IDPATTERN)) {
			String[] strs = str.split(",");
			Set<Long> set = new HashSet<Long>();
			for (String lng : strs) {
				set.add(Long.valueOf(lng));
			}
			return set;
		}
		return null;
	}

	/**
	 * 将逗号分隔的字符串解析.
	 */
	public static List<String> splitStrToList(String str) {
		if (StringUtils.isNotBlank(str)) {
			String[] strs = str.split(",");
			List<String> list = new ArrayList<String>();
			for (String strtp : strs) {
				if (StringUtils.isNotBlank(strtp)) {
					list.add(strtp);
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 将逗号分隔的字符串加密字符串解析.
	 */
	public static List<String> splitDesStrToList(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] strArray = str.split(",");
		if (strArray.length > 0) {
			List<String> list = null;
			for (String strTp : strArray) {
				String tmp = ServiceUtils.decodeFromDes3(strTp);
				if (StringUtils.isNotBlank(tmp)) {
					list = list == null ? new ArrayList<String>() : list;
					list.add(tmp);
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 加密code 并且加上时间戳
	 * 
	 * @param code
	 * @param 时间长度
	 * @return
	 */
	public static String encodeDesCode(Long code) {
		if (code == null) {
			return null;
		}
		return encodeDesCode(code.toString());
	}

	/**
	 * 加密code 并且加上时间戳
	 */
	public static String encodeDesCode(String code) {
		if (StringUtils.isBlank(code)) {
			return null;
		}
		return encodeToDes3(code.toString() + "|" + (new Date()).getTime());
	}

	/**
	 * 将以List按一定大小划分为多个List. <br/>
	 * <p>
	 * <table align="center" border="1">
	 * <tr>
	 * <td rowspan="2">参数</td>
	 * <td>arrStr = ["112", "223", "1", "2", "11123", "111", "33", "24"]</td>
	 * </tr>
	 * <tr>
	 * <td>sectionSize = 3</td>
	 * </tr>
	 * <tr>
	 * <td>执行结果</td>
	 * <td>[["112", "223", "1"], ["2", "11123", "111"], ["33", "24"]]</td>
	 * </tr>
	 * </table>
	 * </p>
	 * 
	 * @param arr
	 *            List集合
	 * @param sectionSize
	 *            划分区间容量size，默认为1000
	 * @return
	 */
	public static List<List<Long>> splitLongListBySection(List<Long> arr, int sectionSize) {
		List<List<Long>> arrList = new ArrayList<List<Long>>();

		if (arr == null) {
			return arrList;
		}
		if (sectionSize <= 0 || sectionSize > 1000) {
			sectionSize = 1000;
		}

		int count = arr.size() % sectionSize == 0 ? arr.size() / sectionSize : arr.size() / sectionSize + 1;
		for (int i = 0; i < count; i++) {
			int maxLen = (i + 1) * sectionSize;
			if (i + 1 == count && arr.size() % sectionSize != 0) {
				maxLen = arr.size();
			}
			// List<Long> arrTmp = new ArrayList<Long>();
			// for (int j = i * sectionSize; j < maxLen; j++) {
			// arrTmp.add(arr.get(j));
			// }
			// arrList.add(arrTmp);
			// 优化
			arrList.add(arr.subList(i * sectionSize, maxLen));
		}
		return arrList;
	}

	public static void main(String[] args) {
		// String[] s = "|23|".split("|");
		// System.out.println(s[1]);
		// System.out.println(decodeFromDes3("83ky%2Bi7rqPRVOEePOFb7OQ%3D%3D"));
		
		List<Integer> list = new ArrayList<Integer>();  
        list.add(1);  
        list.add(2);  
        list.add(3);  
          
        List<Integer> sub = list.subList(0, 2);  
        sub.add(5);  
          
        for (Integer i : sub) {  
            System.out.print(i.intValue() + "\t");  
        }  
	}
}
