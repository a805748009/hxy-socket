package hxy.server.socket.engine;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public abstract class AbstractTcpSocketServerHandler extends AbstractSocketServerHandler<ByteBuf> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        doHandler(() -> socketMsgHandler.onConnect(ctx, null), ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        doHandler(() -> socketMsgHandler.disConnect(ctx), ctx);
    }

}