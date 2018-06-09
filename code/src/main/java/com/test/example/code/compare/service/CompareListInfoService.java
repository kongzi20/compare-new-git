package com.test.example.code.compare.service;

import java.util.List;
import java.util.Set;

import com.test.example.code.compare.model.PrpCompareListVO;
import com.test.example.core.cp.model.CompareListInfo;

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
