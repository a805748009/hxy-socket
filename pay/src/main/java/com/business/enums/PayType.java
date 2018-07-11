package com.business.enums;

public enum PayType {

    IOS("IOS","苹果支付"),
    WX("WX","微信支付"),
    ALI("ALI","阿里支付");

    private String type;

    private String name;

    PayType() {
    }

    PayType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
