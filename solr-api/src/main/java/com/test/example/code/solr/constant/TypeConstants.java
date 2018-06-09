package com.test.example.code.solr.constant;

import java.util.HashMap;
import java.util.Map;

public class TypeConstants {
    
	//项目内容
	public static final String content = "1";
	//可行性报告
	public static final String executableReport = "2";
	//项目名称
	public static final String title = "3";
	//设备
	public static final String equipment = "4";
	//团队
	public static final String team = "5";
	//核准文号信息 
	public static final String approval = "6";
	
	//申请书类型
	public static final String prpType = "1";
	
	
	public static Map<String, String> typeMap = new HashMap<String, String>();
	static {
		typeMap.put(TypeConstants.content, "Content");
		typeMap.put(TypeConstants.executableReport, "ExecutableReport");
		typeMap.put(TypeConstants.title, "Title");
		typeMap.put(TypeConstants.equipment, "Equipment");
		typeMap.put(TypeConstants.team, "Team");
		typeMap.put(TypeConstants.approval, "Approval");
	}
	
	public static Map<String, String> solrCoresTypeMap = new HashMap<String, String>();
	static {
		solrCoresTypeMap.put(TypeConstants.content, "solr_cores_content");
		solrCoresTypeMap.put(TypeConstants.executableReport, "solr_cores_executableReport");
		solrCoresTypeMap.put(TypeConstants.title, "solr_cores_title");
		solrCoresTypeMap.put(TypeConstants.equipment, "solr_cores_equipment");
		solrCoresTypeMap.put(TypeConstants.team, "solr_cores_team");
		solrCoresTypeMap.put(TypeConstants.approval, "solr_cores_approval");
	}
}
