package com.test.example.code.wf.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.test.example.code.wf.model.WfAction;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;

/**
 * 流程对应校验条件Dao.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class WfActionDao extends SimpleHibernateDao<WfAction, Long> {
	public WfAction findWfAction(Long id) {
		return (WfAction) this.getSession().get(WfAction.class, id);
	}

	/**
	 * 根据wfId,lineId查找wfAction
	 * 
	 * @param wfId
	 * @param lineId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findWfActionByWfIdAndLineId(Long wfId, String lineId) {
		String sql = "select w.id,w.condition_ids,w.validate_ids,w.event_ids,w.is_send_mail,w.is_send_sms, w.mail_tmp_code,w.sms_tmp_code from wf_action w"
				+ " left join wf_lines l on l.action_id=w.id where l.wf_id=? and l.line_id=? ";
		return this.getSession().createSQLQuery(sql).setParameter(0, wfId).setParameter(1, lineId).list();

	}

	public void updateWfActor(Long id, String conditionIds, String validateIds, String eventIds) {
		String sql = "update wf_action w set w.CONDITION_IDS=?,w.validate_ids=?,w.event_ids=? where w.id=?";
		this.getSession().createSQLQuery(sql).setParameter(0, conditionIds).setParameter(1, validateIds)
				.setParameter(2, eventIds).setParameter(3, id).executeUpdate();
	}
	public void updateWfActionWithConditions(Long id,String conditionIds){
		String sql = "update wf_action w set w.CONDITION_IDS=? where w.id=?";
		this.getSession().createSQLQuery(sql).setParameter(0, conditionIds).setParameter(1, id).executeUpdate();
	}
	
	public void updateWfActionWithValidation(Long id,String validationIds){
		String sql = "update wf_action w set w.validate_ids=? where w.id=?";
		this.getSession().createSQLQuery(sql).setParameter(0, validationIds).setParameter(1, id).executeUpdate();
	}
	
	public void updateWfActionWithEventIds(Long id,String eventIds){
		String sql = "update wf_action w set w.event_ids=? where w.id=?";
		this.getSession().createSQLQuery(sql).setParameter(0, eventIds).setParameter(1, id).executeUpdate();
	}
	
}
