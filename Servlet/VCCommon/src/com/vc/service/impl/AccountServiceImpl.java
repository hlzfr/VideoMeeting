package com.vc.service.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vc.dao.BaseDAO;
import com.vc.entity.account.User;
import com.vc.service.AccountService;
import com.vc.utils.PasswordHelper;

@Service("accountService")
public class AccountServiceImpl extends BaseServiceImpl implements
		AccountService {

	BaseDAO<User> userDao;

	public BaseDAO<User> getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(BaseDAO<User> userDao) {
		this.userDao = userDao;
	}

	@Transactional
	@Override
	public User register(User user) {
		List<User> finduser = userDao.find("from User u where u.username = ?",
				new String[] { user.getUsername() });
		if (finduser == null || finduser.size() == 0) {
			PasswordHelper.getInstance().encryptPassword(user);
			userDao.save(user);
			return user;
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public User login(String username, String password) throws IncorrectCredentialsException{
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(username);
		token.setPassword(password.toCharArray());
		token.setRememberMe(true);
		SecurityUtils.getSubject().login(token);
		if (SecurityUtils.getSubject().isAuthenticated()) {
			SecurityUtils.getSubject().getSession().setAttribute("username", username);
			return userDao.get("from User u where u.username = ?",
					new String[] { username });
		}
		return null;
	}

	@Override
	public void logout() {
		if (SecurityUtils.getSubject() != null) {
			SecurityUtils.getSubject().logout();
		}
	}

	@Override
	public boolean unregister(User user) {
		return false;
	}

	@Transactional
	@Override
	public User edit(User user) {
		User userOrign = getUserDao().get("from User u where u.username = ?", new String[] { user.getUsername() });
		userOrign.update(user);
		getUserDao().update(userOrign);
		return userOrign;
	}

}
