package com.mode.error;

import io.netty.handler.codec.http.HttpResponseStatus;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月28日 下午8:17:06 
* 类说明 
*/
public class MyHttpResponseStatus extends HttpResponseStatus{
	
	
	public MyHttpResponseStatus(int code, String reasonPhrase) {
		super(code, reasonPhrase);
	}

	//正在维护
	public static final MyHttpResponseStatus SERVERSHUTDOWN = new MyHttpResponseStatus(800, "系统维护中");

	public static final MyHttpResponseStatus CREATE_ORDER_FAILED = new MyHttpResponseStatus(700, "创建订单失败");


	 
	 
	 
	 
}
