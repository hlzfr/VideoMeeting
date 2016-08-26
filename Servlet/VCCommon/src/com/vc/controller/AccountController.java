package com.vc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vc.entity.Result;
import com.vc.entity.account.User;
import com.vc.service.AccountService;
import com.vc.utils.TextUtil;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

	@Autowired
	AccountService accountService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Result<User> login(String username, String password)
			throws IOException {
		// response.setHeader("resetCookie", "true");
		if (TextUtil.isEmpty(username) || TextUtil.isEmpty(password)) {
			return new Result<User>(false, "用户名或密码为空",
					null);
		}
		Result<User> result;
		try {
			User returnUser = accountService.login(username, password);
			if (returnUser != null) {
				// response.setHeader("resetCookie", "true");
				result = new Result<User>(true, null, returnUser);
			} else {
				result = new Result<User>(false, "登录失败.", null);
			}
		} catch (IncorrectCredentialsException e) {
			result = new Result<User>(false, "帐号密码错误", null);
		} catch (UnknownAccountException e1) {
			result = new Result<User>(false, "帐号密码错误", null);
		}
		return result;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public Result<Void> logout(HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("clearCookie", "true");
		accountService.logout();
		return new Result<Void>(true, null, null);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public Result<User> register(String username, String phoneNumber, String password) {
//		System.out.println("！！！！！！！！！！！！user "+user.toString()+"; pass:"+password);
//		user.setPassword(password);
		if (TextUtil.isEmpty(username) || TextUtil.isEmpty(password)) {
			return new Result<User>(false, "用户名或密码为空",
					null);
		}
		User user = new User();
		user.setPassword(password);
		user.setUsername(username);
		user.setPhoneNumber(phoneNumber);
		User registerUser = accountService
				.register(user);
		if (registerUser == null) {
			return new Result<User>(false, "用户已存在", null);
		}
		return new Result<User>(true, null, registerUser);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result<User> update(@RequestBody User user) {
		try {
			if (TextUtil.isEmpty(user.getId())) {
				return new Result<User>(false, "用户不存在", user);
			}
			user = accountService.edit(user);
			return new Result<User>(true, "", user);
		} catch(Exception e) {
			return new Result<User>(false, e.getMessage(), user);
		}
	}
}
