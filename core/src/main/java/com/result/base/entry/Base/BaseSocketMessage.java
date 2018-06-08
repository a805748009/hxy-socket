package com.result.base.entry.Base;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月8日 下午12:00:37 
* 类说明 
*/
public class BaseSocketMessage {

	protected   String clientUri;
	protected   String serverUri;

	public BaseSocketMessage() {
	}

	public BaseSocketMessage(String clientUri, String serverUri) {
		this.clientUri = clientUri;
		this.serverUri = serverUri;
	}

	public String getClientUri() {
		return clientUri;
	}

	public void setClientUri(String clientUri) {
		this.clientUri = clientUri;
	}

	public String getServerUri() {
		return serverUri;
	}

	public void setServerUri(String serverUri) {
		this.serverUri = serverUri;
	}
}
