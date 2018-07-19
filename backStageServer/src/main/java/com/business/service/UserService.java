package com.business.service;

import com.hxy.nettygo.result.base.entry.backStageBean.Admin;

public interface UserService {
    Admin selectUserById(String userId);

    void AddUser(Admin user);
}
