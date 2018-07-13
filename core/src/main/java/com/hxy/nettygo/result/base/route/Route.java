package com.hxy.nettygo.result.base.route;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hxy.nettygo.result.base.annotation.On;
import com.hxy.nettygo.result.base.config.ConfigForSystemMode;
import com.hxy.nettygo.result.base.annotation.BCRemoteCall;
import com.hxy.nettygo.result.base.annotation.Nuri;
import com.hxy.nettygo.result.base.config.ConfigForSecurityMode;
import com.hxy.nettygo.result.base.entry.HttpRouteClassAndMethod;
import com.hxy.nettygo.result.base.entry.SocketRouteClassAndMethod;
import com.hxy.nettygo.result.base.enums.ConnectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethodSelector;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:29
 * @Description TODO
 **/
public class Route {

    private static final Logger logger = LoggerFactory.getLogger(Route.class);

    private Map<String, Object> METHODHANDLEMAP = new HashMap<>();

    public Route(ApplicationContext context) {
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(com.hxy.nettygo.result.base.annotation.Route.class);
        //2.2 遍历判断，合格的注册到map
        taskBeanMap.keySet().forEach(beanName -> {
            detectHandlerMethods(context.getType((String) beanName));
            logger.info("====================路由注册找到类："+beanName);
        });
    }

    public Map<String, Object> getMETHODHANDLEMAP() {
        return METHODHANDLEMAP;
    }

    /**
     * 判断类是不是路由Route
     * @param beanType
     * @return
     */
    private boolean isHandler(Class<?> beanType) {
        return AnnotationUtils.findAnnotation(beanType, com.hxy.nettygo.result.base.annotation.Route.class) != null;
    }

    /**
     * 根据类遍历方法，拼接后注册实际操作方法
     * @param handlerType
     */
    private void detectHandlerMethods(final Class<?> handlerType) {
        if(!isHandler(handlerType))return;
        //获取类的父类，此处没有，返回的本身类
        final Class<?> userType = ClassUtils.getUserClass(handlerType);
        //把所有实现route的类方法放置到set中
        Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new ReflectionUtils.MethodFilter(){
            public boolean matches(Method method) {
                if(ConnectType.HTTP.getType().equals(ConfigForSystemMode.CONNECTTYPE)){
                    //只选择被@Nurl标记的方法
                    return method.isAnnotationPresent(Nuri.class);
                }else{
                    //采用websocket协议，只选择被@on标记的方法
                    return method.isAnnotationPresent(On.class)||method.isAnnotationPresent(BCRemoteCall.class);
                }

            }
        });

        for (Method method : methods) {
            //注册请求映射
            registerHandlerMethod(method, userType);
        }
    }



    /**
     * 注册到方法MAP
     * @param method
     * @param handlerType
     */
    private void registerHandlerMethod(Method method, Class<?> handlerType) {
        boolean isRemote = false;
        String uri = "";
        MethodAccess ma = MethodAccess.get(handlerType);
        if(ConnectType.HTTP.getType().equals(ConfigForSystemMode.CONNECTTYPE)){
            //获取方法method上的@Nuri实例。
            Nuri methodNuri = AnnotationUtils.findAnnotation(method, Nuri.class);
            //方法被注解了
            if (methodNuri != null) {
                //检查方法所属的类有没有@Nuri注解
                Nuri classNuri = AnnotationUtils.findAnnotation(handlerType,Nuri.class);
                BCRemoteCall classBCRemoteCall = AnnotationUtils.findAnnotation(method,BCRemoteCall.class);
                String methodType = methodNuri.method()+":";
                if (classNuri != null) {
                    //有类层次的@Nuri注解,就对方法和类的url进行拼接
                    uri = classNuri.uri()+methodNuri.uri();
                }else{
                    uri = methodNuri.uri();
                }
                if (classBCRemoteCall != null) {
                    uri = ConfigForSystemMode.REMOTE_CALL_URI+uri;
                    isRemote = true;
                }
                uri = methodType+uri;
                //如果是远程调用就直接加入免登录访问
                if(isRemote){
                    ConfigForSecurityMode.EXCEPTIONVALIDATE.add(uri);
                }
            }
            METHODHANDLEMAP.put(uri,new HttpRouteClassAndMethod(handlerType, ma,
                    ma.getIndex(method.getName()),method.getParameterTypes().length>0?method.getParameterTypes()[0]:null,methodNuri.type(),method.getParameterTypes().length==1?false:true));
        }else{
            //获取方法method上的@On实例。
            On methodOn = AnnotationUtils.findAnnotation(method, On.class);
            //方法被注解了
            if (methodOn != null) {
                uri = methodOn.value();
                METHODHANDLEMAP.put(uri,new SocketRouteClassAndMethod(handlerType,ma,ma.getIndex(method.getName()),method.getParameterTypes().length>1?method.getParameterTypes()[1]:null));
            }
            //加載RPC路由
            BCRemoteCall classBCRemoteCall = AnnotationUtils.findAnnotation(method,BCRemoteCall.class);
            if (classBCRemoteCall != null) {
                Nuri methodNuri = AnnotationUtils.findAnnotation(method, Nuri.class);
                if (methodNuri != null) {
                    String methodType = methodNuri.method()+":";
                    Nuri classNuri = AnnotationUtils.findAnnotation(handlerType,Nuri.class);
                    if (classNuri != null) {
                        //有类层次的@Nuri注解,就对方法和类的url进行拼接
                        uri = classNuri.uri()+methodNuri.uri();
                    }else{
                        uri = methodNuri.uri();
                    }
                    uri = ConfigForSystemMode.REMOTE_CALL_URI+uri;
                    uri = methodType+uri;
                }
                //如果是远程调用就直接加入免登录访问
                ConfigForSecurityMode.EXCEPTIONVALIDATE.add(uri);

                METHODHANDLEMAP.put(uri,new HttpRouteClassAndMethod(handlerType, ma,
                        ma.getIndex(method.getName()),method.getParameterTypes().length>0?method.getParameterTypes()[0]:null,methodNuri.type(),method.getParameterTypes().length==1?false:true));
            }
        }
        logger.info("================注册uri："+uri+"方法："+method);
    }
}
