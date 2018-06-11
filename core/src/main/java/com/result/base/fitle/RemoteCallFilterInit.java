package com.result.base.fitle;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.result.base.entry.RouteClassAndMethod;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/6/11 下午6:05
 * @Description TODO
 **/
public class RemoteCallFilterInit {
    private RouteClassAndMethod routeClassAndMethod;

    public RemoteCallFilterInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(RemoteCallFilter.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("filter");
            routeClassAndMethod =  new RouteClassAndMethod(context.getType(filterNames[0]), filterMa, index, null);
        }
    }

    public RouteClassAndMethod getFilter() {
        return routeClassAndMethod;
    }
}
