package nafos.bootStrap.handle.socket;

import io.netty.channel.ChannelHandlerContext;
import nafos.core.Enums.Protocol;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.monitor.RunWatch;
import nafos.core.util.JsonUtil;
import nafos.core.util.ProtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午12:45
 * @Description http 选择路由
 **/
public abstract class AbstractSocketRouteHandle {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocketRouteHandle.class);


    public void route(ChannelHandlerContext ctx, SocketRouteClassAndMethod route, byte[] body, byte[] idByte) {
        Object obj = null;
        if (route.getType() == Protocol.JSON) {
            try {
                String jsonStr = new String(body, "UTF-8");
                if (Map.class.isAssignableFrom(route.getParamType())) {
                    obj = JsonUtil.jsonToMap(jsonStr);
                } else {
                    obj = JsonUtil.json2Object(jsonStr, route.getParamType());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            obj = ProtoUtil.deserializeFromByte(body, route.getParamType());
        }

        String methodName = route.getClazz().getName() + "." + route.getMethod().getMethodNames()[route.getIndex()];
        RunWatch runWatch = RunWatch.init(methodName);

        this.invokeRoute(ctx, route, obj, idByte);

        if (route.isPrintLog()) {
            logger.info("方法：" + methodName + "       程序耗时：" + runWatch.stop() + "ms");
        } else {
            runWatch.stop();
        }
    }


    public abstract void invokeRoute(ChannelHandlerContext ctx, SocketRouteClassAndMethod route, Object obj, byte[] idByte);

}
