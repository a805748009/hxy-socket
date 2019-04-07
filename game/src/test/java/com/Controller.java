//package com;
//
//import io.netty.channel.Channel;
//import nafos.core.annotation.controller.Handle;
//import nafos.game.manager.ChannelConnectInitialize;
//import nafos.game.relation.Client;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Map;
//
///**
// * @Author 黄新宇
// * @Date 2018/10/11 下午4:25
// * @Description TODO
// **/
//@nafos.core.annotation.controller.Controller
//public class Controller {
//    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
//
//    @Autowired
//    ChannelConnectInitialize channelConnectInitialize;
//
//    @Handle(code = 1000,type="JSON")
//    public void h(Channel channel, Map map,byte[] id){
//        logger.info(map.toString());
//        channel.writeAndFlush("121".getBytes());
//        User u = new User();
//        u.setUserId("12112");
//        channelConnectInitialize.initChannel(channel,"12","game",u);
//    }
//
//    @Handle(code = 1001,type="JSON")
//    public void hc(Client client, Map map, byte[] id){
//        logger.info(map.toString());
//        client.sendMsg("dad".getBytes(),id);
//    }
//}
