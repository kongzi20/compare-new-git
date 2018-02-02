package com.iris.egrant.code.compare.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.egrant.code.compare.constant.CompareConstants;
import com.iris.egrant.code.compare.model.ProposalExtend;
import com.iris.egrant.core.utils.IrisStringUtils;
import com.iris.egrant.core.utils.XMLHelper;

/**
 * 设备清单比对实现类，返回结果为xml格式String?
 * data/increaseds/list，data/euipments/list（设备名称name，型号model）
 * @author wk
 *
 */
@Service("equipmentCompareTemplateServiceImpl")
public class EquipmentCompareTemplateServiceImpl extends DataFilterJavaSupport implements CompareTemplateService {
	
	@Autowired
	private  com.iris.egrant.core.utils.XMLHelperCacheable XMLHelperCacheable;
	
	@Autowired
	private ProposalExtendService proposalExtendService;
	
	@Override
	/**
	 * 抽取方法，返回值为xml格式String
	 */
	public String extractCompareSource(long prpCode) {
		String content = "";
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:抽取设备清单==========");
		
		ProposalExtend pe = proposalExtendService.getProposalExtendByPrpCode(prpCode);
		//1.获取xml文档
		Document doc = XMLHelperCacheable.parseDoc(pe.getPrpXML());
		
		//2.抽取清单大节点
		Element root = DocumentHelper.createDocument().addElement("data");//用于保存合并后的内容
		Element increaseds = (Element) doc.selectSingleNode(CompareConstants.EQUIPMENT_EXTRACT_XPATH1);
		Element euipments = (Element) doc.selectSingleNode(CompareConstants.EQUIPMENT_EXTRACT_XPATH2);
		Element intelligent = (Element) doc.selectSingleNode(CompareConstants.EQUIPMENT_EXTRACT_XPATH4);
		if(increaseds != null) {
			root.addElement("increaseds").appendContent(increaseds);
		}
		if(euipments != null) {
			root.addElement("euipments").appendContent(euipments);
		}
		if(intelligent != null) {
			root.addElement("intelligent").appendContent(intelligent);
		}
		Element org_no = (Element) doc.selectSingleNode(CompareConstants.HEZHUN_EXTRACT_XPATH_ORGNO);//组织机构代码
		root.addElement("org_no").setText(org_no == null ? "" : IrisStringUtils.full2Half(org_no.getText().trim()));//全角转半角
		content = root.asXML();
		
		// System.out.println(content);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:抽取设备清单==========");
		return content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double compareContent(String sourceContent, String targetContent) {
		Double similarity = 0.0;
		//double sameNum = 0.0;
		if(!StringUtils.isNotBlank(sourceContent) || !StringUtils.isNotBlank(targetContent)) {
			return similarity;
		}
		
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:设备清单相似度比对==========");
		//1.解析xml:<data><increaseds><list>...<list><list>...<list></increaseds><euipments><list>...<list><list>...<list></euipments></data>
		Node sourceRoot =  XMLHelperCacheable.parseDocument(sourceContent).selectSingleNode("data");
		Node targetRoot =  XMLHelperCacheable.parseDocument(targetContent).selectSingleNode("data");
		
		String srcOrgNo = StringUtils.trim(XMLHelperCacheable.getNodeValue(sourceContent, sourceRoot, "org_no") );
		String tagOrgNo = StringUtils.trim(XMLHelperCacheable.getNodeValue(targetContent , targetRoot,"org_no" ));
		
		Node sourceIncreaseds = sourceRoot.selectSingleNode("increaseds");
		Node sourceEuipments = sourceRoot.selectSingleNode("euipments");
		Node sourceIntelligent = sourceRoot.selectSingleNode("intelligent");
		if(sourceIncreaseds == null && sourceEuipments != null) {
			//节点不同类别
			sourceIncreaseds = sourceEuipments;
			if(sourceIntelligent != null){
				sourceIncreaseds.setText( sourceIncreaseds.getText() +  sourceIntelligent.getText());
			}
			
		} else if(sourceEuipments != null){
			sourceIncreaseds.setText( sourceIncreaseds.getText() + sourceEuipments.getText());
			if(sourceIntelligent != null){
				sourceIncreaseds.setText( sourceIncreaseds.getText() + sourceIntelligent.getText());
			}
		}
		
		Node targetIncreaseds = targetRoot.selectSingleNode("increaseds");
		Node targetEuipments = targetRoot.selectSingleNode("euipments");
		Node targetIntelligent = targetRoot.selectSingleNode("intelligent");
		if(targetIncreaseds == null && targetEuipments != null) {
			//节点不同类别
			targetIncreaseds = targetEuipments;
			if(targetIntelligent != null){
				String tempStr = targetIncreaseds.getText() +  targetIntelligent.getText() ;
				targetIncreaseds.setText(tempStr);
			}
		} else if(targetEuipments != null){
			String tempStr = targetIncreaseds.getText() + targetEuipments.getText() ;
			targetIncreaseds.setText(tempStr);
			if(targetIntelligent != null){
				String tempStr2 =  targetIncreaseds.getText() + targetIntelligent.getText() ;
				targetIncreaseds.setText(tempStr2);
			}
		}
		if(sourceIncreaseds == null || targetIncreaseds == null) {
			return similarity;
		}
		
		List<Node> sourceList = sourceIncreaseds.selectNodes("list");
		List<Node> targetList = targetIncreaseds.selectNodes("list");
		List<Node> delList = new ArrayList<Node>();
		double total = 0;
		double samMoney = 0;
		
		boolean isSameOrg = false;
		if(srcOrgNo != null && tagOrgNo != null && srcOrgNo.equalsIgnoreCase(tagOrgNo)){ 
			isSameOrg = true;
		}
		
		for (Node tge : targetList) {
			String tgeMoney = XMLHelperCacheable.getNodeValue(targetContent, tge, CompareConstants.EQUIPMENT_COMPARE_MONEY);
			if(!NumberUtils.isNumber(tgeMoney)) continue;
			total += Double.valueOf(tgeMoney);
		}
		String regEx="[、，]";//替换指定的分隔符
    	Pattern p = Pattern.compile(regEx); 
		for (Node src : sourceList) {
			String srcName = IrisStringUtils.full2Half(IrisStringUtils.filterNull(XMLHelperCacheable.getNodeValue(sourceContent, src, CompareConstants.EQUIPMENT_COMPARE_NAME) )).trim();
			String srcModel = IrisStringUtils.full2Half(IrisStringUtils.filterNull(XMLHelperCacheable.getNodeValue(sourceContent, src, CompareConstants.EQUIPMENT_COMPARE_MODEL))).trim();
			String srcFapiao = XMLHelperCacheable.getNodeValue(sourceContent, src, CompareConstants.EQUIPMENT_COMPARE_FAPIAO);
			srcFapiao = srcFapiao == null ? IrisStringUtils.full2Half(IrisStringUtils.filterNull(XMLHelperCacheable.getNodeValue(sourceContent, src, CompareConstants.EQUIPMENT_COMPARE_FAPIAO2))).trim() : IrisStringUtils.full2Half(IrisStringUtils.filterNull(srcFapiao)).trim();
			/*srcFapiao.replaceAll("，", ",");
			srcFapiao.replaceAll("、", ",");*/
			Matcher m = p.matcher(srcFapiao); 
			srcFapiao=m.replaceAll(",");
			String srcDate = IrisStringUtils.filterNull( XMLHelperCacheable.getNodeValue(sourceContent, src, CompareConstants.EQUIPMENT_COMPARE_DATE));
			
			String srcMoney = XMLHelperCacheable.getNodeValue(sourceContent, src, CompareConstants.EQUIPMENT_COMPARE_MONEY);
			if(!NumberUtils.isNumber(srcMoney)) continue;
			total += Double.valueOf(srcMoney);
			
			for (Node tge : targetList) {
				String tgeName = IrisStringUtils.full2Half(IrisStringUtils.filterNull(XMLHelperCacheable.getNodeValue(targetContent, tge, CompareConstants.EQUIPMENT_COMPARE_NAME))).trim();
				String tgeModel = IrisStringUtils.full2Half(IrisStringUtils.filterNull( XMLHelperCacheable.getNodeValue(targetContent, tge,CompareConstants.EQUIPMENT_COMPARE_MODEL))).trim();
				String tgeFapiao =   XMLHelperCacheable.getNodeValue(targetContent, tge, CompareConstants.EQUIPMENT_COMPARE_FAPIAO)  ;
				tgeFapiao = tgeFapiao == null ? IrisStringUtils.full2Half(IrisStringUtils.filterNull(  XMLHelperCacheable.getNodeValue(targetContent, tge, CompareConstants.EQUIPMENT_COMPARE_FAPIAO2))).trim() : IrisStringUtils.full2Half(IrisStringUtils.filterNull(tgeFapiao)).trim();
				/*tgeFapiao.replaceAll("，", ",");
				tgeFapiao.replaceAll("、", ",");*/
				Matcher m2 = p.matcher(tgeFapiao); 
				tgeFapiao=m2.replaceAll(",");
				String tgeDate =   XMLHelperCacheable.getNodeValue(targetContent, tge, CompareConstants.EQUIPMENT_COMPARE_DATE) ;
				String tgeMoney = IrisStringUtils.filterNull(  XMLHelperCacheable.getNodeValue(targetContent, tge, CompareConstants.EQUIPMENT_COMPARE_MONEY));
				if(!NumberUtils.isNumber(tgeMoney)) continue;
				
				if(isSameOrg){//同一单位
					/**
					 * 比较数据：规则 
					 * (1) 名称+型号+发票 （去掉高频率词：无）
					 * (2) 发票+名称 
					 * (3) 名称+型号（去掉高频率词，如待定、定制、无、暂无、购置、自制）  
					 * (4) 名称+购置日期
					 * (5) 编号
					 * (6) 发票+设备型号 
					*/
					if ((srcName.equalsIgnoreCase(tgeName) && !new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) && new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(",")))))//2
							|| (!CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel) && srcName.equalsIgnoreCase(tgeName))//3
							|| (srcName.equalsIgnoreCase(tgeName)&&srcDate.equalsIgnoreCase(tgeDate))//4
							|| (!CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel))//5
							|| (!CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel) && !new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) && new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(",")))))//6
							) {
						//sameNum++;
						delList.add(tge);
						samMoney += Double.valueOf(srcMoney);
						samMoney += Double.valueOf(tgeMoney);
						break;
					}
				}else{//不同一单位
					//(如果有发票一致的设备，疑似度为100%
					if(!new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) && new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(","))))){
						// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"======设备清单相似度：" + similarity+"======不同一单位，如果有发票一致的设备，疑似度为100%");
						return 1.0;
					}
					/**
					 * 1 名称+型号+发票 （去掉高频率词：无）
						2 发票+名称
						3 发票
						4 名称+购置日期+型号（去掉高频率词，如待定、定制、无、暂无、购置、自制）  
					 */
					if ((srcName.equalsIgnoreCase(tgeName) && !new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) && new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(",")))))//2
							|| (!new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) && new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(",")))))//3
							|| (srcName.equalsIgnoreCase(tgeName)&&srcDate.equalsIgnoreCase(tgeDate)&&!CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel))) {//5
						//sameNum++;
						delList.add(tge);
						samMoney += Double.valueOf(srcMoney);
						samMoney += Double.valueOf(tgeMoney);
						break;
					}
				}
			}
			targetList.removeAll(delList);
		}
		  
		// 3.计算相似度:相同条数*2/表单条数之和
		//similarity = sameNum * 2 / (sourceList.size() + targetList.size() + delList.size()) ;
		similarity = samMoney / total ;
		similarity = (double) Math.round(similarity * 10000) / 10000; // 保留小数点后4位
		
		// System.out.println("设备清单相似度：" + similarity);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:设备清单相似度比对==========");
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
		int sameNum = 0;//相同条目数，用于获取颜色
		
		String srcOrgNo = StringUtils.trim(sourceDoc.selectSingleNode(CompareConstants.EQUIPMENT_EXTRACT_XPATH3).getText());
		String tagOrgNo = StringUtils.trim(targetDoc.selectSingleNode(CompareConstants.EQUIPMENT_EXTRACT_XPATH3).getText());
		
		boolean isSameOrg = false;
		if(srcOrgNo != null && tagOrgNo != null && srcOrgNo.equalsIgnoreCase(tagOrgNo)){ 
			isSameOrg = true;
		}
		
		List<Element> sourcetList = new ArrayList<Element>();
		sourcetList.addAll(sourceDoc.selectNodes("//data/increaseds/list"));
		sourcetList.addAll(sourceDoc.selectNodes("//data/euipments/list"));
		sourcetList.addAll(sourceDoc.selectNodes("//data/intelligent/list"));
		List<Element> targetList = new ArrayList<Element>();
		targetList.addAll(targetDoc.selectNodes("//data/increaseds/list"));
		targetList.addAll(targetDoc.selectNodes("//data/euipments/list"));
		targetList.addAll(targetDoc.selectNodes("//data/intelligent/list"));
		String regEx="[、，]";//替换指定的分隔符
    	Pattern p = Pattern.compile(regEx); 
		String flag = ""; //ABCDE分别对应5种比对规则
		for (Element src : sourcetList) {
			flag = "";
			List<Element> delList = new ArrayList<Element>();
			String srcName = IrisStringUtils.full2Half(IrisStringUtils.filterNull(src.elementText(CompareConstants.EQUIPMENT_COMPARE_NAME))).trim();
			String srcModel = IrisStringUtils.full2Half(IrisStringUtils.filterNull(src.elementText(CompareConstants.EQUIPMENT_COMPARE_MODEL))).trim();
			String srcFapiao = src.elementText(CompareConstants.EQUIPMENT_COMPARE_FAPIAO);
			srcFapiao = srcFapiao == null ? IrisStringUtils.full2Half(IrisStringUtils.filterNull(src.elementText(CompareConstants.EQUIPMENT_COMPARE_FAPIAO2))).trim() : IrisStringUtils.full2Half(IrisStringUtils.filterNull(srcFapiao)).trim();
			Matcher m = p.matcher(srcFapiao); 
			srcFapiao=m.replaceAll(",");
			/*srcFapiao.replaceAll("，", ",");
			srcFapiao.replaceAll("、", ",");*/
			String srcDate = IrisStringUtils.filterNull(src.elementText(CompareConstants.EQUIPMENT_COMPARE_DATE));
			
			//2.2疑似项目设备表1比较
			for (Element tge : targetList) {
				flag = "";
				String tgeName = IrisStringUtils.full2Half(IrisStringUtils.filterNull(tge.elementText(CompareConstants.EQUIPMENT_COMPARE_NAME))).trim();
				String tgeModel = IrisStringUtils.full2Half(IrisStringUtils.filterNull(tge.elementText(CompareConstants.EQUIPMENT_COMPARE_MODEL))).trim();
				String tgeFapiao = tge.elementText(CompareConstants.EQUIPMENT_COMPARE_FAPIAO);
				tgeFapiao = tgeFapiao == null ? IrisStringUtils.full2Half(IrisStringUtils.filterNull(tge.elementText(CompareConstants.EQUIPMENT_COMPARE_FAPIAO2))).trim() : IrisStringUtils.full2Half(IrisStringUtils.filterNull(tgeFapiao)).trim();
				Matcher m2 = p.matcher(tgeFapiao); 
				tgeFapiao=m2.replaceAll(",");
				/*tgeFapiao.replaceAll("，", ",");
				tgeFapiao.replaceAll("、", ",");*/
				String tgeDate = IrisStringUtils.filterNull(tge.elementText(CompareConstants.EQUIPMENT_COMPARE_DATE));
				
				if(isSameOrg){
					if(srcName.equalsIgnoreCase(tgeName) && !CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel)
							&& !new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) 
							&& new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(","))))) {
						flag = "A";
					} else if(!new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) 
							&& srcName.equalsIgnoreCase(tgeName) 
							&& new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(","))))) {
						flag = "B";
					}else if(!CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel) 
							&& !new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) 
							&& new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(","))))) {
						flag = "H";
					} else if(!CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel) && srcName.equalsIgnoreCase(tgeName)) {
						flag = "C";
					} else if(srcName.equalsIgnoreCase(tgeName)&&srcDate.equalsIgnoreCase(tgeDate)) {
						flag = "D";
					}else if(!CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel)) {
						flag = "E";
					}
				}else{
					if(srcName.equalsIgnoreCase(tgeName) && !CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel)
							&& !new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) 
							&& new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(","))))) {
						flag = "A";
					} else if(!new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) 
							&& srcName.equalsIgnoreCase(tgeName) 
							&& new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(","))))) {
						flag = "B";
					} else if(!new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(CompareConstants.filterWord) 
							&& new HashSet<String>(Arrays.asList(srcFapiao.split(","))).removeAll(new HashSet<String>(Arrays.asList(tgeFapiao.split(","))))) {
						flag = "F";
					} else if(srcName.equalsIgnoreCase(tgeName)&&srcDate.equalsIgnoreCase(tgeDate)&&!CompareConstants.filterWord.contains(srcModel) && srcModel.equalsIgnoreCase(tgeModel)) {
						flag = "G";
					}
				}
				
				if (StringUtils.isNotBlank(flag)) {
					// 相同条目添加标识，页面进行渲染
					src.addAttribute("high_light", flag+Integer.toString(sameNum));
					tge.addAttribute("high_light", flag+Integer.toString(sameNum));

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
