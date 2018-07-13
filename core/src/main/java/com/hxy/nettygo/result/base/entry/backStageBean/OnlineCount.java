package com.hxy.nettygo.result.base.entry.backStageBean;

import java.io.Serializable;

public class OnlineCount extends BaseMqMessage implements Serializable{
    private static final long serialVersionUID = -8558944158471877977L;
    protected String onlineId;
    protected int onlineCount;
    protected String gameName;
    protected String updateTime;

    @Override
    public String toString() {
        return "OnlineCount{" +
                "onlineId='" + onlineId + '\'' +
                ", onlineCount=" + onlineCount +
                ", gameName='" + gameName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public String getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(String onlineId) {
        this.onlineId = onlineId;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
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

    public OnlineCount() {
        super();
    }

    public OnlineCount(String onlineId, int onlineCount, String gameName, String updateTime) {
        this.onlineId = onlineId;
        this.onlineCount = onlineCount;
        this.gameName = gameName;
        this.updateTime = updateTime;
    }
}
