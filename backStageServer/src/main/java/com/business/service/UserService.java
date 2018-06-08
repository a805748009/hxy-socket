package com.business.service;

import com.result.base.entry.backStageBean.Admin;

public interface UserService {
    Admin selectUserById(String userId);

    void AddUser(Admin user);
}
