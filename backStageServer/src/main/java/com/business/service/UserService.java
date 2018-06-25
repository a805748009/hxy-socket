package com.business.service;

import Entry.User;

public interface UserService {
    User selectUserById(String userId);

    void AddUser(User user);
}
