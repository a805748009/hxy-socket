package com.hxy.nettygo.result.base.entry.backStageBean;

import java.io.Serializable;

public class ActiveCount extends BaseMqMessage implements Serializable{
    private static final long serialVersionUID = 440887509982662798L;
    protected String activeId;
    protected Integer activeCount;
    protected String createTime;
    protected String gameName;

    @Override
    public String toString() {
        return "ActiveCount{" +
                "activeId='" + activeId + '\'' +
                ", avticeCount='" + activeCount + '\'' +
                ", createTime='" + createTime + '\'' +
                ", gameName='" + gameName + '\'' +
                '}';
    }

    public ActiveCount() {
    }

    public ActiveCount(String activeId, Integer activeCount, String createTime, String gameName) {
        this.activeId = activeId;
        this.activeCount = activeCount;
        this.createTime = createTime;
        this.gameName = gameName;
    }

    public ActiveCount(String uri, String activeId, Integer activeCount, String createTime, String gameName) {
        super(uri);
        this.activeId = activeId;
        this.activeCount = activeCount;
        this.createTime = createTime;
        this.gameName = gameName;
    }

    public ActiveCount(String uri) {
        super(uri);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer ActiveCount) {
        this.activeCount = ActiveCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
