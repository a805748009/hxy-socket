package com.hxy.nettygo.result;

import com.hxy.nettygo.result.base.config.ConfigForSecurityMode;
import com.hxy.nettygo.result.base.config.ConfigForSystemMode;
import com.hxy.nettygo.result.base.enums.ConnectType;
import com.hxy.nettygo.result.base.inits.InitHttpNettyServer;
import com.hxy.nettygo.result.base.inits.InitSocketNettyServer;
import com.hxy.nettygo.result.base.inits.InitStartAppTasks;
import com.hxy.nettygo.result.base.redis.JedisListener;

public class Server {
	
	//只能启动一次
	public static  void run(int port,int maxSize){
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
}
