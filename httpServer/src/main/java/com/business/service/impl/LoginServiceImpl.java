package com.business.service.impl;

import com.business.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.dao.UserDao;
import com.business.service.LoginService;
import com.result.base.tools.DateUtil;
import com.result.base.tools.SnowflakeIdWorker;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月13日 上午10:49:10 
* 类说明 
*/
@Service
public class LoginServiceImpl implements LoginService{
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	 
	@Autowired  UserDao userDao ;


	
	/**
	 * 第三方第一次登陆
	 */
	public String regiest(User user) {
		String userId = String.valueOf(SnowflakeIdWorker.getSnowflakeIdWorker().nextId());
		user.setUserId(userId);
		if(userDao.addUser(user))
			return userId;
		return null;
	}


}
