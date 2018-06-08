package com.result.serverbootstrap.assist.proxy.socket.entry;
/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月20日 上午9:33:05 
* 服务器信息
*/
public class ServerData {
	
	private String serverName;
	
	private String serverId;
	
	private String serverExtranetIp;//公网IP
	
	private String serverIntranetIp;//内网IP
	
	private int serverPort;
	
	private int weight;
	
	private boolean isSSL;
	
	private String connectType;//连接类型，socket,htp
	
	
	

	public ServerData() {
		super();
	}

	public ServerData(String serverName, String serverId, String serverExtranetIp, String serverIntranetIp,
			int serverPort, int weight, boolean isSSL, String connectType) {
		super();
		this.serverName = serverName;
		this.serverId = serverId;
		this.serverExtranetIp = serverExtranetIp;
		this.serverIntranetIp = serverIntranetIp;
		this.serverPort = serverPort;
		this.weight = weight;
		this.isSSL = isSSL;
		this.connectType = connectType;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerExtranetIp() {
		return serverExtranetIp;
	}

	public void setServerExtranetIp(String serverExtranetIp) {
		this.serverExtranetIp = serverExtranetIp;
	}

	public String getServerIntranetIp() {
		return serverIntranetIp;
	}

	public void setServerIntranetIp(String serverIntranetIp) {
		this.serverIntranetIp = serverIntranetIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean isSSL() {
		return isSSL;
	}

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	public String getConnectType() {
		return connectType;
	}

	public void setConnectType(String connectType) {
		this.connectType = connectType;
	}

	@Override
	public String toString() {
		return "ServerData [serverName=" + serverName + ", serverId=" + serverId + ", serverExtranetIp="
				+ serverExtranetIp + ", serverIntranetIp=" + serverIntranetIp + ", serverPort=" + serverPort
				+ ", weight=" + weight + ", isSSL=" + isSSL + ", connectType=" + connectType + "]";
	}
	
	
	
}
