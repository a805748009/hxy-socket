package com.business.bean;

public class PayOrder {
    private String orderId;

    private String userId;

    private String payType;

    private Integer payStatus;

    private Integer shopId;

    private String createTime;

    public PayOrder(String orderId, String userId, String payType, Integer payStatus, Integer shopId, String createTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.payType = payType;
        this.payStatus = payStatus;
        this.shopId = shopId;
        this.createTime = createTime;
    }

    public PayOrder() {
        super();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }
}