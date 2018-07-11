package com.business.service;

import com.business.bean.PayOrder;
import com.business.bean.Shop;

import java.util.Map;

public interface MyPayService {
    //统一下单
    Map<String, Object> unifiedOrder(String orderId, Shop shop);
}
