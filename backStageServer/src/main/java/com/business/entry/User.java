package com.business.entry;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = -5923092790304682686L;
    private String userId;
    private String userName;
    private String backType;
    private String userPass;
    private String createTime;

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", backType='" + backType + '\'' +
                ", userPass='" + userPass + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public User() {
        super();
    }

    public User(String userId, String userName, String backType, String userPass, String createTime) {
        this.userId = userId;
        this.userName = userName;
        this.backType = backType;
        this.userPass = userPass;
        this.createTime = createTime;
    }
}
