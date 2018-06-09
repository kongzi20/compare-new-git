package com.test.example.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
/**
 * 相似度比较工具类
 * @author Administrator
 *
 */
public class SimilarityUtils {

	private static String  regex="\\s|[,，]|[。]";

	public  static  double calculateSimilaryByClause(String sourceContent, String targetContent,int filterLength,List<String> filterWord) {
		return calculateSimilaryByClause( sourceContent,  targetContent, filterLength,filterWord,false);
	}
	/**
	 * lyj modify 2014-5-15
	 * 按照子句划分得到的相似度
	 * @param sourceContent 源文本
	 * @param targetContent 目标文本
	 * @param filterLength 过来字符长度
	 * @param filterWord 过滤词组，其中内容不做比较
	 * @param splitByChar 是否按单个字符比较
	 * @return
	 */
	public  static  double calculateSimilaryByClause(String sourceContent, String targetContent,int filterLength,List<String> filterWord,Boolean splitByChar) {
	    Map<String, Integer> ifreq = segmentByClause(sourceContent,filterLength,filterWord,splitByChar);
	    Map<String, Integer> jfreq = segmentByClause(targetContent,filterLength,filterWord,splitByChar);

	    double ijSum = 0;
	    Iterator<Entry<String, Integer>> it = ifreq.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> entry = it.next();
	       // // System.out.println(entry.getKey()+"|");
	        if(jfreq.containsKey(entry.getKey())) {
	        	int len = StringUtils.length(entry.getKey()); //字符串长度;
	            double iw = weight(entry.getValue()*len);
	            double jw = weight(jfreq.get(entry.getKey())*len);
	            ijSum += (iw * jw);
	        }
	    }
	    
	    double iPowSum = powSum(ifreq);
	    double jPowSum = powSum(jfreq);
	    
	    return ijSum / (iPowSum * jPowSum);
	}

	public static  List<String> getSameList(String sourceContent, String targetContent,int filterLength,List<String> filterWord){ 
		return getSameList(sourceContent, targetContent, filterLength, filterWord, false);
	}
	/**
	 * lyj modify 2014-5-15
	 * 按照子句划分得到的相同字符串
	 * @param sourceContent
	 * @param targetContent
	 * @param filterLength
	 * @param filterWord 过滤词组，其中内容不做比较
	 * @param splitByChar 是否按单个字符比较
	 * @return
	 */
	public static  List<String> getSameList(String sourceContent, String targetContent,int filterLength,List<String> filterWord, boolean splitByChar){
		Map<String, Integer> ifreq = segmentByClause(sourceContent, filterLength, filterWord, splitByChar);
		Map<String, Integer> jfreq = segmentByClause(targetContent, filterLength, filterWord, splitByChar);
		List<String> sameList = new ArrayList<String>();
		Iterator<Entry<String, Integer>> it = ifreq.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = it.next();
			String keyStr = entry.getKey();
			//大小写
			if (jfreq.containsKey(testStringUtils.trimWhitespace(keyStr))
					|| jfreq.containsKey(testStringUtils.trimWhitespace(keyStr).toLowerCase())
					|| jfreq.containsKey(testStringUtils.trimWhitespace(keyStr).toUpperCase())) {
				sameList.add(entry.getKey());
			}
			//全角半角
			else if (jfreq.containsKey(testStringUtils.full2Half(testStringUtils.trimWhitespace(keyStr)))
					|| jfreq.containsKey(testStringUtils.half2Full(testStringUtils.trimWhitespace(keyStr)))) {
				sameList.add(entry.getKey());
			}
			
		}
		return sameList;
	}

	private static double powSum(Map<String, Integer> mapfreq) {
		double sum = 0;
	    Iterator<Entry<String, Integer>> it = mapfreq.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> entry = it.next();
	        int len = StringUtils.length(entry.getKey());
	        double dw = weight(entry.getValue()*len);
	        sum += Math.pow(dw, 2);
	    }
	    return Math.sqrt(sum);
	}

	/**
	 * 计算词项特征值
	 * @param wordfreq
	 * @return
	 */
	private  static   double weight(float wordfreq) {
	    return Math.sqrt(wordfreq);
	}


	@SuppressWarnings("unused")
	private  static   Map<String, Integer> segmentByClause(String content,int filterLength,List<String> filterWord) {
		return segmentByClause( content, filterLength, filterWord, false);
	}
	
	/**
	 * lyj modify 2014-5-15
	 * 对字符串进行按句分词
	 * @param content 待处理文本
	 * @param filterLength 最小处理长度
	 * @param filterWord 过滤词组，其中内容不做比较
	 * @param splitByChar 是否按单个字符比较
	 * @return
	 */
	private static Map<String, Integer> segmentByClause(String content, int filterLength, List<String> filterWord, Boolean splitByChar) {
		//wk modify 2014-9-25 过滤换行
		String content2 = content.replaceAll("[\\s\\n]", "");
		if (content2 == null || content2.isEmpty()) {
			return null;
		}
		String[] contents = splitToStrAry(content2);
		if (splitByChar)
			contents = splitToCharAry(content2);
		Map<String, Integer> mapfreq = new HashMap<String, Integer>();
		try {

			for (int i = 0; i < contents.length; i++) {
				String str = contents[i];
				str = org.springframework.util.StringUtils
						.trimAllWhitespace(str);
				if (StringUtils.isNotBlank(str) && str.length() > filterLength
						&& (filterWord == null || !filterWord.contains(str))) {
					if (!mapfreq.containsKey(str)) {
						mapfreq.put(str, 1);
						continue;
					}
					int freq = mapfreq.get((str));
					mapfreq.put(str, ++freq);
				}

			}

		} catch (Exception e) {

			return null;
		}

		return mapfreq;
	}
	/**
	 * 拆分字符串
	 * @param content
	 * @return
	 */
	public static  String [] splitToStrAry(String content){
		if(StringUtils.isBlank(content)){
			return null;
		}
		//按照空格或逗号分隔
		String [] result=content.split( regex);
		 return result;

	}
	/**
	 * lyj add 2014-5-15
	 * 拆分字符串，单个字符拆分
	 * @param content
	 * @return
	 */
	public static  String [] splitToCharAry(String content){
		if(StringUtils.isBlank(content)){
			return null;
		}
		//按照空格或逗号分隔
		String [] tmp=content.split( "");
		String [] result = Arrays.copyOfRange(tmp,1,tmp.length);
		 return result;

	}
	/**
	 * 拆分字符串,并把拆分符号也包含在内
	 * @param content
	 * @return
	 */
	public static List<String> splitToList(String content){
		List<String> matchList=new ArrayList<String>();
		if(StringUtils.isBlank(content)){
			return matchList;
		}
		int index = 0;
		content=content+" ";
		//wk modify 2014-9-25 过滤换行
		Pattern pattern = Pattern.compile("[,，]|[。]");
		Matcher m = pattern.matcher(content);
		while(m.find()){
			 String match = content.subSequence(index, m.start()).toString();
			 if(StringUtils.isNotBlank(match)){
				 matchList.add(match);
			 }
             matchList.add(m.group());
             index = m.end();
             
            
		}
		//无法拆分时返回整体
		if(matchList.size() == 0) 
			matchList.add(content);
		return matchList;
	}
	
	/**
	 * 拆分字符串,并把拆分符号也包含在内
	 * @param content
	 * @return
	 */
	public static List<String> splitCharToList(String content){
		List<String> matchList=new ArrayList<String>();
		if(StringUtils.isBlank(content)){
			return matchList;
		}
		int index = 0;
		content=content+" ";
		Pattern pattern = Pattern.compile("");
		Matcher m = pattern.matcher(content);
		while(m.find()){
			 String match = content.subSequence(index, m.start()).toString();
			 matchList.add(match);
             index = m.end();
		}
		return matchList;
	}
	
	public static  String replaceSameContent(String content,List<String> sameList,String fontColor,String backgroundColor,boolean mark){
		return replaceSameContent( content, sameList, fontColor, backgroundColor, mark, false);
	}
	
	/**
	 * 把相同的文字高亮显示
	 * @param content 需要替换的内容
	 * @param sameList 相同文字的结果集
	 * @param fontColor 字体颜色
	 * @param backgroundColor 背景颜色
	 * @param mark 是否需要加瞄点标记
	 * @param splitByChar 是否按单个字符比较
	 * @return
	 */
	public static String replaceSameContent(String content, List<String> sameList, String fontColor, String backgroundColor,
			boolean mark, boolean splitByChar) {
		if (StringUtils.isBlank(content) || sameList == null || sameList.size() == 0) {
			return content;
		}
		List<String> list = splitToList(content);
		if (splitByChar)
			list = splitCharToList(content);
		StringBuffer result = new StringBuffer();
		for (String str : list) {
			// 大小写 wk modify 2014-9-25 过滤换行
			String str2 = str.replace("\n", "");
			if (sameList.contains(testStringUtils.trimAllWhitespace(str2))
					|| sameList.contains(testStringUtils.trimAllWhitespace(str2.toLowerCase()))
					|| sameList.contains(testStringUtils.trimAllWhitespace(str2.toUpperCase()))) {
				result.append(str.replace(str,highlightStr(str, fontColor, backgroundColor, mark)));
			}
			// 全角半角
			else if (sameList.contains(testStringUtils.full2Half(testStringUtils.trimAllWhitespace(str2)))
					|| sameList.contains(testStringUtils.half2Full(testStringUtils.trimAllWhitespace(str2)))) {
				result.append(str.replace(str,highlightStr(str, fontColor, backgroundColor, mark)));
			}
			// 大小写与全角半角
			else if(sameList.contains(testStringUtils.full2Half(testStringUtils.trimAllWhitespace(str2.toUpperCase())))
					|| sameList.contains(testStringUtils.half2Full(testStringUtils.trimAllWhitespace(str2.toUpperCase())))
					|| sameList.contains(testStringUtils.full2Half(testStringUtils.trimAllWhitespace(str2.toLowerCase())))
					|| sameList.contains(testStringUtils.half2Full(testStringUtils.trimAllWhitespace(str2.toLowerCase())))){
				result.append(str.replace(str,highlightStr(str, fontColor, backgroundColor, mark)));
			}
			else {
				result.append(str);
			}
		}
		return result.toString();
	}

	private static  String highlightStr(String str, String fontColor,
			String backgroundColor,boolean mark) {
		/*
		 * 将&lt;br&gt;去掉，以\n替换
		 */
		String[] strArr = str.split("&lt;br&gt;");
		String newStr = "";
		int value=testStringUtils.trimWhitespace(str).hashCode();
		if(mark){
			for (String s : strArr) {
				if (strArr.length == 1) {
					newStr = ("<a href=\"#"+value+"\" class=\"sameStr\"><span id="+value+"_0   style=\"color: " + fontColor + "; cursor:pointer; background-color: "
							+ backgroundColor + "\">" + s + "</span></a>");
				} else {
					newStr += "\n"+("<a href=\"#"+value+"\" class=\"sameStr\" ><span id="+value+"_0 style=\"color: " + fontColor + ";cursor:pointer; background-color: "
							+ backgroundColor + "\">" + s + "</span></a>");
				}

			}
		}else{
			for (String s : strArr) {
				if (strArr.length == 1) {
					newStr = ("<span id="+value+"  style=\"color: " + fontColor + ";  background-color: "
							+ backgroundColor + "\">" + s + "</span>");
				} else {
					newStr += "\n"+("<span  style=\"color: " + fontColor + ";background-color: "
							+ backgroundColor + "\">" + s + "</span>");
				}
			}
		}

		if (!newStr.equals("") && strArr.length > 1) {
			newStr = newStr.substring(1);
		}
		return newStr;

	}
	
	public static void main(String[] args) {
		String c1 = "最多使用的应为encodeURIComponent，它是将中文、韩文等特殊字，所以如果给后台传递参数需要使用encodeURIComponent时需要后台解码对utf-8支持（form方式和当前页面编码方式相同";
		String c2 = "使用的应为encodeURIComponent，它是将中文、韩文等特殊字，所以如果给后台传递参数需要使用encodeURIComponent时需要码对utf-8支持（form方式和当前页面编码方式相同";
		double s = calculateSimilaryByClause(c1, c2, 4 , null);
		// System.out.println(s);
	}

}
