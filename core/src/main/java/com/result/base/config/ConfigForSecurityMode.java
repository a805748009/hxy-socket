package com.result.base.config;

import java.util.ArrayList;
import java.util.List;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月28日 下午6:47:38 
* 类说明 
*/
public class ConfigForSecurityMode {
	
	//是否开启登录配置验证  NO 不开启  ALLVALIDATE 默认全部验证  NOVALIDATE 默认全部不验证
	public static String ISVALIDATE = "NO";
	
	//开启登录验证时不需要登录就能访问的URL
	public static List<String> EXCEPTIONVALIDATE = new ArrayList<>();
	
	//redis登录session缓存的时间 秒为单位
	public static int LOGINSESSIONTIMEOUT = 18000;
	
	//是否采用redis存储用户session（集群或者分布式模式下建议启动）
	public static boolean ISSETSESSIONTOREDIS = false;
	

}
