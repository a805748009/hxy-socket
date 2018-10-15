package nafos.network.bootStrap.netty.handle.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import nafos.network.bootStrap.netty.handle.currency.Crc32MessageHandle;
import nafos.network.bootStrap.netty.handle.currency.ZlibMessageHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName:MyWebSocketServerHandler Function: socket 将byteBuf解析成byte【】传递
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
@Component
@ChannelHandler.Sharable
public  class ByteArrayOutboundHandle extends ChannelOutboundHandlerAdapter{
    @Autowired
    ZlibMessageHandle zlibMessageHandle;
    @Autowired
    Crc32MessageHandle crc32MessageHandle;

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg instanceof byte[]){
            byte[] contentBytes = (byte[]) msg;

            contentBytes = crc32MessageHandle.addCrc32IntBefore(contentBytes);//CRC32校验
            contentBytes = zlibMessageHandle.zlibByteMessage(contentBytes);//压缩

            ctx.write(Unpooled.wrappedBuffer(contentBytes), promise);
        }else {
            super.write(ctx,msg,promise);
        }
    }
}

