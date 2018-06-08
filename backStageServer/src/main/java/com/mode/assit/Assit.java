package com.mode.assit;

import com.result.base.tools.ConfigUtil;
import com.result.serverbootstrap.assist.proxy.socket.ServerDataValuesAndProxyRelationship;

import java.util.ResourceBundle;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月20日 下午1:58:38 
* 类说明 
*/
public class Assit {
	
	public void setAssit(){
		ResourceBundle bundle = ConfigUtil.getBundle("/httpConf/server.properties");
		new ServerDataValuesAndProxyRelationship(bundle);
	}
}
