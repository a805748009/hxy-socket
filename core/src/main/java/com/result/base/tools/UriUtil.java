package com.result.base.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.util.Base64;
import com.result.base.config.ConfigForNettyMode;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;


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
	public static Map<String, String> getRequestParamsMap(FullHttpRequest req) throws IOException{
        Map<String, String>requestParams=new HashMap<>();
        // 处理get请求  
        if (req.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());  
            decoder.parameters().entrySet().forEach( entry -> {
                // entry.getValue()是一个List, 只取第一个元素
            	requestParams.put(entry.getKey(), entry.getValue().get(0));
            });
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
	 * 获取参数 ->BaseMessage
	 * @param <T>
	 * @param <T>
	 * @param req
	 * @return
	 * @throws IOException
	 */
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
               	   str = str.replaceAll("-", "+");
               	   str = str.replaceAll("_", "/");
               	   byte[] bt = Base64.decodeFast(str);
            	if(str!=null)
            	basemessage = SerializationUtil.deserializeFromByte(bt,clazz);
            }
        }
        return basemessage;
    }
	

	
}
