package com.mode.filter;

import com.business.entry.User;
import com.hxy.nettygo.result.base.cache.Client;
import com.hxy.nettygo.result.base.cache.NameSpace;
import com.hxy.nettygo.result.base.cache.UserClient;
import com.hxy.nettygo.result.base.entry.ResultStatus;
import com.hxy.nettygo.result.base.fitle.HttpMessageFilter;
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
public class MySocketHandShakeFilter implements HttpMessageFilter {

	@Override
	public ResultStatus filter(ChannelHandlerContext ctx, FullHttpRequest req) {
		ResultStatus resultStatus = new ResultStatus();

		//1. 封装client
		ctx.channel().attr(AttributeKey.valueOf("client")).set(new Client(ctx.channel()));

		//2. 获取token解码后的session
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.uri());
		Map<String, List<String>> parameters = queryStringDecoder.parameters();

		String sessionId = null;
		if(ObjectUtil.isNull(parameters.get("token").get(0))&&ObjectUtil.isNull(req.headers().get("token"))){//QQ玩一玩是放在headers中发送过来的
			resultStatus.setSuccess(false);
			resultStatus.setResponseStatus(MyHttpResponseStatus.TOKENERROR);
			return resultStatus;
		}
		try {
			sessionId = ObjectUtil.isNull(parameters.get("token").get(0))?AESUtil.decrypt(req.headers().get("token")):AESUtil.decrypt(parameters.get("token").get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 3.验证token
		if (ObjectUtil.isNull(sessionId) || sessionId.length()!=18||!SecurityUtil.isLogin(sessionId)) {
			resultStatus.setSuccess(false);
			resultStatus.setResponseStatus(MyHttpResponseStatus.TOKENERROR);
			return resultStatus;
		}

		// 4.token绑定到client
		ctx.channel().attr(AttributeKey.valueOf("token")).set(sessionId);

		// 5.设置命名空间
		ctx.channel().attr(AttributeKey.valueOf("nameSpace")).set(req.uri().substring(1, req.uri().indexOf("?")));
		NameSpace.inviteClient(req.uri().substring(1, req.uri().indexOf("?")),
				(Client) ctx.channel().attr(AttributeKey.valueOf("client")).get());

		// 6.设置userClient
		UserClient.setClient(SecurityUtil.getLoginUser(sessionId,User.class).getUserId(),
				(Client) ctx.channel().attr(AttributeKey.valueOf("client")).get());

		resultStatus.setSuccess(true);
		return resultStatus;
	}

}
