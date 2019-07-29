package nafos.game.manager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import nafos.bootStrap.handle.socket.AbstractSocketRouteHandle;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午12:45
 * @Description http 选择路由
 **/
@Service("GameSocketRouteHandle")
public  class GameSocketRouteHandle extends AbstractSocketRouteHandle {
    private static final Logger logger = LoggerFactory.getLogger(GameSocketRouteHandle.class);
    @Override
    public void invokeRoute(ChannelHandlerContext ctx, SocketRouteClassAndMethod route, Object obj, byte[] idByte) {
        Object client = ctx.channel().attr(AttributeKey.valueOf("client")).get();
        try{
            route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
                    new Object[]{ObjectUtil.isNull(client)?ctx.channel():client,obj,idByte});
        }catch (ClassCastException e){
            logger.debug("error：channel-》client类型转换出错，可能原因：断线重连导致");
        }

    }
}
