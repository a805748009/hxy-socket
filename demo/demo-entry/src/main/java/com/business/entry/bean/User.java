package com.business.entry.bean;

import nafos.game.entry.BaseUser;

/**
 * @Author 黄新宇
 * @Date 2018/5/14 下午5:46
 * @Description TODO
 **/
public class User implements BaseUser{


    private String  userId;

    private String  accountId;//外显ID

    private String userName;

    private int sex;

    private String userImg; 	//用户头像

    private String openId;

    private String phone;	//手机号码

    private String introduct;   //个人说明

    private int regiestType;//注册方式

    private String createTime;

    private String macId;//机器Id

    private String lastLoginTime;//最后登录时间

    private Integer fashionId;

    public User() {
    }

    public User(String userId, String accountId, String userName, int sex, String userImg, String openId, String phone, String introduct, int regiestType, String createTime, String macId, String lastLoginTime, Integer fashionId) {
        this.userId = userId;
        this.accountId = accountId;
        this.userName = userName;
        this.sex = sex;
        this.userImg = userImg;
        this.openId = openId;
        this.phone = phone;
        this.introduct = introduct;
        this.regiestType = regiestType;
        this.createTime = createTime;
        this.macId = macId;
        this.lastLoginTime = lastLoginTime;
        this.fashionId = fashionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIntroduct() {
        return introduct;
    }

    public void setIntroduct(String introduct) {
        this.introduct = introduct;
    }

    public int getRegiestType() {
        return regiestType;
    }

    public void setRegiestType(int regiestType) {
        this.regiestType = regiestType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getFashionId() {
        return fashionId;
    }

    public void setFashionId(Integer fashionId) {
        this.fashionId = fashionId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", userName='" + userName + '\'' +
                ", sex=" + sex +
                ", userImg='" + userImg + '\'' +
                ", openId='" + openId + '\'' +
                ", phone='" + phone + '\'' +
                ", introduct='" + introduct + '\'' +
                ", regiestType=" + regiestType +
                ", createTime='" + createTime + '\'' +
                ", macId='" + macId + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                '}';
    }
}
