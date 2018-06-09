package com.test.example.schedule.slave.rule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.test.example.code.compare.dao.CompareMybatisDao;
import com.test.example.code.compare.model.CompareResultVO;
import com.test.example.code.rule.dao.ElCompnent;
import com.test.example.code.rule.dao.PrpRuleCheckResultDao;
import com.test.example.code.rule.model.PcheckMessage;
import com.test.example.code.rule.model.ProposalRule;
import com.test.example.code.rule.model.PrpRuleCheckResult;
import com.test.example.code.rule.service.PcheckExpService;
import com.test.example.code.rule.service.ProposalRuleCheckTransService;
import com.test.example.code.rule.service.ProposalRuleParamService;
import com.test.example.code.rule.service.ProposalRuleService;
import com.test.example.core.thread.ThreadPoolHolder;
import com.test.example.schedule.slave.common.AbstractMessageServiceListener;

/**
 *   规则计算
 * @author cg
 *
 */
@Component("proposalRuleCheckService")
public class ProposalRuleCheckService extends AbstractMessageServiceListener<PrpRuleCheckResult> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ProposalRuleService proposalRuleService;
	@Autowired
	private ElCompnent elCompnent;
	@Autowired
	private PcheckExpService pcheckExpService;
	@Autowired
	private ProposalRuleParamService proposalRuleParamService;
	@Autowired
	private ProposalRuleCheckTransService transService;
	@Autowired
	private CompareMybatisDao<CompareResultVO> compareMybatisDao ;
	@Autowired
	private PrpRuleCheckResultDao prpRuleCheckResultDao ;
	
	public ProposalRuleCheckService() {
		super();
		super.setThreadPoolHolderBeanId("proposalRuleCheckThreadPoolHolder");
	}

	@Override
	protected List<PrpRuleCheckResult> fetchDb(Long startKeyCode, Long endKeyCode) {
		return  prpRuleCheckResultDao.getPrpRuleCheckResultList(startKeyCode, endKeyCode);
	}

	@Override
	public void doService(List<PrpRuleCheckResult> prpRuleCheckResultList) {
		for (PrpRuleCheckResult prpRuleCheckResult : prpRuleCheckResultList) {
			Long prpCode = prpRuleCheckResult.getPkey().getPrp_code();
			Long ruleCode = prpRuleCheckResult.getPkey().getRule_code();
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				if (prpRuleCheckResult.getPkey().getPrp_code().longValue() < 0)
					continue;
				param.put("data_id", prpCode.toString());
				ProposalRule proposalRule = proposalRuleService.getRuleById(ruleCode);
				PcheckMessage pcheckMessage = elCompnent
						.getElEngRule(proposalRule, pcheckExpService.parseEngRuleParam(param, proposalRuleParamService.getPrParamsByRuleId(ruleCode)));
				if (prpRuleCheckResult.getVallidateType().equals("0")) { //系统计算
					prpRuleCheckResult.setRestoreFinishDate(new Date());
					prpRuleCheckResult.setUserCustomValues(pcheckMessage.getMessage());
					prpRuleCheckResult.setSysCustomValues(pcheckMessage.getMsgZhCnCmp());
					prpRuleCheckResult.setStatus(pcheckMessage.isResult() ? "0" : "1");
					prpRuleCheckResult.setIsRestore("1");
					prpRuleCheckResult.setFaverMsg(pcheckMessage.getFaverMsg());
					prpRuleCheckResult.setRealityMsg((pcheckMessage.getRealityMsg() == null || "null".equals(pcheckMessage.getRealityMsg())) ? "" : pcheckMessage.getRealityMsg());
					prpRuleCheckResult.setResultDesc(pcheckMessage.getRuleDesc());
					prpRuleCheckResult.setKeyCodes(pcheckMessage.getKeyCodes());
					prpRuleCheckResult.setWarnLevel(proposalRule.getWarnLevel());
					transService.savePrpRuleCheckResult(prpRuleCheckResult);
				}else if(prpRuleCheckResult.getVallidateType().equals("2")){ //数据同步
					prpRuleCheckResult.setIsRestore("1");
					prpRuleCheckResult.setRestoreFinishDate(new Date());
					prpRuleCheckResult.setFaverMsg(proposalRule.getFaverMsg());
					prpRuleCheckResult.setRealityMsg(proposalRule.getRealityMsg());
					prpRuleCheckResult.setResultDesc(proposalRule.getRuleDesc());
					prpRuleCheckResult.setWarnLevel(proposalRule.getWarnLevel());
					transService.savePrpRuleCheckResult(prpRuleCheckResult);
					transService.updatePrpExtendCnt(prpCode, "compare_prp".equalsIgnoreCase(proposalRule.getRuleCategory()) ? "3" : "1");
				}else if (pcheckMessage.isResult()) {
					prpRuleCheckResult.setRestoreFinishDate(new Date());
					prpRuleCheckResult.setUserCustomValues(pcheckMessage.getMessage());

					prpRuleCheckResult.setSysCustomValues(pcheckMessage.getMsgZhCnCmp());
					prpRuleCheckResult.setIsRestore("1");
					prpRuleCheckResult.setIsConfirm("0");

					prpRuleCheckResult.setFaverMsg(pcheckMessage.getFaverMsg());
					prpRuleCheckResult.setRealityMsg((pcheckMessage.getRealityMsg() == null || "null".equals(pcheckMessage.getRealityMsg())) ? "" : pcheckMessage.getRealityMsg());
					prpRuleCheckResult.setResultDesc(pcheckMessage.getRuleDesc());
					prpRuleCheckResult.setKeyCodes(pcheckMessage.getKeyCodes());
					prpRuleCheckResult.setWarnLevel(proposalRule.getWarnLevel());
					transService.savePrpRuleCheckResult(prpRuleCheckResult);
				} else {
					transService.deletePrpRuleCheckResult(prpRuleCheckResult);
				}
			} catch (Exception e) {
				prpRuleCheckResult.setIsRestore("1");
				prpRuleCheckResult.setStatus("2");
				transService.savePrpRuleCheckResult(prpRuleCheckResult);
				logger.error("prp_code=" + prpCode + ";rule_code=" + ruleCode + "规则检查错误", e);
			} finally {
				// wk modify 2014-9-23 更新规则检查违规数目
				transService.updatePrpExtendCnt(prpCode, "2");
			}
		}
	}

}

@Component("proposalRuleCheckThreadPoolHolder")
class ProposalRuleCheckThreadPoolHolder implements ThreadPoolHolder{
	
	private ReentrantLock lock = new ReentrantLock() ;
	
	private ThreadPoolExecutor threadPoolExecutor  = (ThreadPoolExecutor) Executors.newFixedThreadPool(1) ;
	
	public ProposalRuleCheckThreadPoolHolder() {
		super();
	}
	
	@Override
	public void lock() {
		lock.lock(); 
	}
	@Override
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}
	@Override
	public void unLock() {
		lock.unlock();
	}
}
