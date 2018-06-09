package com.test.example.code.compare.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.example.code.compare.constant.CompareConstants;
import com.test.example.code.compare.model.ProposalExtend;
import com.test.example.core.utils.DateFormator;
import com.test.example.core.utils.DateUtils;
import com.test.example.core.utils.testStringUtils;
import com.test.example.core.utils.SimilarityUtils;
import com.test.example.core.utils.XMLHelper;

/**
 * 奖励类项目比对实现类
 * @author dhj
 *
 */
@Service("prizeCompareTemplateServiceImpl")
public class PrizeCompareTemplateServiceImpl extends DataFilterJavaSupport implements CompareTemplateService {
	
	@Autowired
	private com.test.example.core.utils.XMLHelperCacheable XMLHelperCacheable;

	@Autowired
	private ProposalExtendService proposalExtendService;
	
	private String FITER_WORD_REG = "\\{|\\}|\\(|\\)|<|>|【|】|（|）|《|》|\\[|\\]";
	
	@SuppressWarnings("unchecked")
	@Override
	public String extractCompareSource(long prpCode) {
		String content = "";
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:抽取奖励类项目比对内容==========");
		
		
		ProposalExtend pe = proposalExtendService.getProposalExtendByPrpCode(prpCode);
		//1.获取xml文档
		Document doc = XMLHelperCacheable.parseDoc(pe.getPrpXML());
		//2.抽取节点
		Element zh_title = (Element) doc.selectSingleNode(CompareConstants.PRPNAME_EXTRACT_XPATH);//项目名称
		Element prp_content = (Element) doc.selectSingleNode(CompareConstants.PRPCONTENT_EXTRACT_XPATH);//项目简介
		Element zl_no = (Element) doc.selectSingleNode(CompareConstants.ZHUANLI_EXTRACT_XPATH_NUMBER);//专利号
		Element zl_date = (Element) doc.selectSingleNode(CompareConstants.ZHUANLI_EXTRACT_XPATH_DATE);//授权日期
		Element zl_qlr = (Element) doc.selectSingleNode(CompareConstants.ZHUANLI_EXTRACT_XPATH_QLR);//权利人
		Element zl_fmr = (Element) doc.selectSingleNode(CompareConstants.ZHUANLI_EXTRACT_XPATH_FMR);//发明人
		
	/*	Element content1 = (Element) doc.selectSingleNode(CompareConstants.ZHUANLI_EXTRACT_XPATH_CONTENT1);//主要科技创新
		Element content2 = (Element) doc.selectSingleNode(CompareConstants.ZHUANLI_EXTRACT_XPATH_CONTENT2);
		Element content3 = (Element) doc.selectSingleNode(CompareConstants.ZHUANLI_EXTRACT_XPATH_CONTENT3);
		Element content4 = (Element) doc.selectSingleNode(CompareConstants.ZHUANLI_EXTRACT_XPATH_CONTENT4);*/
		
		//2.抽取专利大节点
		List<Element> zlList = doc.selectNodes(CompareConstants.ZHUANLI_EXTRACT_XPATH_LIST);
		
		//论文节点
		List<Element> paperList = doc.selectNodes(CompareConstants.PAPER_EXTRACT_XPATH_LIST);
		
		//人员节点
		Element pname = (Element) doc.selectSingleNode(CompareConstants.PSN_EXTRACT_XPATH_FIRST_NAME);
		Element gender = (Element) doc.selectSingleNode(CompareConstants.PSN_EXTRACT_XPATH_FIRST_GENDER);
		Element posi = (Element) doc.selectSingleNode(CompareConstants.PSN_EXTRACT_XPATH_FIRST_POSI);
		Element title = (Element) doc.selectSingleNode(CompareConstants.PSN_EXTRACT_XPATH_FIRST_TITLE);
		Element tvalue = (Element) doc.selectSingleNode(CompareConstants.PSN_EXTRACT_XPATH_FIRST_TYPE_VALUE);
		Element type = (Element) doc.selectSingleNode(CompareConstants.PSN_EXTRACT_XPATH_FIRST_TYPE);
		Element code = (Element) doc.selectSingleNode(CompareConstants.PSN_EXTRACT_XPATH_FIRST_CODE);
		
		List<Element> personList = doc.selectNodes(CompareConstants.PSN_EXTRACT_XPATH_LIST);
		
		//3.组装xml
		Document newDoc = DocumentHelper.createDocument();
		Element root = newDoc.addElement("data");
		root.addElement("zh_title").setText(zh_title == null ? "" : testStringUtils.full2Half(zh_title.getText().trim()));//全角转半角
		root.addElement("prp_content").setText(prp_content == null ? "" : testStringUtils.full2Half(prp_content.getText().trim()));
		root.addElement("zl_no").setText(zl_no == null ? "" : testStringUtils.full2Half(zl_no.getText().trim()));
		/*root.addElement("content1").setText(content1 == null ? "" : testStringUtils.full2Half(content1.getText().trim()));
		root.addElement("content2").setText(content2 == null ? "" : testStringUtils.full2Half(content2.getText().trim()));
		root.addElement("content3").setText(content3 == null ? "" : testStringUtils.full2Half(content3.getText().trim()));
		root.addElement("content4").setText(content4 == null ? "" : testStringUtils.full2Half(content4.getText().trim()));*/
		if(pname != null){
			Element rootP = root.addElement("zh_persons").addElement("list");
			rootP.addAttribute("seq_no", Integer.toString(1));
				rootP.addElement("zh_name").setText(pname == null ? "" : testStringUtils.full2Half(pname.getText().trim()));
				rootP.addElement("gender_name").setText(gender == null ? "" : testStringUtils.full2Half(gender.getText().trim()));
				rootP.addElement("position").setText(posi == null ? "" : testStringUtils.full2Half(posi.getText().trim()));
				rootP.addElement("prof_title_name").setText(title == null ? "" : testStringUtils.full2Half(title.getText().trim()));
				rootP.addElement("card_type_value").setText(tvalue == null ? "1" : testStringUtils.full2Half(tvalue.getText().trim()));
				rootP.addElement("card_type_name").setText(type == null ? "身份证" : testStringUtils.full2Half(type.getText().trim()));
				rootP.addElement("card_code").setText(code == null ? "" : testStringUtils.full2Half(code.getText().trim()));
		}
		
		if(personList.size() != 0) {
			int num = 2;
			for(Element zl:personList){
				Element root1 = root.addElement("zh_persons").addElement("list");
				root1.addAttribute("seq_no", Integer.toString(num));
				
					Element root2 = (Element) zl.selectSingleNode("basic_info");
					String zl_1 = root2.elementText("zh_name").trim();
					String zl_2 = root2.elementText("gender_name").trim();
					String zl_3 = root2.elementText("position").trim();
					String zl_4 = root2.elementText("prof_title_name").trim();
					String zl_5 = root2.elementText("card_type_value").trim();
					String zl_6 = root2.elementText("card_type_name").trim();
					String zl_7 = root2.elementText("card_code").trim();
					root1.addElement("zh_name").setText(zl_1 == null ? "" : testStringUtils.full2Half(zl_1));
					root1.addElement("gender_name").setText(zl_2 == null ? "" : testStringUtils.full2Half(zl_2));
					root1.addElement("position").setText(zl_3 == null ? "" : testStringUtils.full2Half(zl_3));
					root1.addElement("prof_title_name").setText(zl_4 == null ? "" : testStringUtils.full2Half(zl_4));
					root1.addElement("card_type_value").setText(zl_5 == null ? "" : testStringUtils.full2Half(zl_5));
					root1.addElement("card_type_name").setText(zl_6 == null ? "" : testStringUtils.full2Half(zl_6));
					root1.addElement("card_code").setText(zl_7 == null ? "" : testStringUtils.full2Half(zl_7));
					
				num++;
			}
			
		}
		
		if(paperList.size() != 0) {
			int num = 1;
			for(Element zl:paperList){
				Element root1 = root.addElement("paper").addElement("list");
				root1.addAttribute("seq_no", Integer.toString(num));
				String zl_1 = zl.elementText("paper_name").trim();
				String zl_2 = zl.elementText("paper").trim();
				String zl_3 = zl.elementText("paper_author").trim();
				String zl_4 = zl.elementText("publish_date").trim();
				String zl_5 = zl.elementText("yesorno_name").trim();
				root1.addElement("paper_name").setText(zl_1 == null ? "" : testStringUtils.full2Half(zl_1));
				root1.addElement("paper").setText(zl_2 == null ? "" : testStringUtils.full2Half(zl_2));
				root1.addElement("paper_author").setText(zl_3 == null ? "" : testStringUtils.full2Half(zl_3));
				root1.addElement("publish_date").setText(zl_4 == null ? "" : testStringUtils.full2Half(zl_4));
				root1.addElement("yesorno_name").setText(zl_5 == null ? "" : testStringUtils.full2Half(zl_5));
				num++;
			}
			
		}
		if(zlList.size() != 0) {
			int num = 1;
			for(Element zl:zlList){
				Element root1 = root.addElement("mainproperty").addElement("list");
				root1.addAttribute("seq_no", Integer.toString(num));
				String zl_1 = zl.elementText("property_name").trim();
				String zl_2 = zl.elementText("patent_number_part").trim();
				String zl_3 = zl.elementText("authorization_date").trim();
				String zl_4 = zl.elementText("obligee").trim();
				String zl_5 = zl.elementText("inventer").trim();
				root1.addElement("property_name").setText(zl_1 == null ? "" : testStringUtils.full2Half(zl_1));
				root1.addElement("patent_number_part").setText(zl_2 == null ? "" : testStringUtils.full2Half(zl_2));
				root1.addElement("authorization_date").setText(zl_3 == null ? "" : testStringUtils.full2Half(zl_3));
				root1.addElement("obligee").setText(zl_4 == null ? "" : testStringUtils.full2Half(zl_4));
				root1.addElement("inventer").setText(zl_5 == null ? "" : testStringUtils.full2Half(zl_5));
				num++;
			}
			
		}else if(zl_no != null){
			Element root1 = root.addElement("mainproperty").addElement("list");
			root1.addElement("property_name").setText(zh_title == null ? "" : testStringUtils.full2Half(zh_title.getText().trim()));
			root1.addElement("patent_number_part").setText(zl_no == null ? "" : testStringUtils.full2Half(zl_no.getText().trim()));
			root1.addElement("authorization_date").setText(zl_date == null ? "" : testStringUtils.full2Half(zl_date.getText().trim()));
			root1.addElement("obligee").setText(zl_qlr == null ? "" : testStringUtils.full2Half(zl_qlr.getText().trim()));
			root1.addElement("inventer").setText(zl_fmr == null ? "" : testStringUtils.full2Half(zl_fmr.getText().trim()));
		}
		
		content = root.asXML();
		// System.out.println(content.toString());
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:抽取奖励类项目比对内容==========");
		return content.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double compareContent(String sourceContent, String targetContent) {
		Double similarity = 0.0;

		if(!StringUtils.isNotBlank(sourceContent) || !StringUtils.isNotBlank(targetContent)) {
			return similarity;
		}
		
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:奖励类项目相似度比对==========");
		//1.解析xml
		Element sourceRoot = (Element) XMLHelperCacheable.parseDocument(sourceContent).selectSingleNode("data");
		Element targetRoot = (Element) XMLHelperCacheable.parseDocument(targetContent).selectSingleNode("data");
		Document sourceDoc = XMLHelper.parseDocument(sourceContent);
		Document targetDoc = XMLHelper.parseDocument(targetContent);
		String srcContent = sourceRoot.elementText("prp_content").trim();
		String tagContent = targetRoot.elementText("prp_content").trim();
		/*String srcContent1 = sourceRoot.elementText("content1").trim();
		String tagContent1 = targetRoot.elementText("content1").trim();
		String srcContent2 = sourceRoot.elementText("content2").trim();
		String tagContent2 = targetRoot.elementText("content2").trim();
		String srcContent3 = sourceRoot.elementText("content3").trim();
		String tagContent3 = targetRoot.elementText("content3").trim();
		String srcContent4 = sourceRoot.elementText("content4").trim();
		String tagContent4 = targetRoot.elementText("content4").trim();*/
		
		List<Element> sourceList = sourceDoc.selectNodes(CompareConstants.ZHUANLI_EXTRACT_XPATH);
		List<Element> targetList = targetDoc.selectNodes(CompareConstants.ZHUANLI_EXTRACT_XPATH);
		if(sourceList.size() != 0 && targetList.size() != 0) {
			//2.比较数据：专利号
			for (Element src : sourceList) {
				String srcPatentNumber = src.element("list").elementText("patent_number_part").trim();
				for (Element tge : targetList) {
					String tgePatentNumber = tge.element("list").elementText("patent_number_part").trim();
					
					if(srcPatentNumber.equalsIgnoreCase(tgePatentNumber)) {
						//相同条目添加标识，页面进行渲染
						similarity = 1.0;
						break;
					}
				}
			}
		}
		List<Element> sourcePaperList = sourceDoc.selectNodes(CompareConstants.PAPER_EXTRACT_XPATH_LIST);
		List<Element> targetPaperList = targetDoc.selectNodes(CompareConstants.PAPER_EXTRACT_XPATH_LIST);
		if(sourcePaperList.size() != 0 && targetPaperList.size() != 0) {
			//2.比较数据：论文专著名称、刊名和作者
			for (Element src : sourcePaperList) {
				String srcPaper = src.elementText("paper_name").trim();
				String srcName = src.elementText("paper").trim();
				String srcAuthor = src.elementText("paper_author").trim();
				for (Element tge : targetPaperList) {
					String tgePaper = tge.elementText("paper_name").trim();
					String tgeName = tge.elementText("paper").trim();
					String tgeAuthor = tge.elementText("paper_author").trim();
					if(srcPaper.equalsIgnoreCase(tgePaper) && srcName.equalsIgnoreCase(tgeName) && srcAuthor.equalsIgnoreCase(tgeAuthor)) {
						//相同条目添加标识，页面进行渲染
						similarity = 1.0;
						break;
					}
				}
			}
		}
		List<Element> sourcePersonList = sourceDoc.selectNodes(CompareConstants.PSN_EXTRACT_XPATH);
		List<Element> targetPersonList = targetDoc.selectNodes(CompareConstants.PSN_EXTRACT_XPATH);
		if(sourcePersonList.size() != 0 && targetPersonList.size() != 0) {
			//2.比较数据：论文专著名称、刊名和作者
			for (Element src : sourcePersonList) {
				String srcType = src.element("list").elementText("card_type_value").trim();
				String srcCode = src.element("list").elementText("card_code").trim();
				for (Element tge : targetPersonList) {
					String tgeType = tge.element("list").elementText("card_type_value").trim();
					String tgeTypeName = tge.element("list").elementText("card_type_name").trim();
					String tgeCode = tge.element("list").elementText("card_code").trim();
					//证件类型为其他以及证件号码为：(无、暂无则不参与检查 || "无".equalsIgnoreCase(tgeCode) || "暂无".equalsIgnoreCase(tgeCode) )
					if("其他".equals(tgeTypeName)) {
						continue;
					}
					if(srcType.equalsIgnoreCase(tgeType) && srcCode.equalsIgnoreCase(tgeCode)) {
						//相同条目添加标识，页面进行渲染
						similarity = 1.0;
						
						break;
					}
				}
			}
		}
			
			if((similarity < 0.5)&&(srcContent!=null&&srcContent!="")&&(tagContent!=null&&tagContent!="")){
				String sc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent.toUpperCase()));//全角转半角，统一大写
				String tc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent.toUpperCase()));
				
				similarity = SimilarityUtils.calculateSimilaryByClause(sc, tc, 4, CompareConstants.filterWord);
			}
			
		/*	if((similarity < 0.5)&&(srcContent1!=null&&srcContent1!="")&&(tagContent1!=null&&tagContent1!="")&&(srcContent2!=null&&srcContent2!="")&&(tagContent2!=null&&tagContent2!="")
					&&(srcContent3!=null&&srcContent3!="")&&(tagContent3!=null&&tagContent3!="")&&(srcContent4!=null&&srcContent4!="")&&(tagContent4!=null&&tagContent4!="")){
				String sc1 = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent1.toUpperCase()));//全角转半角，统一大写
				String tc1 = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent1.toUpperCase()));
				
				String sc2 = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent2.toUpperCase()));//全角转半角，统一大写
				String tc2 = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent2.toUpperCase()));
				
				String sc3 = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent3.toUpperCase()));//全角转半角，统一大写
				String tc3 = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent3.toUpperCase()));
				
				String sc4 = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent4.toUpperCase()));//全角转半角，统一大写
				String tc4 = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent4.toUpperCase()));
				
				similarity = SimilarityUtils.calculateSimilaryByClause(sc1+sc2+sc3+sc4, tc1+tc2+tc3+tc4, 4, CompareConstants.filterWord);
			}*/
		
		// System.out.println("奖励类项目相似度：" + similarity);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:奖励类项目相似度比对==========");
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
		Element sourceRoot = (Element) sourceDoc.selectSingleNode("data");
		Element targetRoot = (Element) targetDoc.selectSingleNode("data");
		String srcContent = sourceRoot.elementText("prp_content").trim();
		String tagContent = targetRoot.elementText("prp_content").trim();
		/*String srcContent1 = sourceRoot.elementText("content1").trim();
		String tagContent1 = targetRoot.elementText("content1").trim();
		String srcContent2 = sourceRoot.elementText("content2").trim();
		String tagContent2 = targetRoot.elementText("content2").trim();
		String srcContent3 = sourceRoot.elementText("content3").trim();
		String tagContent3 = targetRoot.elementText("content3").trim();
		String srcContent4 = sourceRoot.elementText("content4").trim();
		String tagContent4 = targetRoot.elementText("content4").trim();*/
		
		List<Element> sourceList = sourceDoc.selectNodes(CompareConstants.ZHUANLI_EXTRACT_XPATH);
		List<Element> targetList = targetDoc.selectNodes(CompareConstants.ZHUANLI_EXTRACT_XPATH);
		List<Element> delList = new ArrayList<Element>();
		if(sourceList.size() != 0 && targetList.size() != 0) {
			int sameNum = 0;//相同条目数，用于获取颜色
			//2.比较数据：专利号
			for (Element src : sourceList) {
				String srcPatentNumber = src.element("list").elementText("patent_number_part").trim();
				for (Element tge : targetList) {
					String tgePatentNumber = tge.element("list").elementText("patent_number_part").trim();
					
					if(srcPatentNumber.equalsIgnoreCase(tgePatentNumber)) {
						//相同条目添加标识，页面进行渲染
						sameNum++;
						src.element("list").element("patent_number_part").addAttribute("high_light", Integer.toString(sameNum));
						tge.element("list").element("patent_number_part").addAttribute("high_light", Integer.toString(sameNum));
						delList.add(tge);
						
						break;
					}
				}
				targetList.removeAll(delList);
			}
		}
		List<Element> sourcePaperList = sourceDoc.selectNodes(CompareConstants.PAPER_EXTRACT_XPATH_LIST);
		List<Element> targetPaperList = targetDoc.selectNodes(CompareConstants.PAPER_EXTRACT_XPATH_LIST);
		List<Element> delPaperList = new ArrayList<Element>();
		if(sourcePaperList.size() != 0 && targetPaperList.size() != 0) {
			int sameNum1 = 0;//相同条目数，用于获取颜色
			//2.比较数据：论文专著名称、刊名和作者
			for (Element src : sourcePaperList) {
				String srcPaper = src.elementText("paper_name").trim();
				String srcName = src.elementText("paper").trim();
				String srcAuthor = src.elementText("paper_author").trim();
				for (Element tge : targetPaperList) {
					String tgePaper = tge.elementText("paper_name").trim();
					String tgeName = tge.elementText("paper").trim();
					String tgeAuthor = tge.elementText("paper_author").trim();
					if(srcPaper.equalsIgnoreCase(tgePaper) && srcName.equalsIgnoreCase(tgeName) && srcAuthor.equalsIgnoreCase(tgeAuthor)) {
						//相同条目添加标识，页面进行渲染
						sameNum1++;
						src.addAttribute("high_light", Integer.toString(sameNum1));
						tge.addAttribute("high_light", Integer.toString(sameNum1));
						delPaperList.add(tge);
						
						break;
					}
				}
				targetPaperList.removeAll(delPaperList);
			}
		}
		List<Element> sourcePersonList = sourceDoc.selectNodes(CompareConstants.PSN_EXTRACT_XPATH);
		List<Element> targetPersonList = targetDoc.selectNodes(CompareConstants.PSN_EXTRACT_XPATH);
		List<Element> delPersonList = new ArrayList<Element>();
		if(sourcePersonList.size() != 0 && targetPersonList.size() != 0) {
			int sameNum2 = 0;//相同条目数，用于获取颜色
			//2.比较数据：论文专著名称、刊名和作者
			for (Element src : sourcePersonList) {
				String srcType = src.element("list").elementText("card_type_value").trim();
				String srcCode = src.element("list").elementText("card_code").trim();
				for (Element tge : targetPersonList) {
					String tgeType = tge.element("list").elementText("card_type_value").trim();
					String tgeTypeName = tge.element("list").elementText("card_type_name").trim();
					String tgeCode = tge.element("list").elementText("card_code").trim();
					//证件类型为其他--以及证件号码为：无、暂无则不参与检查 (|| "无".equalsIgnoreCase(tgeCode) || "暂无".equalsIgnoreCase(tgeCode) )
					if("其他".equals(tgeTypeName)) {
						delPersonList.add(tge);
						continue;
					}
					if(srcType.equalsIgnoreCase(tgeType) && srcCode.equalsIgnoreCase(tgeCode)) {
						//相同条目添加标识，页面进行渲染
						sameNum2++;
						src.element("list").addAttribute("high_light", Integer.toString(sameNum2));
						tge.element("list").addAttribute("high_light", Integer.toString(sameNum2));
						delPersonList.add(tge);
						
						break;
					}
				}
				targetPersonList.removeAll(delPersonList);
			}
		}
		
		
		if((srcContent!=null&&srcContent!="")&&(tagContent!=null&&tagContent!="")){
			String sc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent.toUpperCase()));//全角转半角，统一大写
			String tc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent.toUpperCase()));
			
			List<String> same1 = SimilarityUtils.getSameList(sc, tc, 4, CompareConstants.filterWord);
			String fontColor1 = "#FF0000";
			String backgroundColor = "#FFFFFF";
			srcContent = SimilarityUtils.replaceSameContent(sc, same1, fontColor1, backgroundColor, true);
			tagContent = SimilarityUtils.replaceSameContent(tc, same1, fontColor1, backgroundColor, false);
			sourceRoot.element("prp_content").setText(srcContent);
			targetRoot.element("prp_content").setText(tagContent);
		}
		
		
		
		
		/*if((srcContent1!=null&&srcContent1!="")&&(tagContent1!=null&&tagContent1!="")){
			
			String sc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent1.toUpperCase()));//全角转半角，统一大写
			String tc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent1.toUpperCase()));
			List<String> same1 = SimilarityUtils.getSameList(sc, tc, 4, CompareConstants.filterWord);
			String fontColor1 = "#FF0000";
			String backgroundColor = "#FFFFFF";
			srcContent1 = SimilarityUtils.replaceSameContent(sc, same1, fontColor1, backgroundColor, true);
			tagContent1 = SimilarityUtils.replaceSameContent(tc, same1, fontColor1, backgroundColor, false);
			sourceRoot.element("content1").setText(srcContent1);
			targetRoot.element("content1").setText(tagContent1);
		}
		if((srcContent2!=null&&srcContent2!="")&&(tagContent2!=null&&tagContent2!="")){
			String sc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent2.toUpperCase()));//全角转半角，统一大写
			String tc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent2.toUpperCase()));
			
			List<String> same1 = SimilarityUtils.getSameList(sc, tc, 4, CompareConstants.filterWord);
			String fontColor1 = "#FF0000";
			String backgroundColor = "#FFFFFF";
			srcContent2 = SimilarityUtils.replaceSameContent(sc, same1, fontColor1, backgroundColor, true);
			tagContent2 = SimilarityUtils.replaceSameContent(tc, same1, fontColor1, backgroundColor, false);
			sourceRoot.element("content2").setText(srcContent2);
			targetRoot.element("content2").setText(tagContent2);
		}
		if((srcContent3!=null&&srcContent3!="")&&(tagContent3!=null&&tagContent3!="")){
			String sc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent3.toUpperCase()));//全角转半角，统一大写
			String tc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent3.toUpperCase()));
			
			List<String> same1 = SimilarityUtils.getSameList(sc, tc, 4, CompareConstants.filterWord);
			String fontColor1 = "#FF0000";
			String backgroundColor = "#FFFFFF";
			srcContent3 = SimilarityUtils.replaceSameContent(sc, same1, fontColor1, backgroundColor, true);
			tagContent3 = SimilarityUtils.replaceSameContent(tc, same1, fontColor1, backgroundColor, false);
			sourceRoot.element("content3").setText(srcContent3);
			targetRoot.element("content3").setText(tagContent3);
		}
		if((srcContent4!=null&&srcContent4!="")&&(tagContent4!=null&&tagContent4!="")){
			String sc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(srcContent4.toUpperCase()));//全角转半角，统一大写
			String tc = testStringUtils.full2Half(org.springframework.util.StringUtils.trimAllWhitespace(tagContent4.toUpperCase()));
			
			List<String> same1 = SimilarityUtils.getSameList(sc, tc, 4, CompareConstants.filterWord);
			String fontColor1 = "#FF0000";
			String backgroundColor = "#FFFFFF";
			srcContent4 = SimilarityUtils.replaceSameContent(sc, same1, fontColor1, backgroundColor, true);
			tagContent4 = SimilarityUtils.replaceSameContent(tc, same1, fontColor1, backgroundColor, false);
			sourceRoot.element("content4").setText(srcContent4);
			targetRoot.element("content4").setText(tagContent4);
		}*/
		
		
		sourceContent = sourceDoc.asXML();
		targetContent = targetDoc.asXML();
		
		map.put("sourceContent", sourceContent);
		map.put("targetContent", targetContent);
		return map;
	}
	
}
