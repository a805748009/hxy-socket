package nafos.bootStrap.handle.socket;

import io.netty.channel.ChannelHandlerContext;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.helper.SpringApplicationContextHolder;
import org.springframework.stereotype.Service;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午12:45
 * @Description http 选择路由
 **/
@Service("SocketRouteHandle")
public class SocketRouteHandle extends AbstractSocketRouteHandle {

    @Override
    public void invokeRoute(ChannelHandlerContext ctx, SocketRouteClassAndMethod route, Object obj, byte[] idByte) {
        route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()), route.getIndex(),
                new Object[]{ctx.channel(), obj, idByte});
    }
}
