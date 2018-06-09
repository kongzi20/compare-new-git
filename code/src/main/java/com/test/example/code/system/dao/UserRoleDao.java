package com.test.example.code.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.test.example.code.system.model.UserRole;
import com.test.example.code.system.model.UserRoleId;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;

/**
 * 注册人員登陸信息DAO.
 * 
 * 
 * 
 */
@Repository
public class UserRoleDao extends SimpleHibernateDao<UserRole, UserRoleId> {

	/**
	 * 获取系统管理员角色.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserRole> getSysAdministrator() {

		// TODO:请不要直接写死为4，建议使用常量替代(修改好了把TODO删除)lqh add.
		String ql = "from UserRole where id.rolId = 4";
		List<UserRole> users = super.createQuery(ql).list();
		return users;

	}

	/**
	 * 获取人员角色列表.
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserRole> getUserRoles(Long userId) {

		String ql = "from UserRole where id.userId = ?";
		List<UserRole> roles = super.createQuery(ql, userId).list();
		return roles;

	}

	/**
	 * 人员是否已经存在该角色.
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public boolean existsUserRole(Long userId, Long roleId) {
		String ql = "From UserRole where id.userId=? and id.rolId=?";
		UserRole userRole = (UserRole) super.createQuery(ql, new Object[] { userId, roleId }).uniqueResult();

		return userRole != null;

	}
	
	public UserRole findUserRoleByPsnCodeAndOrgCode(Long psnCode,Long offOrgCode){
		String ql = " from UserRole where id.userId = ? and id.insId = ? ";
		UserRole userRole = (UserRole) super.createQuery(ql, new Object[] { psnCode, offOrgCode}).uniqueResult();
		return userRole;
	}
	
	/**
	 * 设置单位管理员的标志
	 * @param userId 用户id 对应psnCode
	 * @param roleId 角色id
	 * @param insId 单位id
	 */
	public void updateUserRole(Long userId,Long roleId,Long insId){
		this.batchExecute("update UserRole set id.insId = ? where id.userId = ? and id.rolId = ?", insId,userId,roleId);
	}

	/**
	 * 将用户的insId全部设置为0
	 * @param userId
	 */
	public void updateUserRole(Long userId){
		this.batchExecute("update UserRole set id.insId = 0 where id.userId = ? ", userId);
	}
	
	/**
	 * 保存或修改用户角色.
	 * 
	 * @param psnCode
	 * @param roles
	 *            用,分割
	 */
	public void saveOrUpdate(Long userId, String roles) {
		this.getSession().createQuery("delete UserRole where id.userId = "+userId +" and  id.rolId not in ("+roles+")").executeUpdate();
		for (String role : roles.split(",")) {
			UserRoleId id = new UserRoleId();
			id.setUserId(userId);
			id.setRolId(Long.valueOf(role));
			UserRole ur = this.get(id);
			if (ur == null) {
				ur = new UserRole();
			}
			ur.setId(id);
			this.save(ur);			
		}
	}

	/**
	 * 判断用户角色配置中是否有不存在的角色.
	 * 
	 * @param userId
	 * @return
	 */
	public boolean lackUserRole(Long userId) {
		String sql = "select count(*) from sys_user_role ur where user_id=? and not exists (select 1 from sys_role where id=ur.role_id)";

		int count = this.queryForInt(sql, new Object[] { userId });

		return count > 0 ? true : false;
	}
}