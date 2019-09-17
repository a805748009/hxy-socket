package nafos.bootStrap.handle.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.springframework.stereotype.Component;

/**
 * ClassName:MyWebSocketServerHandler Function: socket 将byteBuf解析成byte【】传递
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
@Component
@ChannelHandler.Sharable
public class BytebufToBinaryFrameHandle extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            ctx.write(new BinaryWebSocketFrame((ByteBuf) msg), promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }
}

