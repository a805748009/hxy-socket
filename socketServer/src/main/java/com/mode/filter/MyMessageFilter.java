package com.mode.filter;

import com.business.entry.User;
import com.result.base.cache.Client;
import com.result.base.cache.UserClient;
import com.result.base.config.ConfigForSystemMode;
import com.result.base.entry.ResultStatus;
import com.result.base.fitle.HttpMessageFilter;
import com.result.base.security.SecurityUtil;
import com.result.base.tools.AESUtil;
import com.result.base.tools.ObjectUtil;
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
public class MyMessageFilter implements HttpMessageFilter {

	@Override
	public ResultStatus filter(ChannelHandlerContext ctx, FullHttpRequest req) {
		ResultStatus resultStatus = new ResultStatus();

		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.uri());
		Map<String, List<String>> parameters = queryStringDecoder.parameters();

		String sessionId = null;
		if(ObjectUtil.isNull(parameters.get("token").get(0))){
			resultStatus.setSuccess(false);
			resultStatus.setResponseStatus(MyHttpResponseStatus.TOKENERROR);
			return resultStatus;
		}
		try {
			sessionId = AESUtil.decrypt(parameters.get("token").get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 1.1验证token
		if (ObjectUtil.isNull(sessionId) || sessionId.length()!=18||!SecurityUtil.isLogin(sessionId)) {
			resultStatus.setSuccess(false);
			resultStatus.setResponseStatus(MyHttpResponseStatus.TOKENERROR);
			return resultStatus;
		}

		UserClient.setClient(SecurityUtil.getLoginUser(sessionId,User.class).getUserId(),
				(Client) ctx.channel().attr(AttributeKey.valueOf("client")).get());
		resultStatus.setSuccess(true);

		return resultStatus;
	}

}
