package com.test.example.code.compare.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.example.code.compare.constant.CompareConstants;
import com.test.example.code.compare.model.ProposalExtend;
import com.test.example.core.utils.DateFormator;
import com.test.example.core.utils.DateUtils;
import com.test.example.core.utils.IrisStringUtils;
import com.test.example.core.utils.XMLHelper;

 

/**
 * 核心团队比对实现类
 * data/zh_persons/zh_person（证据类型card_type_value+card_type_name，证据号码card_code）
 * @author wk
 *
 */
@Service("psnCompareTemplateServiceImpl")
public class PsnCompareTemplateServiceImpl extends DataFilterJavaSupport implements CompareTemplateService {
	
	@Autowired
	private com.test.example.core.utils.XMLHelperCacheable XMLHelperCacheable;
	
	@Autowired
	private ProposalExtendService proposalExtendService;

	@Override
	/**
	 * 抽取方法，返回值为xml格式String
	 */
	public String extractCompareSource(long prpCode) {
		String content = "";
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:抽取核心团队==========");
		
		ProposalExtend pe = proposalExtendService.getProposalExtendByPrpCode(prpCode);
		//1.获取xml文档
		Document doc = XMLHelperCacheable.parseDoc(pe.getPrpXML());
		//2.抽取人员大节点
		Node zh_persons = doc.selectSingleNode(CompareConstants.PSN_EXTRACT_XPATH);
		if(zh_persons != null) {
			content= zh_persons.asXML();
		}
		
		// System.out.println(content);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:抽取核心团队==========");
		return content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double compareContent(String sourceContent, String targetContent) {
		Double similarity = 0.0;
		double sameNum = 0.0;
		if(!StringUtils.isNotBlank(sourceContent) || !StringUtils.isNotBlank(targetContent)) {
			return similarity;
		}
		
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:核心团队相似度比对==========");
		//1.解析xml
		List<Element> sourceList = XMLHelperCacheable.parseDocument(sourceContent).selectNodes(CompareConstants.PSN_COMPARE_XPATH);
		List<Element> targetList = XMLHelperCacheable.parseDocument(targetContent).selectNodes(CompareConstants.PSN_COMPARE_XPATH);
		List<Element> delList = new ArrayList<Element>();
		if(sourceList.size() == 0 || targetList.size() == 0) {
			return similarity;
		}
		//2.比较数据：证件类型+证件号码
		for (Element src : sourceList) {
			String srcCardTypeValue = src.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY1).trim();
			String srcCardCode = IrisStringUtils.full2Half(src.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY2)).trim();
			for (Element tge : targetList) {
				String tgeCardTypeValue = tge.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY1).trim();
				String tgeCardTypeName = tge.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY3).trim();
				String tgeCardCode = IrisStringUtils.full2Half(tge.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY2)).trim();
				//证件类型为其他以及证件号码为：无、暂无则不参与检查
				if(("其他".equals(tgeCardTypeName) || "无".equalsIgnoreCase(tgeCardCode) || "暂无".equalsIgnoreCase(tgeCardCode) )) {
					delList.add(tge);
					continue;
				}
				
				if(srcCardTypeValue.equalsIgnoreCase(tgeCardTypeValue) && srcCardCode.equalsIgnoreCase(tgeCardCode)) {
					sameNum++;
					delList.add(tge);
					break;
				}
			}
			targetList.removeAll(delList);
		}
		// 3.计算相似度:相同条数*2/表单条数之和
		similarity = sameNum * 2 / (sourceList.size() + targetList.size() + delList.size()) ;
		//similarity = (double) Math.round(similarity * 10000) / 10000; // 保留小数点后4位
		
		// System.out.println("核心团队相似度：" + similarity);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:核心团队相似度比对==========");
		return similarity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> renderDiffer(String sourceContent, String targetContent) {
		Map<String, String> map = new HashMap<String, String>();
		if(!StringUtils.isNotBlank(sourceContent) || !StringUtils.isNotBlank(targetContent)) {
			map.put("sourceContent", sourceContent);
			map.put("targetContent", targetContent);
			return map;
		}
		
		//1.解析xml
		Document sourceDoc = XMLHelper.parseDocument(sourceContent);
		Document targetDoc = XMLHelper.parseDocument(targetContent);
		List<Element> sourceList = sourceDoc.selectNodes(CompareConstants.PSN_COMPARE_XPATH);
		List<Element> targetList = targetDoc.selectNodes(CompareConstants.PSN_COMPARE_XPATH);
		List<Element> delList = new ArrayList<Element>();
		if(sourceList.size() == 0 || targetList.size() == 0) {
			return null;
		}
		int sameNum = 0;//相同条目数，用于获取颜色
		//2.比较数据：设备名称+设备型号
		for (Element src : sourceList) {
			String srcCardTypeValue = src.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY1).trim();
			String srcCardCode = IrisStringUtils.full2Half(src.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY2)).trim();
			for (Element tge : targetList) {
				String tgeCardTypeValue = tge.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY1).trim();
				String tgeCardTypeName = tge.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY3).trim();
				String tgeCardCode = IrisStringUtils.full2Half(tge.element("basic_info").elementText(CompareConstants.PSN_COMPARE_KEY2)).trim();
				//证件类型为其他以及证件号码为：无、暂无则不参与检查
				if(("其他".equals(tgeCardTypeName) || "无".equalsIgnoreCase(tgeCardCode) || "暂无".equalsIgnoreCase(tgeCardCode) )) {
					delList.add(tge);
					continue;
				}
				if(srcCardTypeValue.equalsIgnoreCase(tgeCardTypeValue) && srcCardCode.equalsIgnoreCase(tgeCardCode)) {
					//相同条目添加标识，页面进行渲染
					src.addAttribute("high_light", Integer.toString(sameNum));
					tge.addAttribute("high_light", Integer.toString(sameNum));

					delList.add(tge);
					sameNum++;
					break;
				}
			}
			targetList.removeAll(delList);
		}
		//3.返回处理后的文本
		sourceContent = sourceDoc.asXML();
		targetContent = targetDoc.asXML();
		
		map.put("sourceContent", sourceContent);
		map.put("targetContent", targetContent);
		return map;
	}

}
