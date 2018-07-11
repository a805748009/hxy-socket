package com.business.bean;

public class IosReceiptMd5 {
    private String receiptData;

    private String orderId;

    public IosReceiptMd5(String receiptData, String orderId) {
        this.receiptData = receiptData;
        this.orderId = orderId;
    }

    public IosReceiptMd5() {
        super();
    }

    public String getReceiptData() {
        return receiptData;
    }

    public void setReceiptData(String receiptData) {
        this.receiptData = receiptData == null ? null : receiptData.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }
}