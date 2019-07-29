package nafos.core.mode;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.core.Enums.Protocol;
import nafos.core.annotation.Controller;
import nafos.core.annotation.Handle;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
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

    public static Protocol defaultProtocol = Protocol.JSON;

    private static String parentPath = null;

    private static Class[] interceptors = null;


    public RouteFactory(ApplicationContext context) {
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(Controller.class);
        //2.2 遍历判断，合格的注册到map
        taskBeanMap.keySet().forEach(beanName -> {
            detectHandlerMethods(context.getType(beanName));
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
     * 并写入parentPath
     *
     * @param beanType
     * @return
     */
    private boolean isHandler(Class<?> beanType) {
        Controller controller = AnnotatedElementUtils.findMergedAnnotation(beanType, Controller.class);
        boolean isHandler = controller != null;
        if (isHandler) {
            parentPath =  controller.value();
            if (controller.interceptor() != null) {
                interceptors = controller.interceptor();
            }
        }
        return isHandler;
    }

    /**
     * 根据类遍历方法，拼接后注册实际操作方法
     *
     * @param handlerType
     */
    private void detectHandlerMethods(final Class<?> handlerType) {

        if (!isHandler(handlerType)) return;

        //获取类的父类，此处没有，返回的本身类
        final Class<?> userType = ClassUtils.getUserClass(handlerType);

        //把所有实现route的类方法放置到set中
        Set<Method> methods = MethodIntrospector.selectMethods(userType, new ReflectionUtils.MethodFilter() {
            public boolean matches(Method method) {
                return AnnotatedElementUtils.findMergedAnnotation(method, Handle.class) != null;
            }
        });
        for (Method method : methods) {
            //注册请求映射
            registerHandlerMethod(method, userType);
        }
    }


    /**
     * 注册到方法MAP
     *
     * @param method
     * @param handlerType
     */
    private void registerHandlerMethod(Method method, Class<?> handlerType) {

        //获取方法method上的@Nuri实例。
        Handle handle = AnnotatedElementUtils.findMergedAnnotation(method, Handle.class);

        //socket路由
        if (handle.code() > 0) {
            socketRoutRecord(method, handlerType, handle);
        }

        //http路由
        if (ObjectUtil.isNotNull(handle.uri())) {
            httpRoutRecord(method, handlerType, handle);
        }
    }


    /**
     * SOCKET路由记录入map
     *
     * @param method
     * @param handlerType
     * @param handle
     */
    private void socketRoutRecord(Method method, Class<?> handlerType, Handle handle) {
        MethodAccess ma = MethodAccess.get(handlerType);
        Integer code = handle.code();

        SOCKETMETHODHANDLEMAP.put(code, new SocketRouteClassAndMethod(handlerType, ma,
                ma.getIndex(method.getName()), method.getParameterTypes().length > 0 ? method.getParameterTypes()[1] : null,
                handle.printLog(), handle.type() == Protocol.DEFAULT ? defaultProtocol : handle.type(), handle.runOnWorkGroup(), interceptors));

    }


    /**
     * HTTP路由记录入map
     *
     * @param method
     * @param handlerType
     * @param handle
     */
    private void httpRoutRecord(Method method, Class<?> handlerType, Handle handle) {
        MethodAccess ma = MethodAccess.get(handlerType);
        String uri;
        String methodType = handle.method() + ":";
        if (parentPath != null) {
            //有类层次的@Nuri注解,就对方法和类的url进行拼接
            uri = parentPath + handle.uri();
        } else {
            uri = handle.uri();
        }
        uri = methodType + uri;

        logger.debug("register router:{}",uri);
        HTTPMETHODHANDLEMAP.put(uri, new HttpRouteClassAndMethod(handlerType, ma,
                ma.getIndex(method.getName()), null,
                handle.printLog(), handle.type() == Protocol.DEFAULT ? defaultProtocol : handle.type(), handle.runOnWorkGroup(), method.getParameters(), interceptors));


    }
}
