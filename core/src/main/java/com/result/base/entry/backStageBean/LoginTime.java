package com.result.base.entry.backStageBean;

import java.io.Serializable;

public class LoginTime extends BaseMqMessage implements Serializable{
    private static final long serialVersionUID = 7855050448056414514L;
    protected String loginId;
    protected String loginUserId;
    protected String loginTime;
    protected String gameName;


    @Override
    public String toString() {
        return "LoginTime{" +
                "loginId='" + loginId + '\'' +
                ", loginUserId='" + loginUserId + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", gameName='" + gameName + '\'' +
                '}';
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public LoginTime() {
        super();
    }

    public LoginTime(String loginId, String loginUserId, String loginTime, String gameName) {
        this.loginId = loginId;
        this.loginUserId = loginUserId;
        this.loginTime = loginTime;
        this.gameName = gameName;
    }
}
