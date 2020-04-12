package hxy.server.socket.engine;

import hxy.server.socket.engine.factory.ChannelHandlerInitializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.*;

public class WebSocketServerHandler extends AbstractSocketServerHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    private final static SocketMsgHandler socketMsgHandler = ChannelHandlerInitializer.getSocketMsgHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            handleHttpRequest(ctx, (HttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }


    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
        if (isWebSocketRequest(req)) {
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(req.uri(), null, true);
            this.handshaker = wsFactory.newHandshaker(req);
            if (this.handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                this.handshaker.handshake(ctx.channel(), req);
                doHandler(() -> socketMsgHandler.onConnect(ctx, req), ctx);
            }
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            this.handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            doHandler(() -> socketMsgHandler.disConnect(ctx), ctx);
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        String msg = ((TextWebSocketFrame) frame).text();
        doHandler(() -> socketMsgHandler.onMessage(ctx, msg), ctx);
    }


    private boolean isWebSocketRequest(HttpRequest req) {
        return req != null
                && req.decoderResult().isSuccess()
                && "websocket".equals(req.headers().get("Upgrade"));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

}