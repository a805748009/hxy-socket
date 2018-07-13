package com.hxy.nettygo.result.base.entry.backStageBean;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月8日 下午12:00:37 
* 类说明 
*/
public class BaseMqMessage {

	protected   String uri;

	public BaseMqMessage() {
	}

	public BaseMqMessage(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
