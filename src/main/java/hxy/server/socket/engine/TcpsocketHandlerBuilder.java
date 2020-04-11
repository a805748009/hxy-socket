package hxy.server.socket.engine;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @ClassName TcpsocketInitHandler
 * @Description 启动适配
 * @Author hxy
 * @Date 2020/4/8 17:15
 */
public class TcpsocketHandlerBuilder implements SocketHandlerBuilder {

    @Override
    public void buildChannelPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("lengthEncode", new LengthFieldPrepender(4, false));
        pipeline.addLast("lengthDecoder", new LengthFieldBasedFrameDecoder(2000, 0, 4, 0, 4));
        pipeline.addLast(MsgOutboundHandler.getInstance());
        pipeline.addLast(new TcpSocketServerHandler());
    }

}
