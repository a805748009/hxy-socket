package com.result.base.security;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.result.base.entry.RouteClassAndMethod;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/5/17 下午3:24
 * @Description TODO
 **/
public class SessionTimeUpdateHandleInit {

    private RouteClassAndMethod routeClassAndMethod;

    public SessionTimeUpdateHandleInit(ApplicationContext context) {
        String[] filterNames = context.getBeanNamesForType(SessionTimeUpdate.class);
        if (filterNames.length > 0) {
            //自定义了handleHttpRequest
            MethodAccess filterMa = MethodAccess.get(context.getType(filterNames[0]));
            int index = filterMa.getIndex("run");
            routeClassAndMethod =  new RouteClassAndMethod(context.getType(filterNames[0]), filterMa, index, null);
        }
    }

    public RouteClassAndMethod getHandle() {
        return routeClassAndMethod;
    }
}
