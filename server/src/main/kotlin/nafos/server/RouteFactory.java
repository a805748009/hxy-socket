package nafos.server;

import com.esotericsoftware.reflectasm.MethodAccess;
import nafos.server.annotation.Controller;
import nafos.server.annotation.Handle;
import nafos.server.annotation.Interceptor;
import nafos.server.enums.Protocol;
import nafos.server.util.ArrayUtil;
import nafos.server.enums.Protocol;
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

    private final static Map<String, HttpRouteClassAndMethod> httpMethodHandleMap = new HashMap<>();

    private final static Map<Integer, RouteClassAndMethod> socketMethodHandleMap = new HashMap<>();

    private Protocol defaultProtocol = Protocol.JSON;

    private String parentPath = null;

    private Class[] interceptors = null;

    private String[] interceptorParams = null;


    public RouteFactory(ApplicationContext context) {
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(Controller.class);
        //2.2 遍历判断，合格的注册到map
        taskBeanMap.keySet().forEach(beanName -> {
            detectHandlerMethods(context.getType(beanName));
        });
    }


    public static HttpRouteClassAndMethod getHttpHandler(String uri) {
        return httpMethodHandleMap.get(uri);
    }

    public static RouteClassAndMethod getSocketHandler(int code) {
        return socketMethodHandleMap.get(code);
    }


    /**
     *@Description 判断类是不是路由Controller  并写入parentPath
     *@Author      xinyu.huang
     *@Time        2019/11/28 23:16
     */
    private boolean isHandler(Class<?> beanType) {
        Controller controller = AnnotatedElementUtils.findMergedAnnotation(beanType, Controller.class);
        boolean isHandler = controller != null;
        if (isHandler) {
            parentPath = controller.value();
            if (controller.interceptor() != null) {
                interceptors = controller.interceptor();
                interceptorParams = controller.interceptorParams();
                int lengthDif = interceptors.length - interceptorParams.length;
                if (lengthDif > 0) {
                    interceptorParams = (String[]) ArrayUtil.concat(interceptorParams, new String[lengthDif]);
                }
            }
        }
        return isHandler;
    }

    /**
     *@Description 根据类遍历方法，拼接后注册实际操作方法
     *@Author      xinyu.huang
     *@Time        2019/11/28 23:16
     */
    private void detectHandlerMethods(final Class<?> handlerType) {

        if (!isHandler(handlerType)) {
            return;
        }

        //获取类的父类，此处没有，返回的本身类
        final Class<?> userType = ClassUtils.getUserClass(handlerType);

        //把所有实现route的类方法放置到set中
        Set<Method> methods = MethodIntrospector.selectMethods(userType, (ReflectionUtils.MethodFilter) method -> AnnotatedElementUtils.findMergedAnnotation(method, Handle.class) != null);
        for (Method method : methods) {
            //注册请求映射
            registerHandlerMethod(method, userType);
        }
    }


   /***
    *@Description 注册到方法MAP
    *@Author      xinyu.huang
    *@Time        2019/11/28 23:16
    */
    private void registerHandlerMethod(Method method, Class<?> handlerType) {

        //获取方法method上的@Nuri实例。
        Handle handle = AnnotatedElementUtils.findMergedAnnotation(method, Handle.class);

        //socket路由
        if (handle.code() > 0) {
            socketRoutRecord(method, handlerType, handle);
        }else {
            //http路由
            httpRoutRecord(method, handlerType, handle);
        }
    }


    /**
     *@Description SOCKET路由记录入map
     *@Author      xinyu.huang
     *@Time        2019/11/28 23:12
     */
    private void socketRoutRecord(Method method, Class<?> handlerType, Handle handle) {
        MethodAccess ma = MethodAccess.get(handlerType);
        Integer code = handle.code();

        socketMethodHandleMap.put(code, new RouteClassAndMethod(handlerType, ma,
                ma.getIndex(method.getName()), method.getParameterTypes().length > 0 ? method.getParameterTypes() : null,
                handle.type() == Protocol.DEFAULT ? defaultProtocol : handle.type(), hmInterptors(method), hmInterptorParams(method)));

    }


    /**
     *@Description HTTP路由记录入map
     *@Author      xinyu.huang
     *@Time        2019/11/28 23:14
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

        logger.debug("register router:{}", uri);


        httpMethodHandleMap.put(uri, new HttpRouteClassAndMethod(handlerType, ma,
                ma.getIndex(method.getName()), null,
                handle.type() == Protocol.DEFAULT ? defaultProtocol : handle.type(),
                method.getParameters(), hmInterptors(method), hmInterptorParams(method)));


    }

    private Class[] hmInterptors(Method method) {
        Interceptor interceptor = AnnotatedElementUtils.findMergedAnnotation(method, Interceptor.class);
        Class[] hmInterceptors;
        if (interceptor != null) {
            hmInterceptors = (Class[]) ArrayUtil.concat(interceptors, interceptor.value());
        } else {
            hmInterceptors = interceptors;
        }
        return hmInterceptors;
    }

    private String[] hmInterptorParams(Method method) {
        Interceptor interceptor = AnnotatedElementUtils.findMergedAnnotation(method, Interceptor.class);
        String[] params;
        if (interceptor != null) {
            params = (String[]) ArrayUtil.concat(interceptorParams, interceptor.interceptorParams());
        } else {
            params = interceptorParams;
        }
        int lengthDif = hmInterptors(method).length - params.length;
        if (lengthDif > 0) {
            params = (String[]) ArrayUtil.concat(params, new String[lengthDif]);
        }
        return params;
    }
}
