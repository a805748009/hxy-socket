package com.hxy.nettygo.result.base.entry;

import java.io.Serializable;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月29日 上午9:47:13 
* 类说明 
*/
public class GoSession implements Serializable{

	private static final long serialVersionUID = 3117098259764339233L;

	private String createTime;
	
	private Object	object;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public GoSession(String createTime, Object object) {
		super();
		this.createTime = createTime;
		this.object = object;
	}

	public GoSession() {
		super();
	}
	
	
}
