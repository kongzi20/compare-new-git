package com.test.example.code.compare.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.test.example.code.compare.model.CompareListSnapshot;
import com.test.example.core.cp.model.CompareListInfo;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;

@Repository
public class CompareListInfoDao extends SimpleHibernateDao<CompareListInfo, Long>{

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	public CompareListInfo getCompareListInfoByCode(Long keyCode){
		String sql = "from CompareListInfo c where c.keyCode = ?";
		List<CompareListInfo> list = (List<CompareListInfo>) hibernateTemplate.find(sql, keyCode);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
 
	@Cacheable(value = "compareListInfoByDataType", key = "#dataType")
	public List<CompareListInfo>  getCompareListInfoByDataType(Integer  dataType){
		String queryString = "select c from CompareListInfo c where c.dataType = :dataType";
		List<CompareListInfo> list =  super.createQuery(queryString).setParameter("dataType", dataType).setFetchSize(1000).list() ;
		return list ;
	}

	@SuppressWarnings("unchecked")
	public List<CompareListInfo> getCompareListAllByCode(Long keyCode){
		String sql = "from CompareListInfo c where c.keyCode = ? order by c.dataType";
		List<CompareListInfo> list = (List<CompareListInfo>) hibernateTemplate.find(sql, keyCode);
		if(list.size()>0){
			return list;
		}else{
			String sql1 = "from CompareListSnapshot c where c.keyCode = ?";
			List<CompareListSnapshot> list1 = (List<CompareListSnapshot>) hibernateTemplate.find(sql1, keyCode);
			
			if(list1.size()>0){
				for(CompareListSnapshot cl : list1){
					CompareListInfo cli = new CompareListInfo();
					cli.setId(cl.getId());
					cli.setKeyCode(cl.getKeyCode());
					cli.setType(cl.getType());
					cli.setDataType(cl.getDataType());
					cli.setContent(cl.getContent());
					list.add(cli);
				}
				return list;
			}else{
				return null;
			}
		}
	}


	public Set<Long> getKeyCodeByIds(List<Long> idList) {
		String sql = "select distinct key_code as key_code from compare_list where id in (:idList)  " ;
		List<BigDecimal> listKeyCode = super.createSqlQuery(sql, new ArrayList<Object>()).setParameterList("idList", idList).list() ;
		Set<Long> s = new HashSet<Long>() ;
		for (BigDecimal l : listKeyCode) {
			s.add(l.longValue());
		}
		return  s;
	}
	
}
