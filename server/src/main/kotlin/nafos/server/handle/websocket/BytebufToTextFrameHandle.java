package nafos.server.handle.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * ClassName:MyWebSocketServerHandler Function: socket 将byteBuf解析成byte【】传递
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
@ChannelHandler.Sharable
public class BytebufToTextFrameHandle extends ChannelOutboundHandlerAdapter {

    private static BytebufToTextFrameHandle bytebufToTextFrameHandle = new BytebufToTextFrameHandle();

    public static BytebufToTextFrameHandle getInstance(){
        return bytebufToTextFrameHandle;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof String) {
            ctx.write(new TextWebSocketFrame((String) msg), promise);
        }else{
            super.write(ctx, msg, promise);
        }
    }
}

