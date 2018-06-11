package com.business.controller;

import com.business.entry.User;
import com.business.entry.UserMsg;
import com.result.base.annotation.BCRemoteCall;
import com.result.base.annotation.Nuri;
import com.result.base.annotation.On;
import com.result.base.annotation.Route;
import com.result.base.cache.Client;
import com.result.base.cache.IoCache;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/5/2 下午7:24
 * @Description TODO
 **/
@Route
public class Test {

    @On("test")
    public void test(Client client, UserMsg user, byte[] id) {
        client.sendMsg(user,id);
    }

    @On("time")
    public void time(Client client, User user, String id) {

//        client.joinRoom(CastUtil.castString(SnowflakeIdWorker.getSnowflakeIdWorker().nextId()));
//        client.roomBroadcast(user,"test");
//        client.nameSpaceBroadcast(user,"test");
        System.out.println("当前连接数========"+ IoCache.spaceClientMap.get(client.getNameSpace()).size());
//        client.sendMsg(user,id);
//        client.sendMsg(SerializationUtil.serializeToByte(user),id);
//        client.sendMsg(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(SerializationUtil.serializeToByte(user))));
    }

    @On("number")
    public void number(Client client, String text, String id) {
        client.sendMsg(new TextWebSocketFrame(String.valueOf(IoCache.spaceClientMap.get("websocket").size())));
    }

}
