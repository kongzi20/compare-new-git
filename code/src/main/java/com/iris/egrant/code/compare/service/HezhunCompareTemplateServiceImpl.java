package com.iris.egrant.code.compare.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.egrant.code.compare.constant.CompareConstants;
import com.iris.egrant.code.compare.model.ProposalExtend;
import com.iris.egrant.core.utils.DateFormator;
import com.iris.egrant.core.utils.DateUtils;
import com.iris.egrant.core.utils.IrisStringUtils;
import com.iris.egrant.core.utils.XMLHelper;


/**
 * 核准制项目比对实现类
 * @author wk
 *
 */
@Service("hezhunCompareTemplateServiceImpl")
public class HezhunCompareTemplateServiceImpl  extends DataFilterJavaSupport implements CompareTemplateService {
	
	@Autowired
	private com.iris.egrant.core.utils.XMLHelperCacheable XMLHelperCacheable ;

	@Autowired
	private ProposalExtendService proposalExtendService;
	
	private String FITER_WORD_REG = "\\{|\\}|\\(|\\)|<|>|【|】|（|）|《|》|\\[|\\]";
	
	@Override
	public String extractCompareSource(long prpCode) {
		String content = "";
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:抽取核准制项目比对内容==========");
		
		ProposalExtend pe = proposalExtendService.getProposalExtendByPrpCode(prpCode);
		//1.获取xml文档
		Document doc = XMLHelperCacheable.parseDoc(pe.getPrpXML());
		//2.抽取节点
		Element org_no = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_ORGNO);//组织机构代码
		Element type = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_TYPE2);//认定内容
		if(type == null) {
			type = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_TYPE);
		}
		Element date = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_DATE);//认定时间
		Element department = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_DEPARTMENT2);//认定部门
		if(department == null) {
			department = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_DEPARTMENT);
		}
		Element no = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_NO);//认定文号
		Element number = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_NUMBER);//认定编号
		//3.组装xml
		Document newDoc = DocumentHelper.createDocument();
		Element root = newDoc.addElement("data");
		root.addElement("org_no").setText(org_no == null ? "" : IrisStringUtils.full2Half(org_no.getText().trim()));//全角转半角
		root.addElement("type").setText(type == null ? "" : IrisStringUtils.full2Half(type.getText().trim()));
		root.addElement("date").setText(date == null ? "" : IrisStringUtils.full2Half(date.getText().trim()));
		root.addElement("department").setText(department == null ? "" : IrisStringUtils.full2Half(department.getText().trim()));
		root.addElement("no").setText(no == null ? "" : IrisStringUtils.full2Half(no.getText().trim()));
		root.addElement("number").setText(number == null ? "" : IrisStringUtils.full2Half(number.getText().trim()));
		
		content = root.asXML();
		// System.out.println(content.toString());
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:抽取核准制项目比对内容==========");
		return content.toString();
	}
	
	
	private String getStrFromContent(String content,String bz){	 
		/*if(content !=null && !content.trim().equals("") && content.indexOf("<"+bz+">")>-1 && content.indexOf("</"+bz+">")>-1){
			*/
		   return content.substring(content.indexOf("<"+bz+">")+bz.length()+2,content.indexOf("</"+bz+">"));
		/*}else{
			return "";
		}*/
	}
	
	/*public static void main(String[] args){
		System.out.print("value=="+getStrFromContent("<data></data>","data").trim());
	}
*/
	@Override
	public Double compareContent(String sourceContent, String targetContent) {
		Double similarity = 0.0;

		if(!StringUtils.isNotBlank(sourceContent) || !StringUtils.isNotBlank(targetContent)) {
			return similarity;
		}
		
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:核准制项目相似度比对==========");
		//1.解析xml
		/*Element sourceRoot = (Element) XMLHelper.parseDocument(sourceContent).selectSingleNode("data");
		Element targetRoot = (Element) XMLHelper.parseDocument(targetContent).selectSingleNode("data");
		String sourceNumber = org.springframework.util.StringUtils.trimAllWhitespace(sourceRoot.elementText("number")).replaceAll(FITER_WORD_REG, "");
		String targetNumber =  org.springframework.util.StringUtils.trimAllWhitespace(targetRoot.elementText("number")).replaceAll(FITER_WORD_REG, "");
		String sourceNo = org.springframework.util.StringUtils.trimAllWhitespace(sourceRoot.elementText("no")).replaceAll(FITER_WORD_REG, "");
		String targetNo = org.springframework.util.StringUtils.trimAllWhitespace(targetRoot.elementText("no")).replaceAll(FITER_WORD_REG, "");*/
		
		String sourceNumber = getStrFromContent(sourceContent,"number").replaceAll(FITER_WORD_REG, "");
		String targetNumber =  getStrFromContent(targetContent,"number").replaceAll(FITER_WORD_REG, "");
		String sourceNo = getStrFromContent(sourceContent,"no").replaceAll(FITER_WORD_REG, "");
		String targetNo = getStrFromContent(targetContent,"no").replaceAll(FITER_WORD_REG, "");
		
		//文号和编号都是文字不参与比对
		if(IrisStringUtils.isChar(sourceNo) || IrisStringUtils.isChar(targetNo) || IrisStringUtils.isChar(sourceNumber) || IrisStringUtils.isChar(targetNumber)){
			return similarity;
		}
		
		//比较规则:同单位文号（包括空）和编号项目则相同，不同单位只要编号相同则比对结果为100%
		//if (sourceRoot.elementText("org_no").equalsIgnoreCase(targetRoot.elementText("org_no").trim())){//组织机构代码
		if (getStrFromContent(sourceContent,"org_no").equalsIgnoreCase(getStrFromContent(targetContent,"org_no").trim())){//组织机构代码
			if(sourceNo.equalsIgnoreCase(targetNo)&&!StringUtils.isBlank(sourceNumber)&&sourceNumber.equalsIgnoreCase(targetNumber)){
				similarity = 1.0 ;
			}
		}else{
			if(!StringUtils.isBlank(sourceNumber)&&sourceNumber.equalsIgnoreCase(targetNumber)){
				similarity = 1.0 ;
			}
		}
		// System.out.println("核准制项目相似度：" + similarity);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:核准制项目相似度比对==========");
		return similarity;
	}

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
		String srcOrgNo = sourceRoot.elementText("org_no").trim();
		String tagOrgNo = targetRoot.elementText("org_no").trim();
		String sourceNumber = org.springframework.util.StringUtils.trimAllWhitespace(sourceRoot.elementText("number")).replaceAll(FITER_WORD_REG, "");
		String targetNumber =  org.springframework.util.StringUtils.trimAllWhitespace(targetRoot.elementText("number")).replaceAll(FITER_WORD_REG, "");
		String sourceNo = org.springframework.util.StringUtils.trimAllWhitespace(sourceRoot.elementText("no")).replaceAll(FITER_WORD_REG, "");
		String targetNo = org.springframework.util.StringUtils.trimAllWhitespace(targetRoot.elementText("no")).replaceAll(FITER_WORD_REG, "");

		//2.比较数据
		if (srcOrgNo.equalsIgnoreCase(tagOrgNo)) {
			sourceRoot.element("org_no").addAttribute("high_light", "1");
			targetRoot.element("org_no").addAttribute("high_light", "1");
		}
		if (sourceNo.equalsIgnoreCase(targetNo)) {//文号
			sourceRoot.element("no").addAttribute("high_light", "1");
			targetRoot.element("no").addAttribute("high_light", "1");
		}
		if (sourceNumber.equalsIgnoreCase(targetNumber)) {//编号
			sourceRoot.element("number").addAttribute("high_light", "1");
			targetRoot.element("number").addAttribute("high_light", "1");
		}
		
		sourceContent = sourceDoc.asXML();
		targetContent = targetDoc.asXML();
		
		map.put("sourceContent", sourceContent);
		map.put("targetContent", targetContent);
		return map;
	}
	
}
