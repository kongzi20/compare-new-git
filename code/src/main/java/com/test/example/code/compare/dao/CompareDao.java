package com.test.example.code.compare.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.test.example.code.compare.model.CompareResult;
import com.test.example.code.compare.model.CompareTemplateSetting;
import com.test.example.core.cp.model.CompareListInfo;


@Repository
public class CompareDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	/**
	 * 获取待处理对比任务列表
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CompareResult> getCompareTask() {
		String sql = "from CompareResult t where t.status = 0";
		return (List<CompareResult>)hibernateTemplate.find(sql);
	}

	/**
	 * 修改对比结果
	 *
	 * @param result
	 */
	public void update(CompareResult result) {
		String sql = "update CompareResult  set compareResult = ?,status = ? where id = ?";
		hibernateTemplate.bulkUpdate(sql, result.getCompareResult() == null ? 0 : result.getCompareResult(), result.getStatus(), result.getId());
	}

	public CompareListInfo getCompareListInfoById(Long id){
		return hibernateTemplate.get(CompareListInfo.class, id);
	}
	
	/**
	 * wk add 2014-5
	 * 通过数据类型获取对应方法beanName
	 * @param dataType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Cacheable(value = "compareTemplateSettingCache", key = "#dataType")
	public CompareTemplateSetting getCompareBeanNameByDataType(Integer dataType) {
		String sql = "from CompareTemplateSetting where dataType = ?";
		List<CompareTemplateSetting> list =  (List<CompareTemplateSetting>) hibernateTemplate.find(sql, dataType);
		if(list.size() > 0 )
			return list.get(0);
		else
			return null;
	}
}
