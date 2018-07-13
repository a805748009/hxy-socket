package com.hxy.nettygo.result.serverbootstrap.assist.rabbitMq.init;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hxy.nettygo.result.base.entry.RouteClassAndMethod;
import com.hxy.nettygo.result.serverbootstrap.assist.rabbitMq.QueueMessageListener;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:42
 * @Description TODO
 **/
public class QueueMessageListenerInit {

    private RouteClassAndMethod routeClassAndMethod;

    public QueueMessageListenerInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(QueueMessageListener.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("messageListener");
            routeClassAndMethod =  new RouteClassAndMethod(context.getType(filterNames[0]), filterMa, index, null);
        }
    }

    public RouteClassAndMethod getListener() {
        return routeClassAndMethod;
    }
}
