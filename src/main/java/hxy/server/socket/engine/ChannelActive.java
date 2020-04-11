package hxy.server.socket.engine;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Description channel连接和断开连接处理
 * @Author xinyu.huang
 * @Time 2020/4/11 15:33
 */
public interface ChannelActive {
    void onConnect(ChannelHandlerContext ctx, HttpRequest req);

    void disConnect(ChannelHandlerContext ctx);
}
