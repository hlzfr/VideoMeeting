package com.vc.service;

import com.vc.entity.account.User;

public interface AccountService {

	/**
	 * 注册
	 */
	public User register(User user);

	/**
	 * 登录
	 */
	public User login(String username, String password);

	/**
	 * 退出登录
	 */
	public void logout();

	/**
	 * 删除账户
	 */
	public boolean unregister(User user);
	
	/**
	 * 编辑账户资料
	 * @param user
	 * @return
	 */
	public User edit(User user);

}
