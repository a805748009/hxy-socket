package com.business.cache;

import com.business.bean.Shop;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 黄新宇
 * @Date 2018/7/10 下午4:07
 * @Description TODO
 **/
public class ShopCache {

    public static ConcurrentHashMap<Integer,Shop> shopCacheMap = new ConcurrentHashMap<>();
}
