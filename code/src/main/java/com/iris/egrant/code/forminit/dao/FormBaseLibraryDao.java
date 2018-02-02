package com.iris.egrant.code.forminit.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.iris.egrant.code.forminit.model.FormBaseLibrary;
import com.iris.egrant.code.forminit.model.FormInitItem;
import com.iris.egrant.code.forminit.model.FormInitTab;
import com.iris.egrant.core.dao.hibernate.SimpleHibernateDao;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.exception.ServiceException;
import com.iris.egrant.core.utils.IrisStringUtils;

 

/**
 * TODO：请添加注释(修改好了把TODO删除)lqh add.
 * 
 * 
 * 
 */
@Repository
public class FormBaseLibraryDao extends SimpleHibernateDao<FormBaseLibrary, Long> {

	/**
	 * 根据form_id获得目前正在使用的模板.
	 * 
	 * @param formId
	 * @return
	 * @throws DaoException
	 */
	public FormBaseLibrary getFormBaseLibraryByFormId(Long formId) throws DaoException {
		String sql = "from FormBaseLibrary t where  t.formId=? and t.status='1'";
		return this.findUnique(sql, new Object[] { formId });
	}

	/**
	 * 根据form_code获得目前正在使用的模板.
	 * 
	 * @param formCode
	 * @return
	 * @throws DaoException
	 */
	public FormBaseLibrary getFormBaseLibraryByFormCode(Long formCode) throws DaoException {
		String sql = "from FormBaseLibrary t where  t.formCode=?";
		return this.findUnique(sql, new Object[] { formCode });
	}

	/**
	 * 通过formItems列表获得需刷新XML节点列表.
	 * 
	 * @param mainpageId
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	public List<FormInitItem> getFormInitItemListById(String formItems) throws DaoException, ServiceException {
		String sql = "select t from FormInitItem t where  t.itemCode in ( :formitems ) order by t.seqNo";
		List<Long> formitems = IrisStringUtils.getSplitLong(formItems);
		Query query = super.getSession().createQuery(sql);
		query.setParameterList("formitems", formitems);
		@SuppressWarnings("unchecked")
		List<FormInitItem> result = query.list();
		return result;

	}

	/**
	 * 通过formTabCode获得初始化页面列表.
	 * 
	 * @param mainpageId
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	public List<FormInitTab> getFormInitTabListById(String formTabs) throws DaoException, ServiceException {
		String sql = "select t from FormInitTab t where  t.formTabCode in ( :formtabs )";
		List<Long> formtabs = IrisStringUtils.getSplitLong(formTabs);
		Query query = super.getSession().createQuery(sql);
		query.setParameterList("formtabs", formtabs);
		@SuppressWarnings("unchecked")
		List<FormInitTab> list = query.list();

		return list;
	}

	/**
	 * 返回SQL查询的字段 参数（传入的SQL语句）.
	 * 
	 * @param sqlcontent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRefreshList(String sqlcontent, List<Object> params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = super.queryForList(sqlcontent, params.toArray());
		// Query query = super.createQuery(sqlcontent, params);
		// list = query.list();
		return list;
	}

}
