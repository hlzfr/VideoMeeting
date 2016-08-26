package com.vc.entity.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.vc.entity.BaseEntity;
import com.vc.utils.TextUtil;

/**
 * @author Jianbin
 *
 */
@Entity
@Table(name = "T_USER")
public class User extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "用户名不能为空")
	private String username;
	@NotEmpty(message = "密码不能为空")
	private String password;
	
	private String avatar;
	
	private String nickname;
	
	private String phoneNumber;

	private String salt;

	private List<Role> roleList = new ArrayList<Role>();
	
	public User() {
		
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Column(name = "username", length = 20)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 225)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "avatar", length = 225)
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	@Column(name = "nickname", length = 20)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Column(name = "phoneNumber", length = 11)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@ManyToMany
	@JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Transient
	public void update(User user) {
		// 只有nickname和avatar可以修改
		if(!TextUtil.isEmpty(user.getAvatar())) {
			this.avatar = user.getAvatar();
		}
		if(!TextUtil.isEmpty(user.getNickname())) {
			this.nickname = user.getNickname();
		}
	}
	
	@Transient
	public Set<String> getRoleName() {
		int size = (roleList == null ? 0 : roleList.size());
		Set<String> set = new HashSet<String>(size);
		if (roleList != null) {
			for (Role role : roleList) {
				set.add(role.getName());
			}
		}
		return set;
	}

	@Transient
	public String getCredentialsSalt() {
		return username + salt;
	}
	
	@Transient
	@Override
	public String toString() {
		return "{\"username\":\""+username+"\"}";
	}
}
