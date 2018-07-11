package com.business.dao;

import com.business.bean.PayOrder;

public interface PayOrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(PayOrder record);

    int insertSelective(PayOrder record);

    PayOrder selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(PayOrder record);

    int updateByPrimaryKey(PayOrder record);
}