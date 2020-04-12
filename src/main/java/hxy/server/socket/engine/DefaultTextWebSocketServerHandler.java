package hxy.server.socket.engine;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class DefaultTextWebSocketServerHandler extends AbstractWebSocketServerHandler<Object> {

    @Override
    protected void doMessage(ChannelHandlerContext ctx, WebSocketFrame frame) {
        String msg = ((TextWebSocketFrame) frame).text();
        doHandler(() -> socketMsgHandler.onMessage(ctx, msg), ctx);
    }
}
