package com.iris.egrant.code.compare.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.egrant.code.compare.constant.CompareConstants;
import com.iris.egrant.code.compare.model.Proposal;
import com.iris.egrant.code.compare.model.ProposalExtend;
import com.iris.egrant.core.utils.SimilarityUtils;
import com.iris.egrant.core.utils.XMLHelperCacheable;


/**
 * 项目内容比对实现类
 * @author wk
 *
 */
@Service("goalCompareTemplateServiceImpl")
public class GoalCompareTemplateServiceImpl extends DataFilterSolrSupport implements  CompareTemplateService  {
	
	@Autowired
	private XMLHelperCacheable XMLHelperCacheable ;

	@Autowired
	private ProposalExtendService  proposalExtendService;
	@Autowired
	private CompareSourceService compareSourceService;
	@SuppressWarnings("unchecked")
	@Override
	public String extractCompareSource(long prpCode) {
	 	// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:抽取项目内容==========");
		
		ProposalExtend pe = proposalExtendService.getProposalExtendByPrpCode(prpCode);
		Proposal prp=proposalExtendService.getProposalByCode(prpCode);
		StringBuffer content = new StringBuffer("");
		//1.获取xml文档
		Document doc = XMLHelperCacheable.parseDoc(pe.getPrpXML());
		List<Map<String,Object>> listNodes=compareSourceService.getSpecialCompareNodeByGrantCode(prp.getSubGrantCode(), 1, 1);
		if(listNodes!=null&&listNodes.size()>0){
			for(int i=0;i<listNodes.size();i++){
				Element e = (Element) doc.selectSingleNode(MapUtils.getString(listNodes.get(i),"NODE"));
				content.append(e.getText() + "\n");
			}
		}else {
			//2.抽取概述大节点
			Element goal = (Element) doc.selectSingleNode(CompareConstants.GOAL_EXTRACT_XPATH);
			//3.遍历子节点拼接文本
			if(goal != null) {
				List<Element> summarys = goal.elements();
				if(summarys != null && summarys.size() > 0) {
					for (Element e : summarys) {
						if(e.getName().indexOf("hidden") == -1 && e.getName().indexOf("summary") >= 0)
							content.append(e.getText() + "\n");
					}
				}
			}
		}
		// System.out.println(content.toString());
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:抽取项目内容==========");
		return content.toString();
	}

	@Override
	public Double compareContent(String sourceContent, String targetContent) {
		Double similarity = 0.0;
		if(!StringUtils.isNotBlank(sourceContent) || !StringUtils.isNotBlank(targetContent)) {
			return similarity;
		}
		
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:项目内容相似度比对==========");
		similarity = SimilarityUtils.calculateSimilaryByClause(sourceContent, targetContent, 4, CompareConstants.filterWord);
		
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+ "项目内容相似度：" + similarity);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:项目内容相似度比对==========");
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
		
		List<String> same = SimilarityUtils.getSameList(sourceContent, targetContent, 4, CompareConstants.filterWord);
		String fontColor = "#FF0000";
		String backgroundColor = "#FFFFFF";
		sourceContent = SimilarityUtils.replaceSameContent(sourceContent, same, fontColor, backgroundColor, true);
		targetContent = SimilarityUtils.replaceSameContent(targetContent, same, fontColor, backgroundColor, false);
		
		map.put("sourceContent", sourceContent);
		map.put("targetContent", targetContent);
		return map;
	}
	
}
