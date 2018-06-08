package com.result.base.pool;

import com.result.base.entry.*;
import com.result.base.inits.InitMothods;
import com.result.base.security.SecurityUtil;
import com.result.base.tools.*;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MyHttpRunnable implements Runnable {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(MyHttpRunnable.class);
	
	// 保存变量
	private ChannelHandlerContext context;
	private FullHttpRequest request;
	
	

	public MyHttpRunnable(ChannelHandlerContext ctx, FullHttpRequest req) {
		ReferenceCountUtil.retain(req);
		context = ctx;
		request = req;
	}
	
	

	@Override
	public void run() {
		//线程设置request
		ThreadLocalUtil.setSession(new GoHttpThread(new GoRequest(request)));
		// 根据URI来查找object.method.invoke
		String uri = request.method()+":"+UriUtil.parseUri(request.uri());
	
		logger.debug( "uri---" + uri + " length:" + uri.length());
		//  0)前置filter
		RouteClassAndMethod filter = InitMothods.getFilter();
		if(ObjectUtil.isNotNull(filter)){
			ResultStatus resultStatus =  (ResultStatus) filter.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(filter.getClazz()),filter.getIndex(),this.context,this.request);
			if(!resultStatus.isSuccess()){
				NettyUtil.sendError(context, resultStatus.getResponseStatus());
				return;
			}
		}
		String cookieId = ThreadLocalUtil.getRequest().getSecurityCookieId();
		//	1)登录验证
		if(SecurityUtil.isGoLogin(cookieId,UriUtil.parseUri(request.uri()))){
			NettyUtil.sendError(context, HttpResponseStatus.FORBIDDEN);
			return;
		}
		//	1.1)更新session存活时间
		SecurityUtil.updateSessionTime(cookieId);
		// 2)寻找路由失败
		HttpRouteClassAndMethod route = InitMothods.getTaskHandler(uri);
		if(route==null){
			NettyUtil.sendError(context, HttpResponseStatus.NOT_FOUND);
			return;
		}
		// 3)寻找路由成功,返回结果
		routeMethod(route);
	}
	
	
	
	
	
	/**
	 * 获取到路由之后的处理方法
	 * @param  route
	 */
	private void routeMethod (HttpRouteClassAndMethod route){
		try {
		if("JSON".equals(route.getType())){
			//这里不用newInstance,这样的话在action中是无法使用spring注解的，所以采用容器获取
			//Object object = route.getMethod().invoke(route.getClazz().newInstance(),new Object[]{UriUtil.getRequestParamsMap(request)});   
			
			Object object = route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
					route.isRequest()?new Object[]{UriUtil.getRequestParamsMap(request),request,context}:new Object[]{UriUtil.getRequestParamsMap(request)});
			//响应消息
			send(context,JsonUtil.toJson(object),HttpResponseStatus.OK);
		}else{
			//采用protobuf编解码
			Object object = route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
					route.isRequest()?new Object[]{UriUtil.getRequestParamsObj(request,route.getParamType()),request,context}:new Object[]{UriUtil.getRequestParamsObj(request,route.getParamType())});
			//error处理
			if(object instanceof  HttpResponseStatus){
				NettyUtil.sendError(context, (HttpResponseStatus) object);
			}
			//如果回传为null，则直接返回
			if(object==null){
				send(context,null,HttpResponseStatus.OK);
				return;
			}
			//如果传回的是byte[]，直接返回(和js以及其他语言传输，采用原生的prorobuf，这里直接返回btye)
			if(object instanceof  byte[]){
				send(context,object,HttpResponseStatus.OK);
				return;
			}
			//发送protostuff转码后的[]数组
			send(context,SerializationUtil.serializeToByte(object),HttpResponseStatus.OK);
		}
		} catch (IllegalArgumentException | IOException e) {
			NettyUtil.sendError(context, HttpResponseStatus.SERVICE_UNAVAILABLE);
			logger.error(e.toString());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
     * 发送的返回值
	 * @param <T> context
     * @param ctx     返回
     * @param context 消息
     * @param status 状态
	 * @throws UnsupportedEncodingException 
     */
    private <T> void send(ChannelHandlerContext ctx, T context,HttpResponseStatus status) throws UnsupportedEncodingException {
    	request.release();
//    	ByteBuf buffer = Unpooled.buffer(oo);
//    	byte[] req = new byte[buffer.readableBytes()];
    	FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
    	//设置允许跨域
    	response.headers().set("Access-Control-Allow-Origin", "*");
    	GoRespone sp =  ThreadLocalUtil.getRespone();
    	//设置cookie头
    	if(sp.getCookies().size()!=0){
    		response.headers().set(HttpHeaderNames.COOKIE, sp.getCookies()); //new DefaultCookie("jssonid", "123123")
    	}
    	//
        if(context instanceof byte[]){
	    	response.content().writeBytes((byte[])context);
	        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
        }else if(context instanceof String){
        	response.content().writeBytes(((String) context).getBytes());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-8");
        }else {
        	//为null的时候
        	response.content().writeBytes("".getBytes());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-8");
        }
		ThreadLocalUtil.threadLocalRemove();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    
}
