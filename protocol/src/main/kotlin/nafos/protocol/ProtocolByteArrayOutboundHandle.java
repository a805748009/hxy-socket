package nafos.protocol;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * ClassName:MyWebSocketServerHandler Function: socket 将byteBuf解析成byte【】传递
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
@ChannelHandler.Sharable
public class ProtocolByteArrayOutboundHandle extends ChannelOutboundHandlerAdapter {

    private static ProtocolByteArrayOutboundHandle byteArrayOutboundHandle = new ProtocolByteArrayOutboundHandle();

    public static ProtocolByteArrayOutboundHandle getInstance(){
        return byteArrayOutboundHandle;
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof byte[]) {
            byte[] contentBytes = (byte[]) msg;

            contentBytes = Crc32MessageHandle.getInstance().addCrc32IntBefore(contentBytes);//CRC32校验
            contentBytes = ZlibMessageHandle.getInstance().zlibByteMessage(contentBytes);//压缩

            ctx.write(Unpooled.wrappedBuffer(contentBytes), promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }
}

