package hxy.server.socket.engine.factory;

import hxy.server.socket.engine.MsgOutboundHandler;
import hxy.server.socket.engine.WebSocketServerHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

/**
 * @ClassName WebsocketInitHandler
 * @Description 启动适配
 * @Author hxy
 * @Date 2020/4/8 17:15
 */
public class WebsocketHandlerBuilder implements SocketHandlerBuilder {

    @Override
    public void buildChannelPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(MsgOutboundHandler.getInstance());
        pipeline.addLast(new WebSocketServerHandler());
    }

}
