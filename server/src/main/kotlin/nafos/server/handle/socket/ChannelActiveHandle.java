package nafos.server.handle.socket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 *@Description 心跳-连接-断开连接 处理
 *@Author xinyu.huang
 *@Time 2019/11/30 23:38
 */
@ChannelHandler.Sharable
public class ChannelActiveHandle extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ChannelActiveHandle.class);

    private static ChannelActiveHandle channelActiveHandle = new ChannelActiveHandle();

    public static ChannelActiveHandle getInstance() {
        return channelActiveHandle;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.debug("心跳检测", ctx.channel().toString());
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    /**
     * channel 通道 action 活跃的
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.debug("有人接入，{}", ctx.channel().toString());
    }


    /**
     * channel 通道 Inactive 不活跃的
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.debug("有人断开，{}", ctx.channel().toString());
    }

    @Override
    public void  exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
        logger.warn(cause.toString());
        ctx.channel().disconnect();
    }

}

