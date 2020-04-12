package hxy.server.socket.engine.factory;

import hxy.server.socket.configuration.SocketConfiguration;
import hxy.server.socket.engine.DefaultTextWebSocketServerHandler;
import hxy.server.socket.engine.MsgOutboundHandler;
import hxy.server.socket.engine.ProtocolWebSocketServerHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName WebsocketInitHandler
 * @Description 启动适配
 * @Author hxy
 * @Date 2020/4/8 17:15
 */
public class WebsocketHandlerBuilder implements SocketHandlerBuilder {
    @Autowired
    private SocketConfiguration socketConfiguration;

    @Override
    public void buildChannelPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(MsgOutboundHandler.INSTANCE);
        if(socketConfiguration.getProtocolType() == SocketConfiguration.ProtocolType.TEXT){
            pipeline.addLast(new DefaultTextWebSocketServerHandler());
        }else{
            pipeline.addLast(new ProtocolWebSocketServerHandler());
        }
    }

}
