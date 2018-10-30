package nafos.network.bootStrap.netty.handle.currency;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author 黄新宇
 * @Date 2018/10/29 下午4:28
 * @Description TODO
 **/
@ChannelHandler.Sharable
public class HttpLimitingHandle extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HttpLimitingHandle.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("http====="+ctx.channel().id());
    }

}
