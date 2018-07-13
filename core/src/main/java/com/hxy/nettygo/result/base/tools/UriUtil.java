package com.hxy.nettygo.result.base.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.util.Base64;
import com.hxy.nettygo.result.base.config.ConfigForNettyMode;
import com.hxy.nettygo.result.base.config.ConfigForSystemMode;
import com.hxy.nettygo.result.base.handle.ZlibMessageHandle;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;


public class UriUtil {

	/**
	 * 截取uri
	 * @param uri
	 * @return
	 */
	public static String parseUri(String uri) {
		// 1)null
		if (null == uri) {
			return null;
		}
		// 2)截断?
		int index = uri.indexOf("?");
		if (-1 != index) {
			uri = uri.substring(0, index);
		}
		return uri;
	}
	
	/**
	 * 获取参数 ->MAP
	 * @param req
	 * @return
	 * @throws IOException
	 */
	public static Map<String, ?> getRequestParamsMap(FullHttpRequest req) throws IOException{
		if (req.uri().length()>13&&req.uri().substring(0, 13).equals(ConfigForSystemMode.REMOTE_CALL_URI)) {
			//远程调用的restful-json处理
			ByteBuf jsonBuf = req.content();
			String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
			return JsonUtil.jsonToMap(jsonStr);
		}
        Map<String, String>requestParams=new HashMap<>();
        // 处理get请求  
        if (req.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
            for(Map.Entry<String, List<String>> entry:decoder.parameters().entrySet()){
				requestParams.put(entry.getKey(), entry.getValue().get(0));
			}
        }
         // 处理POST请求  
        if (req.method() == HttpMethod.POST) {
        	// 是POST请求
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
            decoder.offer(req);
            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData parm : parmList) {
                Attribute data = (Attribute) parm;
                requestParams.put(data.getName(), data.getValue());
            }
        }
        return requestParams;
    }
	
	
	/**
	 * 获取参数 ->BaseMessage  新版本已废弃,请使用 byte[] getRequestParamsObj(FullHttpRequest req);
	 * @param <T>
	 * @param <T>
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static <T> T getRequestParamsObj(FullHttpRequest req,Class<T> clazz) throws IOException{
		 T basemessage = null;
        // 处理get请求  
        if (req.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());  
            String params = (String)decoder.parameters().get("params").get(0);
            basemessage =  SerializationUtil.deserializeFromString(params, clazz);
        }
         // 处理POST请求  
        if (req.method() == HttpMethod.POST) {
            //采用byte数组
            if("BYTE".equals(ConfigForNettyMode.ENCODEDTYPE)){
            	//获取http中body的字节数组
            	byte[] payloadBytes = new byte[req.content().readableBytes()];
            	req.content().readBytes(payloadBytes);
            	//解压
				payloadBytes = ZlibMessageHandle.unZlibByteMessage(payloadBytes);
				//转码
            	basemessage = SerializationUtil.deserializeFromByte(payloadBytes,clazz);
            }
            //如果前端采用BASE64
            if("BASE64".equals(ConfigForNettyMode.ENCODEDTYPE)){
            	   HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
                   decoder.offer(req);
                   List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
               	if(parmList.isEmpty())return null;
               	   Attribute data = (Attribute) parmList.get(0);
               	   String str = data.getValue();
				if(ObjectUtil.isNotNull(str)) {
					str = str.replaceAll("-", "+");
					str = str.replaceAll("_", "/");
					byte[] bt = Base64.decodeFast(str);
					basemessage = SerializationUtil.deserializeFromByte(bt, clazz);
				}
            }
        }
        return basemessage;
    }


    /**
    * @Author 黄新宇
    * @date 2018/7/4 下午4:05
    * @Description(获取request的参数，返回内容字节数组)
    * @param [req]
    * @return byte[]
    */
	public static byte[] getRequestParamsObj(FullHttpRequest req) throws IOException{
		byte[] payloadBytes = null;
		// 处理get请求
		if (req.method() == HttpMethod.GET) {
			QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
			String params = (String)decoder.parameters().get("params").get(0);
			payloadBytes = params.getBytes("ISO8859-1");
		}
		// 处理POST请求
		if (req.method() == HttpMethod.POST) {
			//采用byte数组
			if("BYTE".equals(ConfigForNettyMode.ENCODEDTYPE)){
				//获取http中body的字节数组
				payloadBytes = new byte[req.content().readableBytes()];
				req.content().readBytes(payloadBytes);

			}
			//如果前端采用BASE64
			if("BASE64".equals(ConfigForNettyMode.ENCODEDTYPE)){
				HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
				decoder.offer(req);
				List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
				if(parmList.isEmpty())return null;
				Attribute data = (Attribute) parmList.get(0);
				String str = data.getValue();
				if(ObjectUtil.isNull(str))
					return null;
				str = str.replaceAll("-", "+");
				str = str.replaceAll("_", "/");
				payloadBytes = Base64.decodeFast(str);
			}
		}
		return payloadBytes;
	}
	

	
}
