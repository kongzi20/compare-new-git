package com.test.example.code.solr.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.test.example.code.solr.constant.SqlConstants;

@Repository
public class SolrDocumentJDBCDao {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取所有可导入solr的项目key（六维度）
	 * @return
	 */
    public List<Map<String, Object>> getAllKeyCode(String type) {
		
    	List<Map<String, Object>> objList = this.jdbcTemplate.queryForList(SqlConstants.getAllKeyCode, new Object[] { type  });
		return objList;
		
	}
    
    
    /**
     * 获取所有可导入solr的项目key（单维度）
     * @param dataType 维度类型（参考TypeConstants）
     * @param type 项目类型（参考TypeConstants）
     * @return
     */
    public List<Map<String, Object>> getAllSingleAimensionKeyCode(String dataType , String type) {
		
    	List<Map<String, Object>> objList = this.jdbcTemplate.queryForList(SqlConstants.getAllSingleAimensionKeyCode, new Object[] { dataType , type  });
		return objList;
		
	}
    
    
    
    /**
     * 根据keyCode和type获取具体的项目信息
     * @param keyCode
     * @param keyCode
     * @return
     */
    public List<Map<String, Object>> getItemInfoByKeyCode(String keyCode,String type) {
		
    	Long keyCodeLong = Long.parseLong(keyCode);
    	List<Map<String, Object>> objList = this.jdbcTemplate.queryForList(SqlConstants.getItemInfoByKeyCode, new Object[] { keyCodeLong ,type });
		return objList;
		
	}
    
    
    /**
     * 根据keyCode和dataType和type获取具体的项目信息
     * @param keyCode
     * @param dataType
     * @param type
     * @return
     */
    public List<Map<String, Object>> getItemInfoByKeyCodeAndDataType(String keyCode,String dataType,String type) {
		
    	Long keyCodeLong = Long.parseLong(keyCode);
    	List<Map<String, Object>> objList = this.jdbcTemplate.queryForList(SqlConstants.getItemInfoByKeyCodeAndDataType, new Object[] { keyCodeLong ,dataType ,type });
		return objList;
		
	}
    
    
    /**
     * 单维度总数
     * @param dataType
     * @param type
     * @return
     */
    public List<Map<String, Object>> getTotalSingleAimensionKeyCode(String dataType , String type) {
		
    	List<Map<String, Object>> objList = this.jdbcTemplate.queryForList(SqlConstants.getTotalSingleAimensionKeyCode, new Object[] { dataType , type  });
		return objList;
		
	}
    
    /**
     * 获取维度信息
     * @param idStr 主键
     * @return
     */
    public List<Map<String, Object>> getItemContentById(String idStr) {
		
    	Long idLong = Long.parseLong(idStr);
    	List<Map<String, Object>> objList = this.jdbcTemplate.queryForList(SqlConstants.getItemContentById, new Object[] { idLong });
		return objList;
		
	}
    
	
}
