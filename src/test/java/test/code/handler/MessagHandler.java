package test.code.handler;

import hxy.server.socket.anno.CodeHandler;
import hxy.server.socket.anno.Handle;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/11 16:17
 */
@CodeHandler
public class MessagHandler {

    @Handle(1024)
    public void helloSocket(Channel channel, int clientCode, Map map){
        System.out.println(clientCode);
        System.out.println(channel);
        System.out.println(map);
    }
}
