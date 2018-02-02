package com.iris.egrant.code.system.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 人员角色.
 * 
 * 使用JPA annotation定义ORM关系. 使用Hibernate annotation定义二级缓存.
 * 
 * 
 */
@Entity
@Table(name = "SYS_USER_ROLE")
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7983297580443857926L;
	private UserRoleId id;
	private String roleName;

	public UserRole() {
		super();
	}

	public UserRole(UserRoleId id) {
		super();
		this.id = id;
	}

	public UserRole(long userId, long rolId) {
		UserRoleId userRoleId = new UserRoleId(userId, rolId);
		this.id = userRoleId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(UserRoleId id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	@Id
	@AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "USER_ID")),
			@AttributeOverride(name = "rolId", column = @Column(name = "ROLE_ID")) })
	public UserRoleId getId() {
		return id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Transient
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
