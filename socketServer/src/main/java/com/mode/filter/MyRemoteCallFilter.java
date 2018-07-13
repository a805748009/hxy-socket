package com.mode.filter;

import com.hxy.nettygo.result.base.entry.ResultStatus;
import com.hxy.nettygo.result.base.fitle.RemoteCallFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月9日 下午6:22:13 
* 通信过滤 
*/
@Component
public class MyRemoteCallFilter implements RemoteCallFilter {

	@Override
	public ResultStatus filter(ChannelHandlerContext ctx, FullHttpRequest req) {
		ResultStatus resultStatus = new ResultStatus();
		resultStatus.setSuccess(true);
		return resultStatus;
	}

}
