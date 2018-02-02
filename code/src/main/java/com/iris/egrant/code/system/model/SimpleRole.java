package com.iris.egrant.code.system.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;



/**
 * 角色.
 * 
 * 使用JPA annotation定义ORM关系. 使用Hibernate annotation定义二级缓存.
 * 
 * 
 */
@Entity
@Table(name = "SYS_ROLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SimpleRole extends IdEntity {

	private static final long serialVersionUID = 8563096835601408349L;

	private String name;
	private Set<Person> psns = new HashSet<Person>();

	public SimpleRole() {
	}

	public SimpleRole(Long id) {
		this.setId(id);
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	public Set<Person> getPsns() {
		return psns;
	}

	public void setPsns(Set<Person> psns) {
		this.psns = psns;
	}

}
