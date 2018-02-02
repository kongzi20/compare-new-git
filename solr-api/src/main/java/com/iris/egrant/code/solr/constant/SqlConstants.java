package com.iris.egrant.code.solr.constant;

public class SqlConstants {

	public static final String getAllKeyCode = "select t.id from compare_list t where t.data_type = 3  and t.type = ? and t.content  is not null";
	
	public static final String getAllSingleAimensionKeyCode = "select t.id from compare_list t where t.data_type = ?  and t.type = ? and t.content  is not null";
	
	public static final String getItemInfoByKeyCode = "select t.content , t.data_type from compare_list t where t.key_code = ? and t.type = ? ";
	
	public static final String getItemInfoByKeyCodeAndDataType = "select t.id , t.content , t.data_type from compare_list t where t.key_code = ? and t.data_type = ? and t.type = ?";
	
	public static final String getItemContentById = "select t.id , t.key_code , t.content , t.data_type from compare_list t where t.id = ?";
	
	public static final String getTotalSingleAimensionKeyCode = "select count(0) as total from compare_list t where t.data_type = ?  and t.type = ? and t.content  is not null";
}
