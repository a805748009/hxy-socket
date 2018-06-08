package com.result.base.entry;

import com.esotericsoftware.reflectasm.MethodAccess;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月9日 下午5:17:02 
* 类说明 
*/
public class HttpRouteClassAndMethod extends  RouteClassAndMethod {

	protected String type; //JSON 或者 PRO[TOBUFF]

	protected boolean isRequest;//是否需要request

	public HttpRouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType, String type, boolean isRequest) {
		super(clazz, method, index, paramType);
		this.type = type;
		this.isRequest = isRequest;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequest() {
		return isRequest;
	}

	public void setRequest(boolean request) {
		isRequest = request;
	}
}
