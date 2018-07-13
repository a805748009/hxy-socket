package com.business.dao;

import com.business.bean.Shop;

import java.util.List;

public interface ShopMapper {
    int deleteByPrimaryKey(Integer shopId);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Integer shopId);

    List<Shop> selectList();

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);
}