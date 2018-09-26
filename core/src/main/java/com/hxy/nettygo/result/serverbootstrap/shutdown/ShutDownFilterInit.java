package com.hxy.nettygo.result.serverbootstrap.shutdown;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hxy.nettygo.result.base.entry.RouteClassAndMethod;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:20
 * @Description TODO
 **/
public class ShutDownFilterInit {

    private RouteClassAndMethod routeClassAndMethod;

    public ShutDownFilterInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(ShutDownFilter.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("run");
            routeClassAndMethod =  new RouteClassAndMethod(context.getType(filterNames[0]), filterMa, index, null,false);
        }
    }

    public RouteClassAndMethod getFilter() {
        return routeClassAndMethod;
    }

}
