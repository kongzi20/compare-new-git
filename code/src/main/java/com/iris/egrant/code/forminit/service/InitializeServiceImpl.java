package com.iris.egrant.code.forminit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.egrant.code.forminit.model.FormBaseLibrary;
import com.iris.egrant.code.forminit.model.FormInitItem;
import com.iris.egrant.code.grantsetting.model.GrantSetting;
import com.iris.egrant.code.proposal.service.BaseXmlInitServiceImpl;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.exception.ServiceException;
import com.iris.egrant.core.utils.IrisStringUtils;
import com.iris.egrant.core.utils.XMLHelper;

/**
 * 通用XML初始化实现接口.
 * 
 * 
 * @version $Rev$ $Date$
 */
@Service("InitializeService")
public class InitializeServiceImpl extends BaseXmlInitServiceImpl implements
		InitializeService {

	private static Logger logger = LoggerFactory
			.getLogger(InitializeServiceImpl.class);

	@Autowired
	private InitalizeReadService initializeService;// 初始化

	/**
	 * 获得初始化信息.
	 */
	@Override
	public FormBaseLibrary getFormBaseLibraryByGrantId(Long grantCode,
			Long formCode, String xmlData, Map<String, Object> codes)
			throws ServiceException {
		// 获得主页面路径
		FormBaseLibrary formBaseLibrary = null;
		try {
			if (formCode == null) {
				GrantSetting gs = null;
				String subGrantNo = (String) codes.get("sub_grant_no");
				Object subGrantCode = codes.get("sub_grant_code");
				if (StringUtils.isNotBlank(subGrantNo)) {
					gs = initializeService.getGrantInfoByGrantNo(subGrantNo);
				} else if (subGrantCode != null) {
					gs = initializeService.getGrantInfoByGrantCode(Long
							.valueOf(subGrantCode.toString()));
				} else {
					gs = initializeService.getGrantInfoByGrantCode(grantCode);
				}

				String formType = codes.get("formType").toString();
				Long formId = 0L;
				if (formType.equalsIgnoreCase("proposal")) {// poposal，申请书模板的编号
					formId = gs.getPrpFormId();
				} else if (formType.equalsIgnoreCase("contract")) {// contract，合同书模板的编号
					formId = gs.getCtrFormId();
				} else if (formType.equalsIgnoreCase("acceptance")) {// acceptance，验收模板的编号
					formId = gs.getAptFormId();
				} else if (formType.equalsIgnoreCase("project")) {// project，项目模板的编号
					formId = gs.getPrjFormId();
				} else if (formType.equalsIgnoreCase("report")) {// report，报表模板的编号
					formId = gs.getRptFormId();
				}
				// 获得通用初始化信息
				formBaseLibrary = initializeService
						.getFormBaseLibraryByFormId(formId);
			} else {
				formBaseLibrary = initializeService
						.getFormBaseLibraryByFormCode(formCode);
			}
			List<FormInitItem> initbList = initializeService
					.getFormInitItemListById(formBaseLibrary.getInitItems());
			// 初始化XML字符串
			xmlData = refreshData(initbList, xmlData,
					(Integer) codes.get("actionType"), codes);
			formBaseLibrary.setXmlData(xmlData);
			// System.out.println(xmlData);
		} catch (DaoException e) {
			logger.error("没有找到业务类别ID为-->" + grantCode + "的配置", e);
			throw new ServiceException("没有找到类型ID为-->" + grantCode + "的配置");
		}

		return formBaseLibrary;
	}

	/**
	 * 根据传过来的值刷新XML 参数（刷新的页面集合，需刷新的xml字符串,申请书加载动作，需替换的SQL参数）.
	 * 
	 * @throws ServiceException
	 */

	public String refreshData(List<FormInitItem> formInitItems, String xmlData,
			Integer refrashType, Map<String, Object> codes)
			throws ServiceException {
		// 第一次初始化
		if (xmlData == null) {
			xmlData = "<data></data>";
		}
		try {
			Document doc = DocumentHelper.parseText(xmlData);
			for (FormInitItem formInitItem : formInitItems) {
				// 不需要刷新则返回
				if (formInitItem.getInitFlag() < refrashType) {
					continue;
				}
				// 取得需要刷新的XML节点
				String type = formInitItem.getType();
				String content = formInitItem.getContent();
				// 替换SQL中参数
				List<Object> params = new ArrayList<Object>();
				if (StringUtils.isNotBlank(content)) {
					content = IrisStringUtils.transSql(content, codes, params);
				}
				if (IrisStringUtils.isExistParam(content)) {
					continue;
				}

				// SQL类型
				if ("SQL".equalsIgnoreCase(type)) {

					// 获得结果集
					List<Map<String, Object>> list = initializeService
							.getRefreshList(content, params);
					if (list.size() == 0) {
						continue;
					}
					String node = formInitItem.getNode();
					// 不需要刷新XPath下的节点
					if (node == null || "".equalsIgnoreCase(node)) {
						if (list.size() == 1) {
							XMLHelper.fillXmlDataFromMap(doc,
									formInitItem.getXpath(), list.get(0));
						} else {
							for (int i = 0; i < list.size(); i++) {
								Map<String, Object> map = list.get(i);
								String value = "";
								if (map.get("VALUE") != null) {
									value = map.get("VALUE").toString();
								}
								String xpath = map.get("XPATH").toString();
								String nodeName = map.get("NODE").toString();
								Map<String, String> tmp = new HashMap<String, String>();
								tmp.put(nodeName, value);
								XMLHelper.fillXmlDataFromMap(doc, xpath, tmp);
							}
						}
					} else {// 刷新xpath节点下的多个记录集
						String nodeXML = XMLHelper.parseTableToXML(list, node
								+ "s", node);
						XMLHelper.refreshXmlDataFromNode(doc,
								formInitItem.getXpath(), nodeXML);
					}
				} else {// 如果后台配置的类型是xpath
					codes.put("xmlData", xmlData);
					// 取出需刷新的XML
					String[] nodes = formInitItem.getNode().split(",");// 根据,号刷新不同的xml节点
					String[] xpaths = formInitItem.getXpath().split(",");// 根据,号分隔不同需要刷新的xml节点,和nodes一一对应
					Document sDoc = super.getDocument(type, codes,
							formInitItem.getContent());
					if (sDoc == null) {
						continue;
					}
					for (int i = 0; i < nodes.length; i++) {
						Node sNode = sDoc.selectSingleNode(nodes[i]);
						XMLHelper.refreshXmlDataFromNode(doc, xpaths[i], sNode,
								false, true);
					}
				}

			}
			xmlData = doc.asXML();
		} catch (DocumentException e) {
			logger.error("初始化时XML转换出错", e);
			throw new ServiceException("初始化时XML转换出错");
		}
		logger.debug(xmlData);
		return xmlData;
	}

}
