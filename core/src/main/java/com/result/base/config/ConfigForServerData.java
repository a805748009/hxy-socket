package com.result.base.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.result.serverbootstrap.assist.proxy.socket.entry.ProxyHttpToSocketContact;
import com.result.serverbootstrap.assist.proxy.socket.entry.ServerData;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月19日 下午5:37:41 
* 类说明 
*/
public class ConfigForServerData {
	
	public static String SERVERNAME; //服务名称
	
	public static String SERVERID; //id
	
	public static String SERVEREXTRANETIP ;//公网IP
	
	public static String SERVERINTRANETIP ;//内网IP
	
	public static int SERVERPORT;//端口
	
	public static int WEIGHT;//权重
	
	public static boolean isSocketProxy = false;//是否开启负载均衡
	
	public static Map<String,List<ServerData>> SERVERLIST = new HashMap<>();//服务相关信息
	
	public static List<ProxyHttpToSocketContact> PROXYHTTPTOSOCKETCONTACTLIST = new ArrayList<>();//HTTP中连接Socket服务的负载
}
