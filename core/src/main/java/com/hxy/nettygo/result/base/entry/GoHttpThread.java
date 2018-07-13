package com.hxy.nettygo.result.base.entry;
/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月26日 下午3:49:01 
* 类说明 
*/
public class GoHttpThread {

	private GoRequest goRequest;
	
	private GoRespone goRespone;

	public GoRequest getGoRequest() {
		return goRequest;
	}

	public void setGoRequest(GoRequest goRequest) {
		this.goRequest = goRequest;
	}

	public GoRespone getGoRespone() {
		return goRespone;
	}

	public void setGoRespone(GoRespone goRespone) {
		this.goRespone = goRespone;
	}

	public GoHttpThread(GoRequest goRequest) {
		super();
		this.goRequest = goRequest;
		this.goRespone = new GoRespone();
	}

	public GoHttpThread() {
		super();
	}
	
	
	
}
