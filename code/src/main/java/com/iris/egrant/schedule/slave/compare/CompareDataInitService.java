package com.iris.egrant.schedule.slave.compare;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.iris.egrant.code.compare.dao.CompareHibernateDao;
import com.iris.egrant.code.compare.model.CompareSource;
import com.iris.egrant.code.compare.service.CompareSourceService;
import com.iris.egrant.core.exception.ServiceException;
import com.iris.egrant.core.utils.ServiceUtils;
  
/**
 *    比对数据初始化服务
 * @author cg
 *
 */
@Component("compareDataInitService")
public class CompareDataInitService extends CompareServiceListener<CompareSource> {

	@Autowired
	private CompareHibernateDao<CompareSource ,Long> cpHibernateDao ;
	
	@Autowired
	private CompareSourceService compareSourceService ;
		
	@SuppressWarnings("unchecked")
	@Override
  @Transactional(rollbackFor = Exception.class,readOnly=true )
	protected List<CompareSource> fetchDb(Long startKeyCode, Long endKeyCode) {
		return 	cpHibernateDao.createQuery("select cs from CompareSource cs where 1=1 and cs.status = :status and cs.id >= :startKeyCode and cs.id <= :endKeyCode" )
				.setParameter("status", 0)
				.setParameter("startKeyCode", startKeyCode)
				.setParameter("endKeyCode", endKeyCode)
				.setFetchSize(2000)
				.list() ;
	}

	@Override
	public void doService(List<CompareSource> list) {
		for (CompareSource cs : list) {
			try {
				String content = cs.getContent() ;
			//	if (cs.getDataType().intValue() != 2 ){  	// TODO 测试暂写 生产数据导回 有些可行性报告已拆好 无需本地再次拆分
				 content = compareSourceService.getContentByDataType(cs);
				//}
				cs.setContent(content);
				cs.setStatus(1);
				cs.setErrMsg("");
				//  调用插入至compare_list和comopare_result表的存储过程
			   compareSourceService.callExecByDataType(cs.getKeyCode(), cs.getType(), cs.getDataType(), content);
			} catch (Exception e) {
				try {
					String errorMsg = ServiceUtils.getErrorTranceStr(e);
					errorMsg = StringUtils.substring(StringUtils.replace(errorMsg, "\"", "'"), 0, 3500);
				} catch (Exception ex) {
				}
				cs.setStatus(2);
				cs.setErrMsg(e.getMessage());
			} finally {
				cs.setCompleteDate(new Date());
				try{
					compareSourceService.updateCompareSource(cs);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
	}
}