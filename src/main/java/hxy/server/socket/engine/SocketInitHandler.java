package hxy.server.socket.engine;

import io.netty.channel.ChannelPipeline;

public interface SocketInitHandler {
    void buildChannelPipeline(ChannelPipeline pipeline);
}
