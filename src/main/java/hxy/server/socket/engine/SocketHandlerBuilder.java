package hxy.server.socket.engine;

import io.netty.channel.ChannelPipeline;

public interface SocketHandlerBuilder {
    void buildChannelPipeline(ChannelPipeline pipeline);
}
