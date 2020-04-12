package hxy.server.socket.engine;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

public class DefaultTextTcpSocketServerHandler extends AbstractTcpSocketServerHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        String text = msg.toString(CharsetUtil.UTF_8);
        doHandler(() -> socketMsgHandler.onMessage(ctx, text), ctx);
    }

    public final static DefaultTextTcpSocketServerHandler INSTANCE = new DefaultTextTcpSocketServerHandler();
}