package com.iris.egrant.code.compare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.egrant.code.compare.dao.CompareHibernateDao;
import com.iris.egrant.code.compare.dao.CompareListInfoDao;
import com.iris.egrant.code.compare.dao.CompareSourceDao;
import com.iris.egrant.code.compare.model.PrpCompareListVO;
import com.iris.egrant.core.cp.model.CompareListInfo;
import com.iris.egrant.core.exception.DaoException;

@Service("compareListInfoService")
public class CompareListInfoServiceImpl implements CompareListInfoService {
	
	@Autowired
	private CompareListInfoDao compareListInfoDao ;
	
	@Autowired
	private CompareSourceDao compareSourceDao ;
	
	@Autowired
	private CompareHibernateDao<PrpCompareListVO ,Long> cpHibernateDao ;

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly=true )
	public List<CompareListInfo> getCompareListInfoByDataType(Integer dataType){
		 return compareListInfoDao.getCompareListInfoByDataType(dataType) ;  // 已做缓存
	}
	
	/**
	 *    初始化compare_result并更新compareList状态
	 * @param prpCompareListVO
	 * @param keyCodeSet
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void initCompareResultAndUpdateCompareList( PrpCompareListVO prpCompareListVO, Set<Long> keyCodeSet) {
		 for (CompareListInfo compareList : prpCompareListVO.getCompareLists()) {
			 String status = "1"  ;
			 try {
				compareSourceDao.initCompareResult(
						  prpCompareListVO.getKeyCode(), 
						  compareList.getType(), 
						   compareList.getDataType(),
						   compareList.getContent() ,
						   keyCodeSet.toArray() );
			} catch (DaoException e) {
				e.printStackTrace();
				status = "2" ;
			}
		   // 更新单条CompareList状态 
			 cpHibernateDao.updateCompareListStatusByID(compareList.getId() , status);
		 }
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateCompareListStatusByKeyCode(Long keyCode, String status) {
		 cpHibernateDao.updateCompareListStatusByKeyCode(keyCode ,  status); // keycode对应的compare_list批量更新
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly=true )
	public Set<Long> getKeyCodeByIds(List<Long> idList) {
		return compareListInfoDao.getKeyCodeByIds(idList) ;
	}
}
