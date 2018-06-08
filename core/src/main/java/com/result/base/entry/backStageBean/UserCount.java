package com.result.base.entry.backStageBean;

import java.io.Serializable;

public class UserCount extends BaseMqMessage implements Serializable{
    private static final long serialVersionUID = 2990357964800501171L;
    protected String id;
    protected int userCount;
    protected String gameName;
    protected String updateTime;

    @Override
    public String toString() {
        return "UserCount{" +
                "id='" + id + '\'' +
                ", userCount=" + userCount +
                ", gameName='" + gameName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public UserCount() {
        super();
    }

    public UserCount(String id, int userCount, String gameName, String updateTime) {
        this.id = id;
        this.userCount = userCount;
        this.gameName = gameName;
        this.updateTime = updateTime;
    }

    public UserCount(String uri, String id, int userCount, String gameName, String updateTime) {
        super(uri);
        this.id = id;
        this.userCount = userCount;
        this.gameName = gameName;
        this.updateTime = updateTime;
    }
}
