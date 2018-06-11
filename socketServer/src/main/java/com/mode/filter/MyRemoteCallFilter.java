package com.mode.filter;

import com.business.entry.User;
import com.mode.error.MyHttpResponseStatus;
import com.result.base.cache.Client;
import com.result.base.cache.UserClient;
import com.result.base.config.ConfigForSystemMode;
import com.result.base.entry.ResultStatus;
import com.result.base.fitle.HttpMessageFilter;
import com.result.base.fitle.RemoteCallFilter;
import com.result.base.security.SecurityUtil;
import com.result.base.tools.AESUtil;
import com.result.base.tools.ObjectUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
