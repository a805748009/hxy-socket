//package hxy.server.socket.engine.factory;
//
//import com.esotericsoftware.reflectasm.MethodAccess;
//import hxy.server.socket.anno.CodeHandler;
//import hxy.server.socket.anno.Handle;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.MethodIntrospector;
//import org.springframework.core.annotation.AnnotatedElementUtils;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @Author 黄新宇
// * @Date 2018/5/10 下午9:29
// **/
//public class RouteFactory {
//
//    private static final Logger logger = LoggerFactory.getLogger(RouteFactory.class);
//
//    private final static Map<Integer, RouteClassAndMethod> socketMethodHandleMap = new HashMap<>();
//
//    private String parentPath = null;
//
//    private Class[] interceptors = null;
//
//    private String[] interceptorParams = null;
//
//
//    public RouteFactory(ApplicationContext context) {
//        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(CodeHandler.class);
//        //2.2 遍历判断，合格的注册到map
//        taskBeanMap.keySet().forEach(beanName -> {
//            detectHandlerMethods(context.getType(beanName));
//        });
//    }
//
//
//    /**
//     * @Description 根据类遍历方法，拼接后注册实际操作方法
//     * @Author xinyu.huang
//     * @Time 2019/11/28 23:16
//     */
//    private void detectHandlerMethods(final Class<?> handlerType) {
//        //获取类的父类，此处没有，返回的本身类
//        final Class<?> userType = ClassUtils.getUserClass(handlerType);
//
//        //把所有实现route的类方法放置到set中
//        Set<Method> methods = MethodIntrospector.selectMethods(userType, (ReflectionUtils.MethodFilter) method -> AnnotatedElementUtils.findMergedAnnotation(method, Handle.class) != null);
//        for (Method method : methods) {
//            //注册请求映射
//            registerHandlerMethod(method, userType);
//        }
//    }
//
//
//    /***
//     *@Description 注册到方法MAP
//     *@Author xinyu.huang
//     *@Time 2019/11/28 23:16
//     */
//    private void registerHandlerMethod(Method method, Class<?> handlerType) {
//        //获取方法method上的@Nuri实例。
//        Handle handle = AnnotatedElementUtils.findMergedAnnotation(method, Handle.class);
//        socketRoutRecord(method, handlerType, handle);
//
//    }
//
//
//    /**
//     * @Description SOCKET路由记录入map
//     * @Author xinyu.huang
//     * @Time 2019/11/28 23:12
//     */
//    private void socketRoutRecord(Method method, Class<?> handlerType, Handle handle) {
//        MethodAccess ma = MethodAccess.get(handlerType);
//        Integer code = handle.code();
//
//        socketMethodHandleMap.put(code, new RouteClassAndMethod(handlerType, ma,
//                ma.getIndex(method.getName()), method.getParameterTypes().length > 0 ? method.getParameterTypes() : null,
//                handle.type() == Protocol.DEFAULT ? defaultProtocol : handle.type(), hmInterptors(method), hmInterptorParams(method)));
//
//    }
//
//
//}
