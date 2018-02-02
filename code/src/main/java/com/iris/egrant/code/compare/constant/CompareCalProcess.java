package com.iris.egrant.code.compare.constant;

/**
 * 常量 项目比对过程 
 * @author cg
 *
 */
public enum CompareCalProcess{
	
	/**
	 * 数据初始化
	 */
	 DATA_INIT( "DATA_INIT" ) ,
	/**
	 * 过滤
	 */
	 DATA_FILTER ( "DATA_FILTER" ) ,
	/**
	 * 相似度计算
	 */
	 DATA_SIMILIAR ("DATA_SIMILIAR" ),
	/**
	 * 结果同步
	 */
	 DATA_RESULT_SYNC ( "DATA_RESULT_SYNC" );
	 
	 private final String value; 
	 
   private CompareCalProcess(String value) { 
      this.value = value; 
      }

		public String getValue() {
			return value;
		}
		
}
