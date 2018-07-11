package com.business.service.impl;

import com.business.bean.PayOrder;
import com.business.dao.PayOrderMapper;
import com.business.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author 黄新宇
 * @Date 2018/7/10 下午12:46
 * @Description TODO
 **/
public class OrderServiceImpl implements OrderService{
    @Autowired
    PayOrderMapper payOrderMapper;

    @Override
    public int insertOrder(PayOrder order) {
        return payOrderMapper.insert(order);
    }

    @Override
    public PayOrder getOrderById(String orderId) {
        return payOrderMapper.selectByPrimaryKey(orderId);
    }
}
