package com.test.example.code.proposal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.example.code.forminit.service.BaseXmlNoTransInitService;
import com.test.example.core.utils.IrisStringUtils;
import com.test.example.core.utils.ServiceConstants;
import com.test.example.core.utils.XMLHelper;

@Service("baseXmlInitService")
public class BaseXmlInitServiceImpl implements BaseXmlInitService {
	private Document prpDoc;// 申请书XML
	private Document psnDoc;// 个人信息XML
	private Document orgDoc;// 单位信息XML
	private Document prjDoc;// 单位信息XML
	
	@Autowired
	private BaseXmlNoTransInitService baseXmlNewInit;

	/**
	 * 获取需要查询的信息.
	 * 
	 * @param type
	 * @param map
	 * @return
	 * @throws DocumentException
	 */
	@Override
	public Document getDocument(String type, Map<String, Object> map, String content) throws DocumentException {
		Long keyCode = new Long(0);
		if (content != null) {//
			// 替换SQL中参数
			List<Object> params = new ArrayList<Object>();

			content = IrisStringUtils.transSql(content, map, params);
			List<Map<String, Object>> list = baseXmlNewInit.getRefreshList(content, params);
			if (list.size() == 1 && !list.get(0).isEmpty()) {
				String strKeyCode = list.get(0).get("KEYCODE").toString();
				if (NumberUtils.isDigits(strKeyCode)) {
					keyCode = Long.valueOf(strKeyCode);
				}
			}
		}
		if (ServiceConstants.PSN_XML.equalsIgnoreCase(type)) {
			org.w3c.dom.Document doc = null;
			Object objOrgCode = map.get("psn_code");

			if (objOrgCode == null || (Long) objOrgCode == 0) {// 如果出错则不刷新
				return null;
			}

			if (keyCode == 0) {
				doc = baseXmlNewInit.getPersonExtend((Long) objOrgCode);

			} else {
				if (baseXmlNewInit.getPerson(keyCode).getEdited() == false) {
					return null;
				}
				doc = baseXmlNewInit.getPersonExtend(keyCode);

			}
			psnDoc = XMLHelper.parseDoc(doc);
			return psnDoc;
		} else if (ServiceConstants.ORG_XML.equalsIgnoreCase(type)) {
			org.w3c.dom.Document doc = null;
			Object objOrgCode = map.get("org_code");
			if (objOrgCode == null || (Long) objOrgCode == 0) {// 如果出错则不刷新
				return null;
			}
			if (keyCode == 0) {
				if (Long.valueOf(objOrgCode.toString()) != 0) {
					doc = baseXmlNewInit.getOrgExtendInfo((Long) objOrgCode).getExtInfo();
				} else {
					doc = null;
				}

			} else {
				doc = baseXmlNewInit.getOrgExtendInfo(keyCode).getExtInfo();

			}
			orgDoc = XMLHelper.parseDoc(doc);

			return orgDoc;
		} else if (ServiceConstants.PRP_XML.equalsIgnoreCase(type)) {// 申请书XML

			if (null != map.get("prp_xml")) {
				prpDoc = DocumentHelper.parseText((String) map.get("prp_xml"));
			}

			return prpDoc;
		} else if (ServiceConstants.PRJ_XML.equalsIgnoreCase(type)) {
			org.w3c.dom.Document doc = null;
			Object objPrjCode = map.get("prj_code");
			if (objPrjCode == null || (Long) objPrjCode == 0) {// 如果出错则不刷新
				return null;
			}
			if (keyCode == 0) {
				doc = baseXmlNewInit.getProject((Long) objPrjCode).getPrjXml();
			} else {
				doc = baseXmlNewInit.getProject(keyCode).getPrjXml();
			}
			prjDoc = XMLHelper.parseDoc(doc);

			return prjDoc;
		}

		return null;
	}
}
