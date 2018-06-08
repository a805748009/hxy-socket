package com.mode.filter;

import com.mode.error.MyHttpResponseStatus;
import com.result.base.entry.ResultStatus;
import com.result.base.fitle.HttpMessageFilter;
import com.result.serverbootstrap.shutdown.Shutdown;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月9日 下午6:22:13 
* 通信过滤 
*/
@Component
public class Filter implements HttpMessageFilter {

	public ResultStatus filter(ChannelHandlerContext ctx, FullHttpRequest req) {
		ResultStatus resultStatus = new ResultStatus();
		// 0) 系统正在维护
		if(Shutdown.isShutdown){
			resultStatus.setSuccess(false);
			resultStatus.setResponseStatus(MyHttpResponseStatus.SERVERSHUTDOWN);
		}
		
		resultStatus.setSuccess(true);
		return resultStatus;
	}

}
