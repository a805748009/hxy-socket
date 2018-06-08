package com.result.base.enums;
/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月12日 下午8:18:35 
* 类说明 
*/
public enum ServerUrl {
	
	PROXY("/nettgoProxy/saveSocketServerData","负载均衡的通知url");
	
	private String url;
	
	private String introduce;

	private ServerUrl(String url,String introduce) {
		this.url = url;
		this.introduce = introduce;
	}

	private ServerUrl() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	
	

}
