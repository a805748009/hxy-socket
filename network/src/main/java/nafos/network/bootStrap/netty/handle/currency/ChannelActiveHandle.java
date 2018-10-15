package nafos.network.bootStrap.netty.handle.currency;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nafos.core.entry.ClassAndMethod;
import nafos.core.mode.InitMothods;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName:MyWebSocketServerHandler Function: TODO ADD FUNCTION. Date:
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
public class ChannelActiveHandle extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ChannelActiveHandle.class);
    int activeCount = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (activeCount != 0) {
            // 3S一字节的心跳，收不到就干掉
            ctx.close();
            activeCount = 0;
        }
        activeCount++;
    }


    /**
     * channel 通道 action 活跃的
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有人接入，{}",ctx.channel().toString());

        ClassAndMethod route = InitMothods.getSocketConnectFilter();
        if (ObjectUtil.isNotNull(route)) {
            try {
                route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),
                        route.getIndex(), new Object[] { ctx.channel()});
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * channel 通道 Inactive 不活跃的
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有人断开，{}",ctx.channel().toString());

        ClassAndMethod route = InitMothods.getSocketDisConnectFilter();
        if (ObjectUtil.isNotNull(route)) {
            try {
                route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),
                        route.getIndex(), new Object[] { ctx.channel()});
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }








}

