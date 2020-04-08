package hxy.server.socket.engine;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface WebSocketHandler {

    void onConnect(ChannelHandlerContext ctx, HttpRequest req);

    void onMessage(ChannelHandlerContext ctx, WebSocketFrame frame);

    void disConnect(ChannelHandlerContext ctx);

}
