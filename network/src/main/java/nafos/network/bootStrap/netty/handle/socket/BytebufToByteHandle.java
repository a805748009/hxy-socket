package nafos.network.bootStrap.netty.handle.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * ClassName:MyWebSocketServerHandler Function: socket 将byteBuf解析成byte【】传递
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
@Component
@ChannelHandler.Sharable
public class BytebufToByteHandle extends SimpleChannelInboundHandler<ByteBuf> {


    /**
     * 接收客户端发送的消息 channel 通道 Read 读
     * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf buf) throws Exception {
        byte[] contentBytes = new byte[buf.readableBytes()];
        buf.readBytes(contentBytes);
        context.fireChannelRead(contentBytes);
    }



}

