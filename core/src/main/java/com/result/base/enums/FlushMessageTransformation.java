package com.result.base.enums;
/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月12日 下午8:18:35 
* 类说明 
*/
public enum FlushMessageTransformation {

	BYTE("BYTE"),
	JSONSTRING("JSONSTRING");

	private String type;

	private FlushMessageTransformation(String type) {
		this.type = type;
	}

	private FlushMessageTransformation() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
