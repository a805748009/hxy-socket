package com.business.service.impl;

import Entry.User;
import com.business.dao.UserDao;
import com.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User selectUserById(String userId) {
        return userDao.loginById(userId);
    }

    @Override
    public void AddUser(User user) {

    }
}
