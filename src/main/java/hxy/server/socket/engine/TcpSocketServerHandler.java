package hxy.server.socket.engine;

import hxy.server.socket.engine.factory.ChannelHandlerInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;



public class TcpSocketServerHandler extends AbstractSocketServerHandler<ByteBuf> {

    private static SocketMsgHandler socketMsgHandler = ChannelHandlerInitializer.getSocketMsgHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        String text = msg.toString(CharsetUtil.UTF_8);
        doHandler(() -> socketMsgHandler.onMessage(ctx, text), ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        doHandler(() -> socketMsgHandler.onConnect(ctx, null), ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        doHandler(() -> socketMsgHandler.disConnect(ctx), ctx);
    }

}