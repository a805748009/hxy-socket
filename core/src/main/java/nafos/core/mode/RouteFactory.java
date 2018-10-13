package nafos.core.mode;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.annotation.controller.Controller;
import nafos.core.annotation.controller.Handle;
import nafos.core.annotation.rpc.RemoteCall;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午9:29
 * @Description TODO
 **/
public class RouteFactory {

    private static final Logger logger = LoggerFactory.getLogger(RouteFactory.class);

    private final Map<String, HttpRouteClassAndMethod> HTTPMETHODHANDLEMAP = new HashMap<>();

    private final Map<Integer, SocketRouteClassAndMethod> SOCKETMETHODHANDLEMAP = new HashMap<>();

    public final static String REMOTE_CALL_URI = "/nafosRemoteCall";




    public RouteFactory(ApplicationContext context) {
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(Controller.class);
        //2.2 遍历判断，合格的注册到map
        taskBeanMap.keySet().forEach(beanName -> {
            detectHandlerMethods(context.getType((String) beanName));
            logger.debug("====================路由注册找到类："+beanName);
        });
    }



    public Map<String, HttpRouteClassAndMethod> getHttpRouteMap() {
        return HTTPMETHODHANDLEMAP;
    }

    public Map<Integer, SocketRouteClassAndMethod> getSocketRouteMap() {
        return SOCKETMETHODHANDLEMAP;
    }



    /**
     * 判断类是不是路由Controller
     * @param beanType
     * @return
     */
    private boolean isHandler(Class<?> beanType) {
        return AnnotationUtils.findAnnotation(beanType, Controller.class) != null;
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
        Set<Method> methods = MethodIntrospector.selectMethods(userType, new ReflectionUtils.MethodFilter(){
            public boolean matches(Method method) {
                    return method.isAnnotationPresent(Handle.class)||method.isAnnotationPresent(RemoteCall.class);
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

        //获取方法method上的@Nuri实例。
        Handle handle = AnnotationUtils.findAnnotation(method, Handle.class);

        //socket路由
        if(handle.code()>0){
            socketRoutRecord(method,handlerType,handle);
        }

        //http路由
        if(ObjectUtil.isNotNull(handle.uri())){
            httpRoutRecord(method,handlerType,handle);
        }
    }


    /**
     * SOCKET路由记录入map
     * @param method
     * @param handlerType
     * @param handle
     */
    private void socketRoutRecord(Method method, Class<?> handlerType,Handle handle){
        MethodAccess ma = MethodAccess.get(handlerType);
        Integer code = handle.code();

        SOCKETMETHODHANDLEMAP.put(code,new SocketRouteClassAndMethod(handlerType, ma,
                ma.getIndex(method.getName()),method.getParameterTypes().length>0?method.getParameterTypes()[1]:null,
                handle.printLog(),handle.type(),handle.runOnWorkGroup()));

    }


    /**
     * HTTP路由记录入map
     * @param method
     * @param handlerType
     * @param handle
     */
    private void httpRoutRecord(Method method, Class<?> handlerType,Handle handle){
        MethodAccess ma = MethodAccess.get(handlerType);
        boolean isRemote = false;
        String uri = "";

        //检查方法所属的类有没有@Nuri注解
        Handle classNuri = AnnotationUtils.findAnnotation(handlerType,Handle.class);
        RemoteCall RemoteCall = AnnotationUtils.findAnnotation(method,RemoteCall.class);

        String methodType = handle.method()+":";
        if (classNuri != null) {
            //有类层次的@Nuri注解,就对方法和类的url进行拼接
            uri = classNuri.uri()+handle.uri();
        }else{
            uri = handle.uri();
        }

        if (RemoteCall != null) {
            uri = REMOTE_CALL_URI + uri;
            isRemote = true;
        }
        uri = methodType+uri;

        //如果是远程调用就直接加入免登录访问
//        if(isRemote){
//            ConfigForSecurityMode.EXCEPTIONVALIDATE.add(uri);
//        }
        HTTPMETHODHANDLEMAP.put(uri,new HttpRouteClassAndMethod(handlerType, ma,
                ma.getIndex(method.getName()),method.getParameterTypes().length>0?method.getParameterTypes()[0]:null,
                handle.printLog(),handle.type(),handle.runOnWorkGroup(),method.getParameterTypes().length==1?false:true));

    }
}
