package com.hxy.nettygo.result.base.fitle;

import com.hxy.nettygo.result.base.entry.ResultStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月9日 下午3:45:14 
* http请求过来的前置处理器
*/
public interface RemoteCallFilter {
	
	ResultStatus filter(ChannelHandlerContext ctx, FullHttpRequest req);

}
