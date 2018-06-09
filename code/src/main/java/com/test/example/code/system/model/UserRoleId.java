package com.test.example.code.system.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 人员角色表 .
 * 
 * @author zb
 * 
 */
@Embeddable
public class UserRoleId implements java.io.Serializable {

	private static final long serialVersionUID = 560960916213063043L;

	// Fields
	private long userId;
	private long rolId;
	private long insId;

	public UserRoleId() {
	}

	public UserRoleId(long userId, long rolId) {
		this.userId = userId;
		this.rolId = rolId;
	}

	// Property accessors

	@Column(name = "USER_ID")
	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	// Constructors

	@Column(name = "ROLE_ID")
	public long getRolId() {
		return rolId;
	}

	public void setRolId(long rolId) {
		this.rolId = rolId;
	}
	
	@Column(name = "INS_ID")
	public long getInsId() {
		return insId;
	}

	public void setInsId(long insId) {
		this.insId = insId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (rolId ^ (rolId >>> 32));
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + (int) (insId ^ (insId >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserRoleId other = (UserRoleId) obj;
		if (rolId != other.rolId) {
			return false;
		}
		if (userId != other.userId) {
			return false;
		}
		if(insId != other.insId){
			return false;
		}
		return true;
	}

}