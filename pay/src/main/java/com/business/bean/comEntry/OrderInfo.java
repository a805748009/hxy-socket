package com.business.bean.comEntry;


import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/7/10 下午3:45
 * @Description TODO
 **/
public class OrderInfo {

    private int shopId;

    private String userToken;//加密后的userId

    private String gameName;//游戏名称

    private String payType;//支付方式

    private int payStatus;//支付状态

    private String orderId;

    private String iosPayId;//ios上的商品Id

    private String subject;

    private String body;

    private int price;//分为单位

    private String receiptData; //苹果支付传的参数

    private String  aliwxReturnInfo; //微信支付宝预付单返回的消息







    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIosPayId() {
        return iosPayId;
    }

    public void setIosPayId(String iosPayId) {
        this.iosPayId = iosPayId;
    }

    public String getAliwxReturnInfo() {
        return aliwxReturnInfo;
    }

    public void setAliwxReturnInfo(String aliwxReturnInfo) {
        this.aliwxReturnInfo = aliwxReturnInfo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getReceiptData() {
        return receiptData;
    }

    public void setReceiptData(String receiptData) {
        this.receiptData = receiptData;
    }
}
