package com.result.base.entry.backStageBean;

import java.io.Serializable;

public class JVMmemory extends BaseMqMessage implements Serializable{
    private static final long serialVersionUID = -6169524110413403859L;
    protected String memoryId;
    protected String number;
    protected String gameName;
    protected String createTime;

    @Override
    public String toString() {
        return "JVMmemory{" +
                "memoryId='" + memoryId + '\'' +
                ", number='" + number + '\'' +
                ", gameName='" + gameName + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public JVMmemory(String memoryId, String number, String gameName, String createTime) {
        this.memoryId = memoryId;
        this.number = number;
        this.gameName = gameName;
        this.createTime = createTime;
    }

    public JVMmemory(String uri, String memoryId, String number, String gameName, String createTime) {
        super(uri);
        this.memoryId = memoryId;
        this.number = number;
        this.gameName = gameName;
        this.createTime = createTime;
    }

    public JVMmemory() {
        super();
    }

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
