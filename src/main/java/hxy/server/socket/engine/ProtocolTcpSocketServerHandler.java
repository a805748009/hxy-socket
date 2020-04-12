package hxy.server.socket.engine;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ProtocolTcpSocketServerHandler extends AbstractTcpSocketServerHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        byte[] contentBytes = new byte[msg.readableBytes()];
        msg.readBytes(contentBytes);
        doHandler(() -> socketMsgHandler.onMessage(ctx, contentBytes), ctx);
    }
    public final static ProtocolTcpSocketServerHandler INSTANCE = new ProtocolTcpSocketServerHandler();
}