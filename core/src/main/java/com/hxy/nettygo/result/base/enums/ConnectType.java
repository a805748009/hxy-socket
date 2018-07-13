package com.hxy.nettygo.result.base.enums;
/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月12日 下午8:18:35 
* 类说明 
*/
public enum ConnectType {
	
	HTTP("HTTP"),
	SOCKET("SOCKET");
	
	private String type;

	private ConnectType(String type) {
		this.type = type;
	}

	private ConnectType() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
