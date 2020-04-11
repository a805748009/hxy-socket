package hxy.server.socket.engine;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.CompletableFuture;


public class TcpSocketServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static SocketMsgHandler socketMsgHandler = ChannelHandlerInitializer.getSocketMsgHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        String text = msg.toString(CharsetUtil.UTF_8);
        CompletableFuture.runAsync(() -> socketMsgHandler.onMessage(ctx, text), ctx.executor());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        CompletableFuture.runAsync(() -> socketMsgHandler.onConnect(ctx, null), ctx.executor());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        CompletableFuture.runAsync(() -> socketMsgHandler.disConnect(ctx), ctx.executor());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx,cause);
    }

}