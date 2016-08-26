package com.vc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vc.dao.BaseDAO;
import com.vc.entity.Page;
import com.vc.entity.PageInfo;
import com.vc.entity.Room;
import com.vc.entity.account.User;
import com.vc.service.UserService;
import com.vc.utils.TextUtil;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	private static final int PER_PAGE = 20;
	
	private BaseDAO<User> userDao;
	
	public BaseDAO<User> getUserDao() {
		return userDao;
	}
	
	@Autowired
	public void setUserDao(BaseDAO<User> userDao) {
		this.userDao = userDao;
	}
	
	@Transactional
	@Override
	public User register(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		getUserDao().save(user);
		return user;
	}

	@Override
	public User login(String username, String password) {
		return null;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean unregister(User user) {
		getUserDao().delete(user);
		return true;
	}

	@Override
	public User update(User user) {
		getUserDao().update(user);
		return user;
	}

	@Transactional
	@Override
	public User getByUserName(String name) {
		return getUserDao().get("from User u where u.username=?", new Object[]{name});
	}

	@Transactional
	@Override
	public Page<User> getUserPage(int page, int limit, String where,
			Object[] params) {
		PageInfo pageInfo = new PageInfo();
		String countHql = "select count(*) from User";
		if(!TextUtil.isEmpty(where)) {
			countHql += " " + where;
		}
		long totalDataNum = getUserDao().count(countHql, params);
		
		int perPageDataNum = limit == -1 ? PER_PAGE : limit;

		String findHql = "from User";
		if(!TextUtil.isEmpty(where)) {
			findHql += " " + where;
		}
//		findHql+=" order by updateDate desc";
		List<User> userList = getUserDao().find(findHql, params, page,
				perPageDataNum);
		// 到此页为止(包括此页)一共的数据数量
		// 前一页的数量+这一页查询到的数据量
		long totalDataNumCurPage = (page * perPageDataNum - perPageDataNum)
				+ userList.size();

		boolean hasNext = totalDataNumCurPage < totalDataNum;

		pageInfo.setCurPage(page);
		pageInfo.setHasNext(hasNext);
		pageInfo.setPerPageDataNum(perPageDataNum);
		pageInfo.setTotalDataNum(totalDataNum);
		pageInfo.setTotalDataNumCurPage(totalDataNumCurPage);
		pageInfo.setTotalPageNum((int) Math.ceil((double) totalDataNum
				/ (double) perPageDataNum));
		
		Page<User> userPage = new Page<User>();
		userPage.setData(userList);
		userPage.setPageInfo(pageInfo);

		return userPage;
	}
}
