package com.iris.egrant.code.compare.service;

import java.util.List;
import java.util.Set;

import com.iris.egrant.code.compare.model.PrpCompareListVO;
import com.iris.egrant.core.cp.model.CompareListInfo;

/**
 * 
 * @author cg
 *
 */
public interface CompareListInfoService {

	List<CompareListInfo> getCompareListInfoByDataType(Integer dataType);
	
	void updateCompareListStatusByKeyCode(Long keyCode, String status) ;
	
	void initCompareResultAndUpdateCompareList( PrpCompareListVO prpCompareListVO, Set<Long> keyCodeSet);

	Set<Long> getKeyCodeByIds(List<Long> set);

}
