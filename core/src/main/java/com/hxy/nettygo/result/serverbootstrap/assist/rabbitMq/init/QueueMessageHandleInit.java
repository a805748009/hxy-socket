package com.hxy.nettygo.result.serverbootstrap.assist.rabbitMq.init;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hxy.nettygo.result.base.annotation.QueueListener;
import com.hxy.nettygo.result.base.annotation.Route;
import com.hxy.nettygo.result.base.entry.SocketRouteClassAndMethod;
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
 * @Date 2018/5/11 上午9:34
 * @Description TODO
 **/
public class QueueMessageHandleInit {

    private static final Logger logger = LoggerFactory.getLogger(QueueMessageHandleInit.class);

    private Map<String, Object> METHODHANDLEMAP = new HashMap<>();

    public QueueMessageHandleInit(ApplicationContext context) {
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(Route.class);
        //2.2 遍历判断，合格的注册到map
        taskBeanMap.keySet().forEach(beanName -> {
            detectHandlerMethods(context.getType((String) beanName));
            logger.info("====================MQ方法注册找到bean："+beanName);
        });
    }

    public Map<String, Object> getHandleRoute() {
        return METHODHANDLEMAP;
    }

    /**
     * 判断类是不是路由Route
     * @param beanType
     * @return
     */
    private boolean isHandler(Class<?> beanType) {
        return AnnotationUtils.findAnnotation(beanType, Route.class) != null;
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
                return method.isAnnotationPresent(QueueListener.class);
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
        String uri = "";
        MethodAccess ma = MethodAccess.get(handlerType);
        //获取方法method上的@QueueListener实例。
        QueueListener methodQueueListener = AnnotationUtils.findAnnotation(method, QueueListener.class);
        //方法被注解了
        if (methodQueueListener != null) {
            uri = methodQueueListener.value();
        }
        METHODHANDLEMAP.put(uri,new SocketRouteClassAndMethod(handlerType,ma,ma.getIndex(method.getName()),method.getParameterTypes()[0]));
        logger.info("================注册MQ-listener："+uri+"方法："+method);
    }
}
