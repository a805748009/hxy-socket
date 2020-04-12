package hxy.server.socket.engine;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public interface SocketMsgHandler<T> {

    void onConnect(ChannelHandlerContext ctx, HttpRequest req);

    void onMessage(ChannelHandlerContext ctx, T msg);

    void disConnect(ChannelHandlerContext ctx);

}
