package com.result.serverbootstrap.assist.proxy.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.result.base.annotation.Nuri;
import com.result.base.annotation.Route;
import com.result.base.entry.Result;
import com.result.base.tools.SerializationUtil;
import com.result.serverbootstrap.assist.proxy.socket.entry.ProxyData;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月19日 下午3:14:24 
* 接收来自socket的报数，选择最优的服务
*/
@Route
@Nuri(uri="/nettgoProxy")
public class HttpServerForReceiveSocketData {
	private static final Logger logger = LoggerFactory.getLogger(HttpServerForReceiveSocketData.class);

	@Nuri(uri = "/saveSocketServerData", method = "POST")
	public Object saveSocketServerData(ProxyData proxyData) {
		long loadNum = proxyData.getLoadNumber();
		//如果是下一批次直接载入
		if(proxyData.getIndex()>ProxyNotes.getSocketSendIndex()){
			ProxyNotes.setSocketIpAndPort(proxyData.getIp()+":"+proxyData.getPort());
			ProxyNotes.setSocketLoadNum(proxyData.getLoadNumber());
			ProxyNotes.setSocketSendIndex(proxyData.getIndex());
		}else{
			logger.info(ProxyNotes.getSocketIpAndPort()+"---"+ProxyNotes.getSocketLoadNum()+"---"+ProxyNotes.getSocketSendIndex());
			//同批次的承载量小于记录的，记录最新
			if(loadNum<ProxyNotes.getSocketLoadNum()){
				String ip = proxyData.isSSL()?"wss://":"ws://";
				ip+=proxyData.getIp()+":"+proxyData.getPort();
				ProxyNotes.setSocketIpAndPort(ip);
				ProxyNotes.setSocketLoadNum(proxyData.getLoadNumber());
				ProxyNotes.setSocketSendIndex(proxyData.getIndex());
			}
		}
		return SerializationUtil.serializeToByte(new Result(true));
	}
}
