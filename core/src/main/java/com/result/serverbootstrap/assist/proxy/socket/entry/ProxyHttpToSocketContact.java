package com.result.serverbootstrap.assist.proxy.socket.entry;

import java.util.List;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月20日 上午9:33:05 
* 服务器负载代理之间的关系类 ,HTTP==>Socket 之间的反向代理
* socketServerName对应的服务可以用哪些serverList（http）连接
*/
public class ProxyHttpToSocketContact {
	
	private String socketServerName;
	
	
	private List<String> httpServerNameList;

	

	public ProxyHttpToSocketContact() {
		super();
	}


	public ProxyHttpToSocketContact(String socketServerName, List<String> httpServerNameList) {
		super();
		this.socketServerName = socketServerName;
		this.httpServerNameList = httpServerNameList;
	}


	public String getSocketServerName() {
		return socketServerName;
	}


	public void setSocketServerName(String socketServerName) {
		this.socketServerName = socketServerName;
	}


	public List<String> getHttpServerNameList() {
		return httpServerNameList;
	}


	public void setHttpServerNameList(List<String> httpServerNameList) {
		this.httpServerNameList = httpServerNameList;
	}


	@Override
	public String toString() {
		return "ProxyHttpToSocketContact [socketServerName=" + socketServerName + ", httpServerNameList="
				+ httpServerNameList + "]";
	}
	
	


}
