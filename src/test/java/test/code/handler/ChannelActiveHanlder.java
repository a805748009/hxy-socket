package test.code.handler;

import hxy.server.socket.engine.ChannelActive;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/11 16:15
 */
@Component
public class ChannelActiveHanlder implements ChannelActive {
    @Override
    public void onConnect(ChannelHandlerContext ctx, HttpRequest req) {
        System.out.println("connect:"+ctx);
    }

    @Override
    public void disConnect(ChannelHandlerContext ctx) {
        System.out.println("disConnect:"+ctx);
    }
}
