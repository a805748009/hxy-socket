package com.hxy.nettygo.result.base.fitle;

import com.hxy.nettygo.result.base.cache.Client;
import com.hxy.nettygo.result.base.entry.ResultStatus;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月9日 下午3:45:14 
* Socket请求过来的前置处理器
*/
public interface SocketMessageFilter {
	
	ResultStatus filter(Client client, String uri);

}
