package com.business.bean;

import java.io.Serializable;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月8日 上午11:31:31 
* 类说明 
*/
public class BaseMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6367549336971325857L;
	
	

	private String game;
	
	private String id;
	
	private String type;
	
	private String name;
	
	private boolean flag;
	
	private int number;
	
	private double mydouble;
	
	private String param1;
	
	private String param2;
	
	private String param3;
	
	private String param4;
	
	private String param5;
	
	
	

	public BaseMessage() {
		super();
	}

	public BaseMessage(String game, String id, String type, String name, boolean flag, int number, double mydouble,
			String param1, String param2, String param3, String param4, String param5) {
		super();
		this.game = game;
		this.id = id;
		this.type = type;
		this.name = name;
		this.flag = flag;
		this.number = number;
		this.mydouble = mydouble;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getMydouble() {
		return mydouble;
	}

	public void setMydouble(double mydouble) {
		this.mydouble = mydouble;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}
	
	public void clear(){
		this.game = null;
		this.id = null;
		this.type = null;
		this.name = null;
		this.flag = false;
		this.number = -1;
		this.mydouble = -1;
		this.param1 = null;
		this.param2 = null;
		this.param3 = null;
		this.param4 = null;
		this.param5 = null;
	}

	@Override
	public String toString() {
		return "BaseMessage [game=" + game + ", id=" + id + ", type=" + type + ", name=" + name + ", flag=" + flag
				+ ", number=" + number + ", mydouble=" + mydouble + ", param1=" + param1 + ", param2=" + param2
				+ ", param3=" + param3 + ", param4=" + param4 + ", param5=" + param5 + "]";
	}
	
}
