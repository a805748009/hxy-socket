package com.business.dao;


import com.business.bean.User;

public interface UserDao {
	
	boolean addUser(User user);

	User findUserByOpenId(String openId);
}
