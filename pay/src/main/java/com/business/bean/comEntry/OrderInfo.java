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

    private String gameName;

    private String payType;

    private String orderId;

    private String iosPayId;

    private Map<String,Object> payInfoMap;

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

    public Map<String, Object> getPayInfoMap() {
        return payInfoMap;
    }

    public void setPayInfoMap(Map<String, Object> payInfoMap) {
        this.payInfoMap = payInfoMap;
    }
}
