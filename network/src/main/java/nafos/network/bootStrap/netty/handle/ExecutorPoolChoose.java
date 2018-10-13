package nafos.network.bootStrap.netty.handle;

import io.netty.channel.ChannelHandlerContext;

public interface ExecutorPoolChoose {

     void choosePool(ChannelHandlerContext ctx, Object msg);
}
