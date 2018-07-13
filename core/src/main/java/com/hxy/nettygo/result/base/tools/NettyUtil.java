package com.hxy.nettygo.result.base.tools;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.SERVER;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.hxy.nettygo.result.base.config.ConfigForServerRespone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;


public class NettyUtil {
	private static final Logger logger = LoggerFactory.getLogger(NettyUtil.class);

	public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		// 构造响应体
		JSONObject object = new JSONObject();
		object.put("error", status.code());
		object.put("status", status.reasonPhrase());
		// 设置到response对象
		final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer(object.toJSONString(), CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, ConfigForServerRespone.JSON);
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(SERVER, ConfigForServerRespone.SERVER);
		
		// 发送
		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static void sendSuccesstTest(ChannelHandlerContext ctx, HttpResponseStatus status, FullHttpRequest request) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
		// boolean keepAlive = HttpHeaders.isKeepAlive(request);
		// response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		// 设置header
		response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
		response.headers().set(CONNECTION, HttpHeaders.Values.CLOSE);
		// 设置body
		ByteBuf buffer = Unpooled.copiedBuffer("Success: " + status + "-" + System.currentTimeMillis() + "\r\n",
				CharsetUtil.UTF_8);
		response.content().writeBytes(buffer);
		buffer.release();
		// 发射&强制关闭连接
		ChannelFuture future = ctx.writeAndFlush(response);
		future.addListener(ChannelFutureListener.CLOSE);
		logger.debug( "handle http request end");
	}
	
	public static void sendOptions(ChannelHandlerContext ctx, HttpResponseStatus status) {
		// 构造响应体
		JSONObject object = new JSONObject();
		object.put("data", 1);
		object.put("status", status.toString());
		// 设置到response对象
		final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer(object.toJSONString(), CharsetUtil.UTF_8));
		//设置头部
		response.headers().set("Access-Control-Allow-Origin", "*");
    	response.headers().set("Access-Control-Allow-Headers", "content-type,GoSessionId");
    	response.headers().set("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
    	
		// 发送
		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
}
