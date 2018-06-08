package com.result.base.enums;
/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月12日 下午8:18:35 
* 类说明 
*/
public enum Deployment {
	
	BASE("BASE"),
	CLOUD("CLOUD");
	
	private String type;

	private Deployment(String type) {
		this.type = type;
	}

	private Deployment() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
