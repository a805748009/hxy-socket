package nafos.core.mode;

import nafos.core.entry.ClassAndMethod;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.mode.filter.InterceptorsInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月4日 下午2:40:05
 * 开始初始化加载被注解的路由
 */
public class InitMothods {
    private static Logger logger = LoggerFactory.getLogger(InitMothods.class);

    private static ApplicationContext context = null;

    //http注册路由
    private static Map<String, HttpRouteClassAndMethod> httpRouteMap;

    //socket注册路由
    private static Map<Integer, SocketRouteClassAndMethod> socketRouteMap;

    // interceptors
    private static Map<Class, ClassAndMethod> interceptors;

    //其他filter等处理事件
    private static final HashMap<String, ClassAndMethod> filterMap = new HashMap<>();

    private static final HashMap<String, List<ClassAndMethod>> filterListMap = new HashMap<>();


    public static void init(ApplicationContext ac) {
        context = ac;

        //1.遍获取实现Route的类
        RouteFactory rf = new RouteFactory(context);
        httpRouteMap = rf.getHttpRouteMap();
        socketRouteMap = rf.getSocketRouteMap();

        //2.获取拦截器
        interceptors = new InterceptorsInit(context).getInterceptors();

        //6.获取socket连接和断开filter
        SocketConnectFactory socketConnectFactory = new SocketConnectFactory(context);
        filterListMap.put("connectClassAndMethods", socketConnectFactory.getConnectClassAndMethod());
        filterListMap.put("disConnectClassAndMethods", socketConnectFactory.getDisConnectClassAndMethod());

//
//		//3.获取MQ消息处理handle
//		mqQueueMessageListener = new QueueMessageListenerInit(context).getListener();
//
//		//4.MQ消息监听handle路由
//		mqQueueHandleMap = new QueueMessageHandleInit(context).getHandleRoute();
//


    }


    public static void setFilter(String key, ClassAndMethod classAndMethod) {
        logger.debug("设置了filter:{}", key);
        filterMap.put(key, classAndMethod);
    }

    public static ClassAndMethod getFilter(String key) {
        return filterMap.get(key);
    }


    /**
     * 获取路由方法
     *
     * @param uri
     * @return
     */
    public static HttpRouteClassAndMethod getHttpHandler(String uri) {
        return httpRouteMap.get(uri);
    }

    public static SocketRouteClassAndMethod getSocketHandler(int code) {
        return socketRouteMap.get(code);
    }

    public static ClassAndMethod getInterceptor(Class clazz) {
        return interceptors.get(clazz);
    }

    public static List<ClassAndMethod> getSocketConnectFilter() {
        return filterListMap.get("connectClassAndMethods");
    }

    public static List<ClassAndMethod> getSocketDisConnectFilter() {
        return filterListMap.get("disConnectClassAndMethods");
    }


}
