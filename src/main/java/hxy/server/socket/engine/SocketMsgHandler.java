package hxy.server.socket.engine;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface SocketMsgHandler {

    void onConnect(ChannelHandlerContext ctx, HttpRequest req);

    void onMessage(ChannelHandlerContext ctx, String msg);

    void disConnect(ChannelHandlerContext ctx);

}
