package com.business.bean;

public class Chat {
    private String toUserId;

    private String fromUserId;

    private String msgId;

    private Integer chatType;

    private String groupId;

    private String createTime;

    public Chat(String toUserId, String fromUserId, String msgId, Integer chatType, String groupId, String createTime) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.msgId = msgId;
        this.chatType = chatType;
        this.groupId = groupId;
        this.createTime = createTime;
    }

    public Chat() {
        super();
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId == null ? null : toUserId.trim();
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId == null ? null : fromUserId.trim();
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId == null ? null : msgId.trim();
    }

    public Integer getChatType() {
        return chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }
}