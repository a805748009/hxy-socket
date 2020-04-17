package test.websocket.text;

import hxy.server.socket.anno.Socket;
import hxy.server.socket.engine.SocketMsgHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/8 21:32
 */
@Socket
public class SimpleSocketMsgHandler implements SocketMsgHandler<String> {
    @Override
    public void onConnect(ChannelHandlerContext ctx, HttpRequest req) {
        System.out.println(ctx.channel().toString());
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {
        System.out.println("收到消息=" + msg);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void disConnect(ChannelHandlerContext ctx) {
        System.out.println("断开连接=" + ctx.channel().toString());
    }
}
