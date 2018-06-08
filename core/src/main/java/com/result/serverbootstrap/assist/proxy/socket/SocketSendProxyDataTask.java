package com.result.serverbootstrap.assist.proxy.socket;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.result.base.cache.IoCache;
import com.result.base.config.ConfigForSSL;
import com.result.base.config.ConfigForServerData;
import com.result.base.config.ConfigForSystemMode;
import com.result.base.enums.ConnectType;
import com.result.base.enums.ServerUrl;
import com.result.base.tools.HttpUtil;
import com.result.base.tools.SerializationUtil;
import com.result.serverbootstrap.assist.proxy.socket.entry.ProxyData;
import com.result.serverbootstrap.assist.proxy.socket.entry.ProxyHttpToSocketContact;
import com.result.serverbootstrap.assist.proxy.socket.entry.ServerData;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月19日 下午4:57:42 
* 向对应的Http服务定时发送自己的状态信息以供HTTP的负载 
*/
@Component
public class SocketSendProxyDataTask {
	private static final Logger logger = LoggerFactory.getLogger(HttpServerForReceiveSocketData.class);
	public static long index = 1l;

	@Scheduled(cron="0/10 * * * * ?")
	public void sendSocketData() throws IOException{
		if(ConfigForSystemMode.CONNECTTYPE.equals(ConnectType.SOCKET.getType())&&!ConfigForServerData.PROXYHTTPTOSOCKETCONTACTLIST.isEmpty()){
			for(ProxyHttpToSocketContact proxyHttpToSocketContact:ConfigForServerData.PROXYHTTPTOSOCKETCONTACTLIST){
				if(proxyHttpToSocketContact.getSocketServerName().equals(ConfigForServerData.SERVERNAME)){
					for(String serverName:proxyHttpToSocketContact.getHttpServerNameList()){
						for(ServerData serverData:ConfigForServerData.SERVERLIST.get(serverName)){
							long loadNum = 0l ;
							  Set<String> set = IoCache.spaceClientMap.keySet(); 
							  for (String nameSpace:set) {
								  loadNum+=IoCache.spaceClientMap.get(nameSpace).size();
							  }
							ProxyData proxyData = new ProxyData(ConfigForServerData.SERVEREXTRANETIP, ConfigForServerData.SERVERPORT,ConfigForSSL.ISOPENSSL,loadNum, ConfigForServerData.WEIGHT, index);
							String ip = serverData.isSSL()?"https://":"http://";
							ip+=serverData.getServerIntranetIp()+":"+serverData.getServerPort()+ServerUrl.PROXY.getUrl();
							logger.debug("=================>>>>>>准备发送数据");
							HttpUtil.binyPost(ip, SerializationUtil.serializeToByte(proxyData));
							index++;
						}
					}
				}
			}
		}
	}
}
