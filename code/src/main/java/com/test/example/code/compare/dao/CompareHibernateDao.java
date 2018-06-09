package com.test.example.code.compare.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.test.example.code.compare.model.PrpCompareListVO;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;

@Repository
@SuppressWarnings("unchecked")
public class CompareHibernateDao<T, PK extends Serializable> extends SimpleHibernateDao<T, PK> {
	
/*	private static final String GET_COMPARE_RESULT_SQL =  " select t.result_id, t.source_id, t.target_id, t.data_type , s1.content as source_content,s2.content as target_content "
			                    + " from compare_result t"   
													+ " inner join compare_list s1 on s1.id = t.source_id "
													+ " inner join compare_list s2 on s2.id = t.target_id "
													+ " where 1=1 " 
													+ "  and t.status= '0' "
													+	"  and t.id >= ? "
													+ "  and  t.id < = ? " ;
	
	public List<Map<String, Object>> getCompareResult(Long startKeyCode, 	Long endKeyCode){
		return super.queryForList(GET_COMPARE_RESULT_SQL, new Object[]{startKeyCode , endKeyCode})  ;
	}*/
	
	private static final String GET_COMPARE_LIST_KEY_CODE= " select distinct t.key_code  from compare_list t "
			+ " where 1=1  "
			+ " and t.key_code >= ? "
			+ " and t.key_code <= ? "
			+ " and exists (select 1 from compare_list t2 where t2.key_code = t.key_code and t2.status = '0')" ;  // 存在着单维度compare_list记录状态变化的 所有的keyCode
	
	private static final String GET_COMPARE_LIST_BY_KEY_CODE = "select  cl from CompareListInfo cl where cl.keyCode = :keyCode order by cl.dataType asc" ;
	
	public List<PrpCompareListVO> getPrpCompareListVOList(Long startKeyCode, 	Long endKeyCode) {
		List<PrpCompareListVO> prpCompareListVOList = new ArrayList<PrpCompareListVO>();
		List<Map<String, BigDecimal>> keyCodeListMap =	super.queryForList(GET_COMPARE_LIST_KEY_CODE, new Object[]{startKeyCode , endKeyCode})  ;
		for (Map<String, BigDecimal> map : keyCodeListMap) {
			Long keyCode = map.get("KEY_CODE").longValue();
			List tempList  = super.createQuery(GET_COMPARE_LIST_BY_KEY_CODE).setParameter("keyCode", keyCode).list() ;
			if ( tempList.size() > 0){
				prpCompareListVOList.add(new PrpCompareListVO(keyCode , tempList ));
			}
		}
		return prpCompareListVOList  ;
	}

	public void updateCompareListStatusByID(Long id, String status) {
		super.update( " update compare_list t set  t.status = ? where t.id = ? ", new Object[]{status ,id}) ;
	}
  
	public void updateCompareListStatusByKeyCode(Long keyCode, String status) {
		super.update( " update compare_list t set  t.status = ? where t.key_code = ? ", new Object[]{status , keyCode }) ;
	}

}
