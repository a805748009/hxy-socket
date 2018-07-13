package com.hxy.nettygo.result.base.config;

import java.util.concurrent.TimeUnit;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月26日 下午7:44:07 
* 类说明 
*/
public class ConfigForSocketHeart {
	
	public static long readerIdleTime = 5;
	
	public static long writerIdleTime = 0;
	
	public static long allIdleTime = 5;
	
	public static TimeUnit unit = TimeUnit.SECONDS;

}
