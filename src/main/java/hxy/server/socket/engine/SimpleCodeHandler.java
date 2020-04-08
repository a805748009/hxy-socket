package hxy.server.socket.engine;

import hxy.server.socket.anno.Socket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/8 21:32
 */
@Socket
public class SimpleCodeHandler implements SocketMsgHandler {
    private Logger logger = LoggerFactory.getLogger(SimpleCodeHandler.class);

    @Override
    public void onConnect(ChannelHandlerContext ctx, HttpRequest req) {
        logger.debug("新的连接:{}", ctx.channel().toString());
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {

        System.out.println("收到消息=" + msg);
    }

    @Override
    public void disConnect(ChannelHandlerContext ctx) {
        logger.debug("断开连接:{}", ctx.channel().toString());
    }
}
