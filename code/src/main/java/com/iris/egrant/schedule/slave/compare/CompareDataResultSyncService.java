package com.iris.egrant.schedule.slave.compare;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.egrant.code.compare.dao.CompareMybatisDao;
import com.iris.egrant.code.compare.model.CompareResultSyncVO;
import com.iris.egrant.code.compare.model.CompareResultVO;
import com.iris.egrant.code.compare.service.CompareResultService;
import com.iris.egrant.schedule.slave.common.AbstractMessageServiceListener;

/**
 *  项目比对结果同步
 * @author cg
 *
 */
@Component("compareDataResultSyncService")
public class CompareDataResultSyncService extends CompareServiceListener<CompareResultSyncVO>  {
	
	@Autowired
	private CompareResultService compareResultService;

	@Autowired
	private CompareMybatisDao<CompareResultVO> compareMybatisDao ;
	
	@Override
	protected List<CompareResultSyncVO> fetchDb(Long startKeyCode, Long endKeyCode) {
		return  compareMybatisDao.getCompareResultSyncVOList(startKeyCode, endKeyCode);
	}

	@Override
	public void doService(List<CompareResultSyncVO> objList) {
		for (CompareResultSyncVO compareResultSyncVO : objList) {
			String type = ObjectUtils.toString(compareResultSyncVO.getType());
			String prpCode = ObjectUtils.toString(compareResultSyncVO.getPrpCode());
			compareResultService.callUpdatePrpExtendCnt(type, prpCode);
		}
	}
	
}
