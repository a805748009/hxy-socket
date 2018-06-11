package com.result.base.inits;

import com.result.base.entry.Base.BaseUser;
import com.result.base.entry.RouteClassAndMethod;
import com.result.base.fitle.MessageFilterInit;
import com.result.base.fitle.RemoteCallFilterInit;
import com.result.base.route.Route;
import com.result.base.security.SessionTimeUpdateHandleInit;
import com.result.serverbootstrap.assist.rabbitMq.init.QueueMessageListenerInit;
import com.result.serverbootstrap.assist.rabbitMq.init.QueueMessageHandleInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;

import java.util.Map;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月4日 下午2:40:05 
 * 开始初始化加载被注解的路由
 */
public class InitMothods extends ApplicationObjectSupport {
	private static final Logger logger = LoggerFactory.getLogger(InitMothods.class);
	
	private static ApplicationContext context = null;
	//所有方法注册路由
	private static Map<String, Object> METHODHANDLEMAP ;
	//filter
	private static RouteClassAndMethod messageFilter = null;
	//filter
	private static RouteClassAndMethod remoteCallFilter = null;
	//user类
	private static Class<?> userClazz ;
	//MQ消息监听处理类
	private static RouteClassAndMethod mqQueueMessageListener = null;
	//MQ消息监听handle路由
	private static Map<String, Object> mqQueueHandleMap ;
	//用户session更新时的外置handle
	private static RouteClassAndMethod sessionUpdateHandle = null;

	
	@Override
	public void initApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
		//0.获取通信前置filter
		messageFilter = new MessageFilterInit(context).getFilter();

		//1.获取user类型class
		String[] s = context.getBeanNamesForType(BaseUser.class);
		userClazz = s.length>0?context.getType(context.getBeanNamesForType(BaseUser.class)[0]):null;
		if(userClazz == null)logger.warn("-------->>>>>>>由于user类未实现BaseUser接口，Room类中操作可能存在报错");
		
		//2.遍获取实现Route的类
		METHODHANDLEMAP = new Route(context).getMETHODHANDLEMAP();

		//3.获取MQ消息处理handle
		mqQueueMessageListener = new QueueMessageListenerInit(context).getListener();

		//4.MQ消息监听handle路由
		mqQueueHandleMap = new QueueMessageHandleInit(context).getHandleRoute();

		//5.读取session更新时附带的操作
		sessionUpdateHandle = new SessionTimeUpdateHandleInit(context).getHandle();

		//6.获取远程前置filter
		remoteCallFilter = new RemoteCallFilterInit(context).getFilter();
	}
	



	
	
	 /**
	  * 获取路由方法
	 * @param <T>
	  * @param taskType
	  * @return
	  */
	public static <T> T getTaskHandler(String taskType) {
		return  (T) METHODHANDLEMAP.get(taskType);
	}

	public static RouteClassAndMethod getMessageFilter(){
		return messageFilter;
	}

	public static RouteClassAndMethod getRemoteCallFilter(){
		return remoteCallFilter;
	}

	public static RouteClassAndMethod getMqQueueMessageLitener(){
		return mqQueueMessageListener;
	}

	public static Class<?> getUserClazz(){
		return userClazz;
	}

	public static <T> T getMqRouteHandle(String taskType) {
		return  (T) mqQueueHandleMap.get(taskType);
	}

	public static RouteClassAndMethod getSessionUpdateHandle(){
		return sessionUpdateHandle;
	}
	
}
