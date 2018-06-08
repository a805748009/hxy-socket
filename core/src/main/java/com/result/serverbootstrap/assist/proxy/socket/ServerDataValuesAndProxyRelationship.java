package com.result.serverbootstrap.assist.proxy.socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.result.base.config.ConfigForServerData;
import com.result.base.tools.CastUtil;
import com.result.serverbootstrap.assist.proxy.socket.entry.ProxyHttpToSocketContact;
import com.result.serverbootstrap.assist.proxy.socket.entry.ServerData;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月25日 下午3:32:43 
* 分布式，集群部署中  各服务记录，以及http》socket阶段负载均衡关系 
*/
public class ServerDataValuesAndProxyRelationship {
	
	private ResourceBundle bundle;
	
	public ServerDataValuesAndProxyRelationship(ResourceBundle bundle){
		this.bundle=bundle;
		setServeValues();
	}
	
	private void setServeValues(){
		ConfigForServerData.isSocketProxy = CastUtil.castBoolean(bundle.getString("isSocketProxy"));//是否开启socket服务的负载均衡
		String serverNames = bundle.getString("serverName");
		String serverIds =  bundle.getString("serverId");
		String serverExtranetIPs = bundle.getString("serverExtranetIP");
		String serverIntranetIPs = bundle.getString("serverIntranetIP");
		String serverPorts = bundle.getString("serverPort");
		String weights = bundle.getString("weight");
		String isSSLs = bundle.getString("isSSL");
		String connectTypes = bundle.getString("connectType");
		
		String hostName = bundle.getString("hostName");
		int hostId = CastUtil.castInt(bundle.getString("hostId"));
		
		String socketServerNames = bundle.getString("socketServerName");
		String proxyServerNames = bundle.getString("proxyServerName");
		
		//开始加载到ServerData,和本机信息
		String[] serverNameArray = serverNames.indexOf(",")==-1?new String[]{serverNames}:serverNames.split(",");
		List<ServerData> list = null;
		int i1 =0;
		for(String serverName:serverNameArray){
			String[] serverIdArray = serverIds.indexOf(";")==-1?new String[]{serverIds}:serverIds.split(";");
			 list = new ArrayList<>();
			int i2= 0;
			for(String serverId:serverIdArray[i1].indexOf(",")==-1?new String[]{serverIdArray[i1]}:serverIdArray[i1].split(",")){
				ServerData serverData = new ServerData();
				serverData.setServerName(serverName);
				serverData.setServerId(serverId);
				serverData.setServerExtranetIp(getValue(serverExtranetIPs,i1,i2));
				serverData.setServerIntranetIp(getValue(serverIntranetIPs,i1,i2));
				serverData.setServerPort(CastUtil.castInt(getValue(serverPorts,i1,i2)));
				serverData.setWeight(CastUtil.castInt(getValue(weights,i1,i2)));
				serverData.setSSL(CastUtil.castBoolean(getValue(isSSLs,i1,i2)));
				serverData.setConnectType(getValue(connectTypes,i1,i2));
				list.add(serverData);
				//本机的信息
				if(hostName.equals(serverName)&&serverId.equals(String.valueOf(hostId))){
					ConfigForServerData.SERVERNAME=serverName;
					ConfigForServerData.SERVERID=serverId;
					ConfigForServerData.SERVEREXTRANETIP=getValue(serverExtranetIPs,i1,i2);
					ConfigForServerData.SERVERINTRANETIP=getValue(serverIntranetIPs,i1,i2);
					ConfigForServerData.SERVERPORT=CastUtil.castInt(getValue(serverPorts,i1,i2));
					ConfigForServerData.WEIGHT=CastUtil.castInt(getValue(weights,i1,i2));
				}
			}
			ConfigForServerData.SERVERLIST.put(serverName, list);
			i1++;
		}
		//负载关系
		if(!ConfigForServerData.isSocketProxy)
			return;
		String[] socketServerNameArray = socketServerNames.indexOf(",")==-1?new String[]{socketServerNames}:socketServerNames.split(",");
		int i3 = 0;
		for(String socketServerName:socketServerNameArray){
			String[] proxyServerNameArray = (proxyServerNames.indexOf(";")==-1?new String[]{proxyServerNames}:proxyServerNames.split(";"))[i3]
					.indexOf(",")==-1?new String[]{ (proxyServerNames.indexOf(";")==-1?new String[]{proxyServerNames}:proxyServerNames.split(";"))[i3]}: 
						(proxyServerNames.indexOf(";")==-1?new String[]{proxyServerNames}:proxyServerNames.split(";"))[i3].split(",");
					ConfigForServerData.PROXYHTTPTOSOCKETCONTACTLIST.add(new ProxyHttpToSocketContact(socketServerName,Arrays.asList(proxyServerNameArray))) ;
			i3++;
		}
	}
	
	private  String getValue(String str,int i1, int i2){
		return(str.indexOf(";")==-1?new String[]{str}:str.split(";"))[i1]
				.indexOf(",")==-1?(str.indexOf(";")==-1?new String[]{str}:str.split(";"))[i1]:(str.indexOf(";")==-1?
						new String[]{str}:str.split(";"))[i1].split(",")[i2];
	}

}
