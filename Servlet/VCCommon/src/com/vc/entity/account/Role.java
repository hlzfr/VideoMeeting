package com.vc.entity.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vc.entity.BaseEntity;


@Entity
@Table(name = "T_ROLE")
public class Role extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	
	private List<Permission> permissionList = new ArrayList<Permission>();
	
	private List<User> userList= new ArrayList<User>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(mappedBy = "role")
	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}

	@ManyToMany
	@JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	@Transient
	public List<String> getPermissionName() {
		int size = permissionList != null ? permissionList.size() : 0;
		List<String> list = new ArrayList<String>(size);
		if (permissionList != null) {
			for (Permission permission : permissionList) {
				list.add(permission.getName());
			}
		}
		return list;
	}
}
