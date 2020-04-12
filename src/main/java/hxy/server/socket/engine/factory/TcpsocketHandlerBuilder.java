package hxy.server.socket.engine.factory;

import hxy.server.socket.configuration.SocketConfiguration;
import hxy.server.socket.engine.DefaultTextTcpSocketServerHandler;
import hxy.server.socket.engine.MsgOutboundHandler;
import hxy.server.socket.engine.ProtocolTcpSocketServerHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName TcpsocketInitHandler
 * @Description 启动适配
 * @Author hxy
 * @Date 2020/4/8 17:15
 */
public class TcpsocketHandlerBuilder implements SocketHandlerBuilder {
    @Autowired
    private SocketConfiguration socketConfiguration;

    @Override
    public void buildChannelPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("lengthEncode", new LengthFieldPrepender(4, false));
        pipeline.addLast("lengthDecoder", new LengthFieldBasedFrameDecoder(2000, 0, 4, 0, 4));
        pipeline.addLast(MsgOutboundHandler.INSTANCE);
        if(socketConfiguration.getProtocolType() == SocketConfiguration.ProtocolType.TEXT){
            pipeline.addLast(DefaultTextTcpSocketServerHandler.INSTANCE);
        }else{
            pipeline.addLast(ProtocolTcpSocketServerHandler.INSTANCE);
        }
    }

}
