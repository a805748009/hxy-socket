package nafos.core.helper;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import nafos.core.annotation.controller.Request;
import nafos.core.annotation.controller.RequestParam;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.mode.RouteFactory;
import nafos.core.util.*;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午5:25
 * @Description TODO
 **/
public class RequestHelper {

	private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);




	public static Object[] getRequestParams(FullHttpRequest req, HttpRouteClassAndMethod route,byte[] content) {
		return getRequestParams(req,route,content,false);
	}

	public static Object[] getRequestParams(FullHttpRequest req, HttpRouteClassAndMethod route) {
		return getRequestParams(req,route,null,true);
	}

	public static Object[] getRequestParams(FullHttpRequest req, HttpRouteClassAndMethod route,byte[] content,boolean isProtoGet) {
		Map<String, String>requestParams = decodeUriToMap(req);
		LinkedList linkedList = new LinkedList();
		for (Parameter parameter : route.getParameters()) {
			Object fieldObj = null;
			RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
			if(ObjectUtil.isNotNull(requestParam)){
				Object object = requestParams.get(requestParam.value());
				if(ObjectUtil.isNull(object)&&requestParam.required()){
					if(requestParam.required()){
						logger.error("======{},参数{}不能为空 ",route.getMethod().toString(),requestParam.value());
						return null;
					}else{
						linkedList.add(null);
						continue;
					}
				}
				fieldObj = requestParams.get(requestParam.value());
				fieldObj = castClass(fieldObj,parameter.getType());
				linkedList.add(fieldObj);
				continue;
			}

			Request request = parameter.getDeclaredAnnotation(Request.class);
			if(ObjectUtil.isNotNull(request)){
				linkedList.add(req);
				continue;
			}

			if(!isProtoGet){
				if(ObjectUtil.isNull(content)){
					getRequestParamsForJson(req,requestParams,parameter,linkedList,fieldObj);
					continue;
				}else{
					getRequestParamsForByte(parameter ,linkedList,content);
					continue;
				}
			}

		}

		return linkedList.toArray();
	}

	/**
	 * 获取参数 ->  JSON方式
	 * @return
	 * @throws IOException
	 */
	public static void getRequestParamsForJson(FullHttpRequest req,Map<String, String>requestParams, Parameter parameter ,LinkedList linkedList,Object fieldObj) {
			//GET请求
			if (req.method() == HttpMethod.GET) {
				if(Map.class.isAssignableFrom(parameter.getType())){
					linkedList.add(requestParams);
				}else{
					try {
						BeanUtils.populate(fieldObj, requestParams);
						linkedList.add(fieldObj);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				return;
			}

			// 处理POST请求
			if (req.method() == HttpMethod.POST) {
				//远程调用的restful-json处理
				linkedList.add(restfulJsonEncode(req,parameter.getType()));
				return;
			}
    }


	public static void getRequestParamsForByte(Parameter parameter ,LinkedList linkedList,byte[] content){
		linkedList.add(ProtoUtil.deserializeFromByte(content,parameter.getType()));
	}

	/**
	 * uri的参数转map
	 * @param req
	 * @return
	 */
	private static Map<String,String> decodeUriToMap(FullHttpRequest req){
		Map<String, String> requestParams = new HashMap<>();
		QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
		for(Map.Entry<String, List<String>> entry:decoder.parameters().entrySet()){
			requestParams.put(entry.getKey(), entry.getValue().get(0));
		}
		return requestParams;
	}


	/**
	 * restful风格的postJSON解析
	 * @param request
	 * @param clazz
	 * @return
	 */
	private static Object restfulJsonEncode(FullHttpRequest request,Class<?> clazz){
		// 处理POST请求
		String strContentType = request.headers().get("Content-Type").trim();
		if (strContentType.contains("application/json")) {
			return getJSONParams(request,clazz);
		} else{
			return getFormParams(request,clazz);
		}
	}


	/**
	 * 解析from表单数据（Content-Type = x-www-form-urlencoded）,默认格式
	 */
	private static Object getFormParams(FullHttpRequest fullHttpRequest,Class<?> clazz) {
		Map<String, Object> params = new HashMap<String, Object>();
		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
		List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
		for (InterfaceHttpData data : postData) {
			if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
				MemoryAttribute attribute = (MemoryAttribute) data;
				params.put(attribute.getName(), attribute.getValue());
			}
		}
		if(Map.class.isAssignableFrom(clazz)){
			return params;
		}else{
			return BeanToMapUtil.mapToObject(params,clazz);
		}

	}

	/**
	 * 解析json数据（Content-Type = application/json）
	 */
	private static Object getJSONParams(FullHttpRequest req,Class<?> clazz) {
		ByteBuf jsonBuf = req.content();
		String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
		if(Map.class.isAssignableFrom(clazz)){
			return JsonUtil.jsonToMap(jsonStr);
		}else{
			return JsonUtil.json2Object(jsonStr,clazz);
		}
	}



	/**
	 * xml风格的postJSON解析
	 * @param req
	 * @param clazz
	 * @return
	 */
	private static Object xmlJsonEncode(FullHttpRequest req,Class<?> clazz){
		Object fieldObj = null;
		Map<String, String> postMap =new HashMap<>();
		HttpPostRequestDecoder httpPostRequestDecoder = new HttpPostRequestDecoder(req);
		httpPostRequestDecoder.offer(req);
		List<InterfaceHttpData> parmList = httpPostRequestDecoder.getBodyHttpDatas();
		for (InterfaceHttpData parm : parmList) {
			Attribute data = (Attribute) parm;
			try {
				postMap.put(data.getName(), data.getValue());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!Map.class.isAssignableFrom(clazz)){
			try {
				BeanUtils.populate(fieldObj, postMap);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}else{
			fieldObj = postMap;
		}
		return fieldObj;
	}

	/**
	 * 把Object 变成对应的calssObj
	 * @param object
	 * @param clazz
	 * @return
	 */
    private static Object castClass(Object object,Class<?> clazz){

		if(clazz.equals(String.class))
			object = CastUtil.castString(object);

		if(clazz.equals(int.class)||clazz.equals(Integer.class))
			object = CastUtil.castInt(object);

		if(clazz.equals(boolean.class))
			object = CastUtil.castBoolean(object);

		if(clazz.equals(double.class))
			object = CastUtil.castDouble(object);

		if(clazz.equals(long.class))
			object = CastUtil.castLong(object);

		return object;
	}




    /**
    * @Author 黄新宇
    * @date 2018/7/4 下午4:05
    * @Description(获取request的参数，返回内容字节数组)
    * @param
    * @return byte[]
    */
	public static byte[] getRequestParamsObj(FullHttpRequest req) {
		byte[] payloadBytes = null;
		// 处理get请求
		if (req.method() == HttpMethod.GET) {
			QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
			String params = (String)decoder.parameters().get("params").get(0);
			try {
				payloadBytes = params.getBytes("ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 处理POST请求
		if (req.method() == HttpMethod.POST) {
			//采用byte数组
			//获取http中body的字节数组
			payloadBytes = new byte[req.content().readableBytes()];
			req.content().readBytes(payloadBytes);
		}
		return payloadBytes;
	}




}
