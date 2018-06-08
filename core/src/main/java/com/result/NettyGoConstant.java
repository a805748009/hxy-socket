package com.result;

import com.result.base.config.ConfigForMQConnect;
import com.result.base.config.ConfigForNettyMode;
import com.result.base.config.ConfigForSecurityMode;
import com.result.base.config.ConfigForSystemMode;
import com.result.base.enums.ServerUrl;

import java.util.List;
import java.util.Map;

/**
 * global类，基础环境配置
 */
public class NettyGoConstant {
	
	/**
	 * 
	* @author huangxinyu
	* @version 创建时间：2018年3月28日 下午6:39:09 
	* @Description: 系统运行配置
	* @param @param connectType  连接类型  HTTP方式：ConnectType.HTTP.getType();  SOCKET方式:ConnectType.SOCKET.getType();
	* @return void    返回类型  
	* @throws
	 */
	public static void setSystemMode(String connectType){
		ConfigForSystemMode.CONNECTTYPE = connectType;
	}

	/**
	* @Author 黄新宇
	* @date 2018/5/4 下午7:40
	* @Description(设置socket转码类型)
	* @param
	* @return
	*/
	public static void setBinaryType(String binaryType){
		ConfigForSystemMode.BINARYTYPE = binaryType;
	}


	/**
	* @Author 黄新宇
	* @date 2018/5/4 下午7:38
	* @Description(设置INTBEFOREl类型  Id>>>>URI 的MAP)
	* @param [socketRouteMao]
	* @return void
	*/
	public static void setSocketRouteMap(Map socketRouteMao){
		ConfigForSystemMode.SOCKETROUTEMAP = socketRouteMao;
	}
	
	/**
	 * 
	* @author huangxinyu
	* @version 创建时间：2018年4月14日 下午4:07:33 
	* @Description: TODO(redis工具类配置配置)  
	* @param @param loginSessionTimeOut   //redis登录session缓存的时间 秒为单位
	* @param @param isSessionToRedis   //是否开启redis作为分布式session缓存
	* @return void    返回类型  
	* @throws
	 */
	public static void setRedisSecurityMode(int loginSessionTimeOut,boolean isSessionToRedis){
		ConfigForSecurityMode.LOGINSESSIONTIMEOUT = loginSessionTimeOut;
		ConfigForSecurityMode.ISSETSESSIONTOREDIS = isSessionToRedis;
	}
	
	
	//----------------------------以下是HTTP用到的配置
	
	/**
	 * 
	* @author huangxinyu
	* @version 创建时间：2018年3月28日 下午6:56:04 
	* @Description: 安全相关配置
	* @param @param isValidate  //是否开启登录配置验证  NO 不开启  ALLVALIDATE 默认全部验证  NOVALIDATE 默认全部不验证
	* @param @param exceptionValidate //开启登录验证时不需要登录就能访问的URL
	* @param @param loginSessionTimeOut   //redis登录session缓存的时间 秒为单位
	* @param @param isSessionToRedis   //是否开启redis作为分布式session缓存
	* @return void    返回类型  
	* @throws
	 */
	public static void setSecurityMode(String isValidate,List<String> exceptionValidate,int loginSessionTimeOut,boolean isSessionToRedis){
		ConfigForSecurityMode.ISVALIDATE = isValidate;
		ConfigForSecurityMode.EXCEPTIONVALIDATE = exceptionValidate;
		ConfigForSecurityMode.LOGINSESSIONTIMEOUT = loginSessionTimeOut;
		ConfigForSecurityMode.ISSETSESSIONTOREDIS = isSessionToRedis;
		ConfigForSecurityMode.EXCEPTIONVALIDATE.add(ServerUrl.PROXY.getUrl());//负载均衡的链接
	}

	
	/**
	 * 
	* @author huangxinyu
	* @version 创建时间：2018年3月28日 下午7:12:19 
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param executorPoolMinSize //线程池最小线程数
	* @param @param executorPoolMaxSize //方法路由连线程池最大线程数
	* @param @param encodedType    //编码方式
	* @return void    返回类型  
	* @throws
	 */
	public static void setNettyMode(int executorPoolMinSize,int executorPoolMaxSize,String encodedType){
		ConfigForNettyMode.EXECUTORPOOLMINSIZE = executorPoolMinSize;
		ConfigForNettyMode.EXECUTORPOOLMAXSIZE = executorPoolMaxSize;
		ConfigForNettyMode.ENCODEDTYPE = encodedType;
	}

	/**
	* @Author 黄新宇
	* @date 2018/5/11 下午3:52
	* @Description(mq配置)
	* @param [mqOpen, mqHost, mqUserName, mqPassword]
	* @return void
	*/

	public static void setMqMode(boolean mqOpen,String mqHost,String mqUserName,String mqPassword){
		ConfigForMQConnect.MQ_OPEN = mqOpen;
		ConfigForMQConnect.MQ_HOST = mqHost;
		ConfigForMQConnect.MQ_USERNAME = mqUserName;
		ConfigForMQConnect.MQ_PASSWORD = mqPassword;
	}
	

	
	
}
