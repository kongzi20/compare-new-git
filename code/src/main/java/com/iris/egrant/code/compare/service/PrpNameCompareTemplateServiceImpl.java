package com.iris.egrant.code.compare.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.iris.egrant.code.compare.constant.CompareConstants;
import com.iris.egrant.code.compare.model.ProposalExtend;
import com.iris.egrant.core.utils.IrisStringUtils;
import com.iris.egrant.core.utils.SimilarityUtils;

 

/**
 * 项目名称比对实现类
 * @author wk
 *
 */
@Service("prpNameCompareTemplateServiceImpl")
public class PrpNameCompareTemplateServiceImpl  extends DataFilterSolrSupport implements  CompareTemplateService   {
	
	@Autowired
	private com.iris.egrant.core.utils.XMLHelperCacheable XMLHelperCacheable;

	@Autowired
	private ProposalExtendService proposalExtendService;
	
	@Override
	public String extractCompareSource(long prpCode) {
		String content = "";
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:抽取项目名称==========");
		
		ProposalExtend pe = proposalExtendService.getProposalExtendByPrpCode(prpCode);
		Document doc = XMLHelperCacheable.parseDoc(pe.getPrpXML());
		Element zh_title = (Element) doc.selectSingleNode(CompareConstants.PRPNAME_EXTRACT_XPATH);
		if(zh_title != null) {
			content = zh_title.getText().trim();
		}
		
		// System.out.println(content);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:抽取项目名称==========");
		return content;
	}

	@Override
	public Double compareContent(String sourceContent, String targetContent) {
		Double similarity = 0.0;
		String st = IrisStringUtils.full2Half(StringUtils.trimAllWhitespace(sourceContent.toUpperCase()));//全角转半角，统一大写
		String tt = IrisStringUtils.full2Half(StringUtils.trimAllWhitespace(targetContent.toUpperCase()));
		
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========start:项目名称相似度比对==========");
		similarity = SimilarityUtils.calculateSimilaryByClause(st, tt, 0, null, true);
		//项目名称，如果完全一致（忽略空格，忽略大小写，忽略全角半角），则项目的相似度100%，否则不做比较（不用进行相似度的分析）。
		//similarity = st.equalsIgnoreCase(tt)? 1.0 : 0.0;
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"项目名称相似度：" + similarity);
		// System.out.println(DateUtils.now(DateFormator.YEAR_MONTH_DAY_HH_MM_SS)+"=========end:项目名称相似度比对==========");
		return similarity;
	}

	@Override
	public Map<String, String> renderDiffer(String sourceContent, String targetContent) {
		Map<String, String> map = new HashMap<String, String>();

		String st = IrisStringUtils.full2Half(StringUtils.trimAllWhitespace(sourceContent.toUpperCase()));//全角转半角，统一大写
		String tt = IrisStringUtils.full2Half(StringUtils.trimAllWhitespace(targetContent.toUpperCase()));
		
		List<String> same = SimilarityUtils.getSameList(st, tt, 0, null, true);
		String fontColor = "#FF0000";
		sourceContent = SimilarityUtils.replaceSameContent(sourceContent, same, fontColor, null, false, true);
		targetContent = SimilarityUtils.replaceSameContent(targetContent, same, fontColor, null, false, true);
		
		map.put("sourceContent", sourceContent);
		map.put("targetContent", targetContent);
		return map;
	}
	
}
