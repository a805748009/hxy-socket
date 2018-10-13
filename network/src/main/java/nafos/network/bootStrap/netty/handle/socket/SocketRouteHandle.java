package nafos.network.bootStrap.netty.handle.socket;

import io.netty.channel.ChannelHandlerContext;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.util.FastJson;
import nafos.core.util.GsonUtil;
import nafos.core.util.ProtoUtil;
import nafos.core.util.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午12:45
 * @Description http 选择路由
 **/
@Service
public class SocketRouteHandle {
    private static final Logger logger = LoggerFactory.getLogger(SocketRouteHandle.class);



    public void route(ChannelHandlerContext ctx, SocketRouteClassAndMethod route,byte[] body,byte[] idByte){
        Object obj = null;
        if("JSON".equals(route.getType())){
            try {
               String jsonStr =  new String(body,"UTF-8");
                if(Map.class.isAssignableFrom(route.getParamType())){
                    obj = GsonUtil.gsonToMap(jsonStr);
                }else{
                    obj = FastJson.getJsonToBean(jsonStr,route.getParamType());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            obj = ProtoUtil.deserializeFromByte(body,route.getParamType());
        }

        long startTime = 0;
        if(route.isPrintLog()){
            startTime=System.currentTimeMillis();
        }

        route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
                new Object[]{ctx.channel(),obj,idByte});

        if(route.isPrintLog()){
            long endTime=System.currentTimeMillis();
            logger.info("方法："+route.getClazz().getName()+"."+route.getMethod().getMethodNames()[route.getIndex()]+
                    "       程序耗时："+(endTime-startTime)+"ms");
        }
    }

}
