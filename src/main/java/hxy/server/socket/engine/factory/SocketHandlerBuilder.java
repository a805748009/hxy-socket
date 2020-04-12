package hxy.server.socket.engine.factory;

import io.netty.channel.ChannelPipeline;

public interface SocketHandlerBuilder {
    void buildChannelPipeline(ChannelPipeline pipeline);
}
