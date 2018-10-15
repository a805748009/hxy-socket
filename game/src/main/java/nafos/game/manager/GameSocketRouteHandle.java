package nafos.game.manager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SpringApplicationContextHolder;
import nafos.network.bootStrap.netty.handle.socket.AbstractSocketRouteHandle;
import org.springframework.stereotype.Service;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午12:45
 * @Description http 选择路由
 **/
@Service("GameSocketRouteHandle")
public  class GameSocketRouteHandle extends AbstractSocketRouteHandle {

    @Override
    public void invokeRoute(ChannelHandlerContext ctx, SocketRouteClassAndMethod route, Object obj, byte[] idByte) {
        Object client = ctx.channel().attr(AttributeKey.valueOf("client")).get();
        route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
                new Object[]{ObjectUtil.isNull(client)?ctx.channel():client,obj,idByte});
    }
}
