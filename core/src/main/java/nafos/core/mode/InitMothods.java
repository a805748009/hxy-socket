package nafos.core.mode;

import nafos.core.entry.ClassAndMethod;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.entry.SocketRouteClassAndMethod;
import nafos.core.mode.filter.HttpMessageFilterInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月4日 下午2:40:05 
 * 开始初始化加载被注解的路由
 */
public class InitMothods  {
	private static Logger logger = LoggerFactory.getLogger(InitMothods.class);

	private static ApplicationContext context = null;

	//http注册路由
	private static Map<String, HttpRouteClassAndMethod> httpRouteMap ;

	//socket注册路由
	private static Map<Integer, SocketRouteClassAndMethod> socketRouteMap ;

	//其他filter等处理事件
	private static final HashMap<String, ClassAndMethod> filterMap = new HashMap<>();



	public static void init( ApplicationContext ac) {
		context = ac;

		//1.遍获取实现Route的类
		RouteFactory rf = new RouteFactory(context);
		httpRouteMap = rf.getHttpRouteMap();
		socketRouteMap = rf.getSocketRouteMap();

		//2.获取通信前置filter
		filterMap.put("httpMessageFilter",new HttpMessageFilterInit(context).getFilter());

		//3.获取远程前置filter
		filterMap.put("remoteCallFilter",new HttpMessageFilterInit(context).getFilter());

		//4.获取http安全校验filter
		filterMap.put("httpSecurityFilter",new HttpMessageFilterInit(context).getFilter());

		//5.获取socket安全校验filter
		filterMap.put("socketSecurityFilter",new HttpMessageFilterInit(context).getFilter());

        //6.获取socket连接filter
		filterMap.put("connectClassAndMethod",new HttpMessageFilterInit(context).getFilter());

        //7.获取socket断开连接filter
		filterMap.put("disConnectClassAndMethod",new HttpMessageFilterInit(context).getFilter());


//		//1.获取user类型class
//		String[] s = context.getBeanNamesForType(BaseUser.class);
//		userClazz = s.length>0?context.getType(context.getBeanNamesForType(BaseUser.class)[0]):null;
//		if(userClazz == null)logger.warn("-------->>>>>>>由于user类未实现BaseUser接口，Room类中操作可能存在报错");
//
//
//
//		//3.获取MQ消息处理handle
//		mqQueueMessageListener = new QueueMessageListenerInit(context).getListener();
//
//		//4.MQ消息监听handle路由
//		mqQueueHandleMap = new QueueMessageHandleInit(context).getHandleRoute();
//


	}
	

	public static void setFilter(String key,ClassAndMethod classAndMethod){
		logger.debug("设置了filter:{}",key);
		filterMap.put(key,classAndMethod);
	}

	public static ClassAndMethod getFilter(String key){
		return filterMap.get(key);
	}

	
	
	 /**
	  * 获取路由方法
	  * @param uri
	  * @return
	  */
	public static HttpRouteClassAndMethod getHttpHandler(String uri) {
		return   httpRouteMap.get(uri);
	}

	public static SocketRouteClassAndMethod getSocketHandler(int code) {
		return   socketRouteMap.get(code);
	}

	public static ClassAndMethod getMessageFilter(){
		return filterMap.get("httpMessageFilter");
	}

	public static ClassAndMethod getRemoteCallFilter(){
		return filterMap.get("remoteCallFilter");
	}

	public static ClassAndMethod getHttpSecurityFilter(){
		return filterMap.get("httpSecurityFilter");
	}

    public static ClassAndMethod getSocketSecurityFilter(){
        return filterMap.get("socketSecurityFilter");
    }

    public static ClassAndMethod getSocketConnectFilter(){
		return filterMap.get("connectClassAndMethod");
    }

    public static ClassAndMethod getSocketDisConnectFilter(){
		return filterMap.get("disConnectClassAndMethod");
    }

	
}
