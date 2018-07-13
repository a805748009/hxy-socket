package com.mode.init;

import com.hxy.nettygo.result.NettyGoConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/5/3 下午6:40
 * @Description TODO
 **/
public class RouteMapInit {

    public RouteMapInit(){
        Map<Integer,String> map = new HashMap<>();
        map.put(10000,"test");
        NettyGoConstant.setSocketRouteMap(map);
    }
}
