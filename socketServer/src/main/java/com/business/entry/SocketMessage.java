package com.business.entry;

import com.result.base.entry.Base.BaseSocketMessage;
import com.result.base.tools.ObjectUtil;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月8日 下午12:00:37 
* 类说明 
*/
@Component
public class SocketMessage extends BaseSocketMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2211489538219792529L;

	private String title;

	private String id;

	private int number;

	private String param1;

	private String param2;

	private String param3;

	private String param4;

	private List<User> user;

	public SocketMessage() {
	}

	public SocketMessage(String title, String id, int number, String param1, String param2, String param3, String param4, List<User> user) {
		this.title = title;
		this.id = id;
		this.number = number;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "SocketMessage [serverUri=" + serverUri + ", title=" + title + ", id=" + id + ", number=" + number + ", param1="
				+ param1 + ", param2=" + param2 + ", param3=" + param3 + ", param4=" + param4 + ", User=" + (ObjectUtil.isNotNull(user)?user.toString():null)
				+ "]";
	}
	
}
