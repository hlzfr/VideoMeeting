package com.vc.service;

import com.vc.entity.Page;
import com.vc.entity.account.User;

public interface UserService {
	public User register(String username, String password);

	public User login(String username, String password);

	public boolean logout();
	
	public boolean unregister(User user);
	
	public User update(User user);
	
	public User getByUserName(String name);
	
	public Page<User> getUserPage(int page, int limit, String where, Object[] params);
}
