package com.business.service.impl;

import com.business.dao.UserDao;
import com.business.service.UserService;
import com.result.base.entry.backStageBean.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public Admin selectUserById(String userId) {
        return userDao.loginById(userId);
    }

    @Override
    public void AddUser(Admin user) {

    }
}
