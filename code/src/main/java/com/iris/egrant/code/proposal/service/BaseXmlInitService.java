package com.iris.egrant.code.proposal.service;

import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;

public interface BaseXmlInitService {

	public Document getDocument(String type, Map<String, Object> map, String content) throws DocumentException;
}
