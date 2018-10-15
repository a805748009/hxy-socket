package com;

import nafos.game.entry.BaseUser;

/**
 * @Author 黄新宇
 * @Date 2018/10/15 下午4:31
 * @Description TODO
 **/
public class User implements BaseUser{
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
