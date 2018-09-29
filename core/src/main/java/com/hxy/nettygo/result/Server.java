package com.hxy.nettygo.result;

import com.hxy.nettygo.result.base.config.ConfigForSecurityMode;
import com.hxy.nettygo.result.base.config.ConfigForSystemMode;
import com.hxy.nettygo.result.base.enums.ConnectType;
import com.hxy.nettygo.result.base.inits.InitHttpNettyServer;
import com.hxy.nettygo.result.base.inits.InitSocketNettyServer;
import com.hxy.nettygo.result.base.inits.InitStartAppTasks;
import com.hxy.nettygo.result.base.redis.JedisListener;
import com.hxy.nettygo.result.serverbootstrap.shutdown.ShutDownHandler;

public class Server {
	
	//只能启动一次
	public static  void run(int port,int maxSize){
		//0.注册停机事件
		ShutDownHandler shutDownHandler = new ShutDownHandler();
		shutDownHandler.registerSignal(getOSSignalType());

		//1.执行项目启动任务
		new InitStartAppTasks().startApp();

		//2.开启redis监听
		if(ConfigForSecurityMode.ISSETSESSIONTOREDIS)
		JedisListener.runRedisListener();

		//3.开启netty服务
		if(ConnectType.HTTP.getType().equals(ConfigForSystemMode.CONNECTTYPE)){
			InitHttpNettyServer.runHttp(port,maxSize); 
        }else{
        	//采用websocket协议，只选择被@on标记的方法  
        	InitSocketNettyServer.runSocket(port,maxSize);
        }
	}

	private static String getOSSignalType()
	{
		return System.getProperties().getProperty("os.name").
				toLowerCase().startsWith("win")||System.getProperties().getProperty("os.name").
				toLowerCase().startsWith("mac") ? "INT" : "USR2";//mac下由于一般是idea开发，开发中idea退出传的是INT
	}
}
