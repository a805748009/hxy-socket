package com.result.base.tools;

import com.result.base.entry.Base.BaseSocketMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月15日 下午8:39:09 
* 类说明 
*/
public class SendUtil {
	private static final Logger logger = LoggerFactory.getLogger(SendUtil.class);
    
    public static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
		// 返回应答给客户端
		if (res.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpHeaders.isKeepAlive(req) || res.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	public static Object castSendMsg(Object id,Object object){
    	//intBefore模式
		if(id instanceof byte[]){
			if(object instanceof byte[])
				return	ArrayUtil.concat((byte[])id,(byte[])object);
			return	ArrayUtil.concat((byte[])id,SerializationUtil.serializeToByte(object));
		}
		//textWebsocketFrame模式
		if(id instanceof String){
			if(object instanceof String)
				return	id+"|"+object;
			//PARENTFORBASESOCKETMESSAGE模式
			if(object instanceof BaseSocketMessage){
				((BaseSocketMessage) object).setClientUri((String)id);
				((BaseSocketMessage) object).setServerUri(null);
				return SerializationUtil.serializeToByte(object);
			}
			return id+"|"+JsonUtil.toJson(object);
		}
		logger.error("================>>>>>>传入参数错误");
    	return null;
	}
}
