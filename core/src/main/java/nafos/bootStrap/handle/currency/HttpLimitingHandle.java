package nafos.bootStrap.handle.currency;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/10/29 下午4:28
 * @Description TODO
 **/
@Component
@ChannelHandler.Sharable
public class HttpLimitingHandle extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HttpLimitingHandle.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.debug("http=====" + ctx.channel().id());
    }

}
