package com.vc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.vc.entity.Page;
import com.vc.entity.Result;
import com.vc.entity.account.User;
import com.vc.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "page/{page}/{limit}", method = RequestMethod.POST)
	@ResponseBody
	public Result<Page<User>> getPage(@PathVariable int page,
			@PathVariable int limit, @RequestBody List<User> notInclude) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			// 需要手动转换，否则报出异常 LinkedHashMap cannot be cast to
			notInclude = mapper.readValue(mapper.writeValueAsString(notInclude), new TypeReference<List<User>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Page<User> result = null;
		if (notInclude != null && notInclude.size() > 0) {
			String where = "as user where ";
			List<String> ids = new ArrayList<String>(notInclude.size());
			int count = notInclude.size();
			for(int i = 0; i < count; i++) {
				if (i > 0) {
					where += " and ";
				}
				ids.add(notInclude.get(i).getId());
				where += "user.id!=?";
			}
			result = userService.getUserPage(page, limit, where, ids.toArray());
		} else {
			result = userService.getUserPage(page, limit, null, null);
		}
		return new Result<Page<User>>(true,"", result);
	}

	// @RequiresUser
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public User test(HttpServletRequest request, HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		User user = new User();
		if (subject == null) {
			user.setUsername("subject == null");
		} else {
			user.setUsername("subject.isRemembered()" + subject.isRemembered()
					+ ";subject.isAuthenticated() " + subject.isAuthenticated());
		}
		return user;
	}

	@RequestMapping(value = "/test2", method = RequestMethod.POST)
	@ResponseBody
	public User test2(@RequestBody User user) {
		return user;
	}

	@RequestMapping(value = "/test3", method = RequestMethod.GET)
	@ResponseStatus(reason = "未授权", value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public void test3(HttpServletRequest request, HttpServletResponse response) {
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public User register(String username, String password) {
		return getUserService().register(username, password);
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	@ResponseBody
	public User register() {
		return getUserService().register("jianbin3", "6965790");
	}

	@RequestMapping(value = "/update/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public User update(@PathVariable String userid) {
		User user = new User();
		user.setId(userid);
		user.setUsername(System.currentTimeMillis() + "");
		return getUserService().update(user);
	}
}
