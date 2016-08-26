package com.vc.entity.account;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vc.entity.BaseEntity;


@Entity
@Table(name = "T_PERMISSION")
public class Permission extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	
	private Role role;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "role_id")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
