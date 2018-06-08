package com.result.serverbootstrap.assist.proxy.socket;
/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月19日 下午3:22:52 
* 类说明 
*/
public class ProxyNotes {
	
	private static long socketLoadNum = 0l;
	
	private static long socketSendIndex = 0l;
	
	private static String socketIpAndPort = null;
	
	
	
	public static String getSocketIpAndPort(){
		return socketIpAndPort;
	}
	
	public static long getSocketLoadNum(){
		return socketLoadNum;
	}
	
	public static long getSocketSendIndex(){
		return socketSendIndex;
	}

	public static void setSocketLoadNum(long socketLoadNum) {
		ProxyNotes.socketLoadNum = socketLoadNum;
	}

	public static void setSocketSendIndex(long socketSendIndex) {
		ProxyNotes.socketSendIndex = socketSendIndex;
	}

	public static void setSocketIpAndPort(String socketIpAndPort) {
		ProxyNotes.socketIpAndPort = socketIpAndPort;
	}
	
	
	

}
