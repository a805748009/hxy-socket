package com.hxy.nettygo.result.base.entry.backStageBean;

import com.hxy.nettygo.result.base.tools.StringUtil;

import java.io.Serializable;
import java.util.List;

public class LoginTimeList extends BaseMqMessage implements Serializable {

    private static final long serialVersionUID = 3445764552286357593L;
    List<LoginTime> loginTime;

    public LoginTimeList(List<LoginTime> loginTime) {
        this.loginTime = loginTime;
    }

    public LoginTimeList(String uri, List<LoginTime> loginTime) {
        super(uri);
        this.loginTime = loginTime;
    }

    public List<LoginTime> getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(List<LoginTime> loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "LoginTimeList{" +
                "loginTime=" + StringUtil.arrayToString(loginTime) +
                '}';
    }
}
