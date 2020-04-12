package hxy.server.socket.engine;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class ProtocolWebSocketServerHandler extends AbstractWebSocketServerHandler<Object> {

    @Override
    protected void doMessage(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof BinaryWebSocketFrame) {
            byte[] contentBytes = new byte[frame.content().readableBytes()];
            frame.content().readBytes(contentBytes);
            doHandler(() -> socketMsgHandler.onMessage(ctx, contentBytes), ctx);
        }
    }
}