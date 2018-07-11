package com.business.bean;

public class Shop {
    private Integer shopId;

    private String iosPayId;

    private Boolean isRmb;

    private Integer price;

    private Boolean isDiscount;

    private Integer discountPrice;

    private String subject;

    private String body;

    public Shop(Integer shopId, String iosPayId, Boolean isRmb, Integer price, Boolean isDiscount, Integer discountPrice, String subject, String body) {
        this.shopId = shopId;
        this.iosPayId = iosPayId;
        this.isRmb = isRmb;
        this.price = price;
        this.isDiscount = isDiscount;
        this.discountPrice = discountPrice;
        this.subject = subject;
        this.body = body;
    }

    public Shop() {
        super();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getIosPayId() {
        return iosPayId;
    }

    public void setIosPayId(String iosPayId) {
        this.iosPayId = iosPayId == null ? null : iosPayId.trim();
    }

    public Boolean getIsRmb() {
        return isRmb;
    }

    public void setIsRmb(Boolean isRmb) {
        this.isRmb = isRmb;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Boolean isDiscount) {
        this.isDiscount = isDiscount;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }
}