package com.business.enums;
/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年2月27日 下午7:06:32 
* 类说明 
*/
public enum GameType {
	
	
	NOONEDIE("001","NOONEDIE","NOONEDIE"),
	BOOTMAN("002","BOOTMAN","BOOTMAN");
	
	
	
	
	private String number;

	private String name;

	private String key;

	GameType(String number, String name, String key) {
		this.number = number;
		this.name = name;
		this.key = key;
	}

	private GameType() {
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
