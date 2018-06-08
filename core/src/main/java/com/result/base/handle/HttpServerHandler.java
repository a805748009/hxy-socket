package com.result.base.handle;


import com.result.base.pool.ExecutorPool;
import com.result.base.pool.MyHttpRunnable;
import com.result.base.tools.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * 
 * @author huangxinyu
 * 
 * @version 创建时间：2018年1月4日 上午11:51:28 
 * 连接处理类
 *
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (!(msg instanceof FullHttpRequest)) {
			NettyUtil.sendError(ctx, BAD_REQUEST);
			return;
		}
		
		FullHttpRequest request = (FullHttpRequest) msg;
		if (!request.decoderResult().isSuccess()) {
			NettyUtil.sendError(ctx, BAD_REQUEST);
			return;
		}
		
		
		//1）跨域方法之前会先收到OPTIONS方法，直接确认
		if (request.method() == OPTIONS) {
			NettyUtil.sendOptions(ctx, OK);
			return;
		}
		
		// 2)确保方法是我们需要的(目前只支持GET | POST  ,其它不支持)
		if (request.method() != GET && request.method() != POST) {
			NettyUtil.sendError(ctx, METHOD_NOT_ALLOWED);
			return;
		}
		// 3)uri是有长度的
		final String uri = request.uri();
		if (uri == null || uri.trim().length() == 0) {
			NettyUtil.sendError(ctx, FORBIDDEN);
			return;
		}
		// 4)一切就绪，准备抛给线程池,这里将线程池交给spring管理，可以共享spring容器
		MyHttpRunnable runnable = new MyHttpRunnable(ctx, request);
		ExecutorPool.getInstance().execute(runnable);
		// 其它的就交给线程池了 :)
    }
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error(cause.toString());
		cause.printStackTrace();
		ctx.close();
	}

}
