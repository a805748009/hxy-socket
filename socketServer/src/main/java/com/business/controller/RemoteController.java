package com.business.controller;

import com.business.entry.User;
import com.result.base.annotation.BCRemoteCall;
import com.result.base.annotation.Nuri;
import com.result.base.annotation.Route;

import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/6/11 下午4:26
 * @Description TODO
 **/
@Route
public class RemoteController {

    @BCRemoteCall
    @Nuri(uri="/test", method = "POST",type="JSON")
    public Object test(Map map) {
        System.out.println("=============收到消息"+map);
        return null;
    }
}
