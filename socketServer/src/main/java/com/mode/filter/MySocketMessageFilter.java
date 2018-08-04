package com.mode.filter;

import com.business.entry.User;
import com.hxy.nettygo.result.base.cache.Client;
import com.hxy.nettygo.result.base.cache.NameSpace;
import com.hxy.nettygo.result.base.cache.UserClient;
import com.hxy.nettygo.result.base.entry.ResultStatus;
import com.hxy.nettygo.result.base.fitle.HttpMessageFilter;
import com.hxy.nettygo.result.base.fitle.SocketMessageFilter;
import com.hxy.nettygo.result.base.security.SecurityUtil;
import com.hxy.nettygo.result.base.tools.AESUtil;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.mode.error.MyHttpResponseStatus;
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
public class MySocketMessageFilter implements SocketMessageFilter {

	@Override
	public ResultStatus filter(Client client, String  uri) {
		ResultStatus resultStatus = new ResultStatus();
		resultStatus.setSuccess(true);
		return resultStatus;
	}

}
