package com.business.dao;


import com.business.bean.User;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
	
	boolean addUser(User user);

	User findUserByOpenId(String openId);
}
