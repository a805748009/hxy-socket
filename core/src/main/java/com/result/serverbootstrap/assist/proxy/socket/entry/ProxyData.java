package com.result.serverbootstrap.assist.proxy.socket.entry;

import java.io.Serializable;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月19日 下午3:05:28 
* 代理机信息
*/
public class ProxyData implements Serializable{
	
	private static final long serialVersionUID = -2825126962989119264L;

	private String ip;
	
	private int port;
	
	private boolean isSSL;
	
	private long loadNumber;//负载连接数
	
	private int weight;//权重
	
	private long index;//第几次发送

	public ProxyData(String ip, int port, boolean isSSL, long loadNumber, int weight, long index) {
		super();
		this.ip = ip;
		this.port = port;
		this.isSSL = isSSL;
		this.loadNumber = loadNumber;
		this.weight = weight;
		this.index = index;
	}

	public ProxyData() {
		super();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSSL() {
		return isSSL;
	}

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	public long getLoadNumber() {
		return loadNumber;
	}

	public void setLoadNumber(long loadNumber) {
		this.loadNumber = loadNumber;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}
	
}
