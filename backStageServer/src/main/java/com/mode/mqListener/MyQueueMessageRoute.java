package com.mode.mqListener;

import com.result.base.entry.SocketRouteClassAndMethod;
import com.result.base.entry.backStageBean.BaseMqMessage;
import com.result.base.inits.InitMothods;
import com.result.base.tools.ObjectUtil;
import com.result.base.tools.SerializationUtil;
import com.result.base.tools.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author 黄新宇
 * @Date 2018/5/11 下午3:58
 * @Description TODO
 **/
public class MyQueueMessageRoute implements  Runnable{
    private static final Logger logger = LoggerFactory.getLogger(MyQueueMessageRoute.class);

    private byte[] content ;

    public MyQueueMessageRoute(byte[] b ){
        this.content = b;
    }

    @Override
    public void run() {
        BaseMqMessage baseMqMessage = SerializationUtil.deserializeFromByte(content,
                BaseMqMessage.class);
        routeMethod(baseMqMessage.getUri(),content);
    }

    private void routeMethod(String uri,byte[] content) {
        SocketRouteClassAndMethod route = InitMothods.getMqRouteHandle(uri);
        if (ObjectUtil.isNull(route)) {
            logger.error("================>>>>MQ消息监听路由方法未找到:" + uri);
            return;
        }
        logger.error("收到MQ发来的消息================>>>>:" + uri);
        route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),
                route.getIndex(), new Object[]{SerializationUtil.deserializeFromByte(content, route.getParamType())});
    }


}
