package com.vc.service.impl;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vc.dao.BaseDAO;
import com.vc.entity.account.Role;
import com.vc.entity.account.User;
import com.vc.utils.MySimpleByteSource;

@Service
public class ShiroService extends AuthorizingRealm implements
		com.vc.service.ShiroService {

	// @Autowired
	// UserService userService;

	private SessionDAO sessionDAO;

	BaseDAO<User> userDao;

	public BaseDAO<User> getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(BaseDAO<User> userDao) {
		this.userDao = userDao;
	}

	// public UserService getUserService() {
	// return userService;
	// }
	//
	// @Autowired(required=true)
	// public void setUserService(UserService userService) {
	// this.userService = userService;
	// }

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}

	@Autowired
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	/**
	 * 为当前登录的subject授予角色和权限
	 */
	@Transactional
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		System.out.println("--------------doGetAuthorizationInfo------------");
		String username = (String) super.getAvailablePrincipal(principals);
		System.out
				.println("--------------doGetAuthorizationInfo------------username:"
						+ username);
		// User user = userService.getByUserName(username);
		User user = userDao.get("from User u where u.username = ?",
				new String[] { username });
		if (null != user) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(
					user.getRoleName());
			if (user.getRoleList() != null) {
				for (Role role : user.getRoleList()) {
					info.addStringPermissions(role.getPermissionName());
				}
			}
			return info;
		} else {
			// throw new AuthorizationException();
			return null;
		}
	}

	/**
	 * 验证当前登录的subject
	 */
	@Transactional
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		System.out.println("--------------doGetAuthenticationInfo------------");
		String username = (String) token.getPrincipal();
		System.out
				.println("--------------doGetAuthenticationInfo------------username:"
						+ username);
		// User user = userService.getByUserName(username);
		// FIXME null exception by userDAO
		User user = userDao.get("from User u where u.username = ?",
				new String[] { username });
		if (user == null) {
			throw new UnknownAccountException(); // 没找到帐号
		}

		// if(Boolean.TRUE.equals(user.getLocked())) {
		// throw new LockedAccountException(); //帐号锁定
		// }
		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得不好可以在此判断或自定义实现
		// 通过在配置文件汇总进行如下配置进行密码匹配
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				user.getUsername(), // 用户名
				user.getPassword(), // 密码
				// ByteSource.Util.bytes(user.getCredentialsSalt()),//
				// salt=username+salt
				new MySimpleByteSource(user.getCredentialsSalt()), getName() // realm
																				// name
		);
		return authenticationInfo;
	}

	@Override
	protected void assertCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) throws AuthenticationException {
		// 如果验证出错，super会抛出异常
		super.assertCredentialsMatch(token, info);
		// 验证通过,走下面,删除旧的subject,不删好像也没事
		// 删除其他设备上的这个用户的session
		// 人多了效率有点危险
		String username = (String) token.getPrincipal();
		if (token == null || username == null)
			return;
		if (SecurityUtils.getSubject() != null) {
			SecurityUtils.getSubject().logout();
			Collection<Session> sessions = sessionDAO.getActiveSessions();
			for (Session session : sessions) {
				if (username.equals(session.getAttribute("username"))) {
					session.stop();
				}
			}
		}
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 * 
	 * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
	 */
	private void setSession(Object key, Object value) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			System.out
					.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}
}
