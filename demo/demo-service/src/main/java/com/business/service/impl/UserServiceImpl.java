package com.business.service.impl;

import com.business.dao.*;
import com.business.entry.bean.User;
import com.business.service.*;
import nafos.core.util.DateUtil;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月13日 上午10:49:10 
* 类说明 
*/
@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	 
	@Autowired
    UserMapper userMapper ;






	@Override
	public User getUserByOpenId(String openId) {
		return userMapper.getUserByOpenId(openId);
	}



}
