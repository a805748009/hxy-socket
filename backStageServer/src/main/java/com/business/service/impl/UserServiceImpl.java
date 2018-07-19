package com.business.service.impl;

import com.business.dao.AdminDao;
import com.business.service.UserService;
import com.hxy.nettygo.result.base.entry.backStageBean.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    AdminDao adminDao;

    @Override
    public Admin selectUserById(String userId) {
        return adminDao.selectLoginById(userId);
    }

    @Override
    public void AddUser(Admin user) {

    }
}
