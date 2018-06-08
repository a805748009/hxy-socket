package com.result.base.entry;

import com.esotericsoftware.reflectasm.MethodAccess;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月9日 下午5:17:02 
* 类说明 
*/
public class RouteClassAndMethod {

	protected Class<?> clazz; //类

	protected MethodAccess method; //方法

	protected Integer index; //实例化方法的index

	protected Class<?> paramType;//参数类型class

	public RouteClassAndMethod(Class<?> clazz, MethodAccess method, Integer index, Class<?> paramType) {
		this.clazz = clazz;
		this.method = method;
		this.index = index;
		this.paramType = paramType;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public MethodAccess getMethod() {
		return method;
	}

	public void setMethod(MethodAccess method) {
		this.method = method;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Class<?> getParamType() {
		return paramType;
	}

	public void setParamType(Class<?> paramType) {
		this.paramType = paramType;
	}
}
