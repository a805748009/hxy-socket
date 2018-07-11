package com.business.service;

import com.business.bean.PayOrder;

public interface OrderService {

    int insertOrder(PayOrder order);

    PayOrder getOrderById(String orderId);


}
