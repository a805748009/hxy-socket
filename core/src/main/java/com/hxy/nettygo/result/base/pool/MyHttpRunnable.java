package com.hxy.nettygo.result.base.pool;

import com.hxy.nettygo.result.base.config.ConfigForSystemMode;
import com.hxy.nettygo.result.base.entry.*;
import com.hxy.nettygo.result.base.handle.Crc32MessageHandle;
import com.hxy.nettygo.result.base.security.SecurityUtil;
import com.hxy.nettygo.result.base.tools.*;
import com.hxy.nettygo.result.base.handle.ZlibMessageHandle;
import com.hxy.nettygo.result.base.inits.InitMothods;
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
		String uri = request.method()+":"+ UriUtil.parseUri(request.uri());
	
		logger.debug( "uri---" + uri + " length:" + uri.length());
		//  0)前置filter
		RouteClassAndMethod filter;
		if(request.uri().length()>13&&request.uri().substring(0,13).equals(ConfigForSystemMode.REMOTE_CALL_URI)){
			filter = InitMothods.getRemoteCallFilter();
		}else{
			filter = InitMothods.getMessageFilter();
		}
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
		HttpRouteClassAndMethod route = InitMothods.getTaskHandler(uri);
		// 2)寻找路由失败
		if(route==null){
			NettyUtil.sendError(context, HttpResponseStatus.NOT_FOUND);
			return;
		}

		// 3）消息入口处理
		Object contentObj = null;
		try {
			contentObj = getMessageObjOnContent(route);
			if(ObjectUtil.isNull(contentObj))
				return;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 4)寻找路由成功,返回结果
		if(ObjectUtil.isNotNull(cookieId)){
			synchronized (cookieId){
				long startTime = 0;
				if(route.isPrintLog()){
					startTime=System.currentTimeMillis();
				}
				contentObj = routeMethod(route,contentObj);
				if(route.isPrintLog()){
					long endTime=System.currentTimeMillis();
					logger.info("sessionId:"+cookieId+"      方法："+route.getClazz().getName()+"."+route.getMethod().getMethodNames()[route.getIndex()]+"       程序耗时："+(endTime-startTime)+"ms");
				}
			}
		}else{
			long startTime = 0;
			if(route.isPrintLog()){
				startTime=System.currentTimeMillis();
			}
			contentObj = routeMethod(route,contentObj);
			if(route.isPrintLog()){
				long endTime=System.currentTimeMillis();
				logger.info("方法："+route.getClazz().getName()+"."+route.getMethod().getMethodNames()[route.getIndex()]+"       程序耗时："+(endTime-startTime)+"ms");
			}
		}



		// 5）发送处理
		sendMethod(route,contentObj);
	}
	
	
	/**
	* @Author 黄新宇
	* @date 2018/7/4 下午4:12
	* @Description(获取内容消息体)
	* @param
	* @return java.lang.Object
	*/
	private Object getMessageObjOnContent(HttpRouteClassAndMethod route) throws IOException {
		if("JSON".equals(route.getType())){
			return UriUtil.getRequestParamsForJson(request,route); //json传输方式 不支持任何处理，基本难用到
		}else{
			if(ObjectUtil.isNull(route.getParamType()))//不需要任何参数
				return false;
			byte[] content = UriUtil.getRequestParamsObj(request);
			if(ObjectUtil.isNull(content))
				return null;
			content = ZlibMessageHandle.unZlibByteMessage(content);//解压
			content = Crc32MessageHandle.checkCrc32IntBefore(content);//CRC32校验
			if(ObjectUtil.isNull(content))
				return null;
			return SerializationUtil.deserializeFromByte(content,route.getParamType());
		}
	}

	/**
	* @Author 黄新宇
	* @date 2018/7/4 下午4:36
	* @Description(路由)
	* @param
	* @return java.lang.Object
	*/
	private Object routeMethod (HttpRouteClassAndMethod route,Object object){
		try {
			if(object instanceof  Boolean){
				if(!(boolean)object){
					return route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
							new Object[]{});
				}
			}
			return route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
					route.isRequest()?new Object[]{object,request,context}:new Object[]{object});
		}catch (Exception e){
			e.printStackTrace();
			return HttpResponseStatus.INTERNAL_SERVER_ERROR;
		}
	}

	/**
	* @Author 黄新宇
	* @date 2018/7/4 下午4:38
	* @Description(发送后置处理器)
	* @param
	* @return void
	*/
	private void sendMethod (HttpRouteClassAndMethod route,Object object){
		try {

		if("JSON".equals(route.getType())){
			send(context, JsonUtil.toJson(object),HttpResponseStatus.OK);
		}else{
			//error处理
			if(object instanceof  HttpResponseStatus){
				NettyUtil.sendError(context, (HttpResponseStatus) object);
				return;
			}
			//如果回传为null，则直接返回
			if(object==null){
				send(context,null,HttpResponseStatus.OK);
				return;
			}
			//如果传回的不是byte，那么必定是bean。
			if(!(object instanceof  byte[])){
				object = SerializationUtil.serializeToByte(object);
			}
			byte[] bytes = Crc32MessageHandle.addCrc32IntBefore((byte[])object);
			bytes = ZlibMessageHandle.zlibByteMessage(bytes);
			send(context,bytes,HttpResponseStatus.OK);
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
