package test.websocket.bytearray;

import hxy.server.socket.anno.Socket;
import hxy.server.socket.engine.SocketMsgHandler;
import hxy.server.socket.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/8 21:32
 */
@Socket
public class SimpleSocketMsgHandler implements SocketMsgHandler<byte[]> {
    @Override
    public void onConnect(ChannelHandlerContext ctx, HttpRequest req) {
        System.out.println(ctx.channel().toString());
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, byte[] msg) {
        System.out.println("server byte message:"+ ProtoUtil.deserializeFromByte(msg,Person.class));
        ctx.writeAndFlush(msg);
    }

    @Override
    public void disConnect(ChannelHandlerContext ctx) {
        System.out.println("断开连接=" + ctx.channel().toString());
    }
}
