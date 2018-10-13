package nafos.core.helper;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.core.mode.RouteFactory;
import nafos.core.util.FastJson;
import nafos.core.util.GsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午5:25
 * @Description TODO
 **/
public class RequestHelper {



	/**
	 * 获取参数 ->  JSON方式
	 * @param req
	 * @return
	 * @throws IOException
	 */
	public static Object getRequestParamsForJson(FullHttpRequest req, HttpRouteClassAndMethod route) throws IOException {
		Object obj = null;
		if (req.uri().length()>16&&req.uri().substring(0,16).equals(RouteFactory.REMOTE_CALL_URI)) {
			//远程调用的restful-json处理
			ByteBuf jsonBuf = req.content();
			String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
			if(Map.class.isAssignableFrom(route.getParamType())){
				obj = GsonUtil.gsonToMap(jsonStr);
			}else{
				obj = FastJson.getJsonToBean(jsonStr,route.getParamType());
			}
			return obj;
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
		if(!Map.class.isAssignableFrom(route.getParamType())){
			try {
				BeanUtils.populate(obj, requestParams);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}else{
			obj = requestParams;
		}
        return obj;
    }




    /**
    * @Author 黄新宇
    * @date 2018/7/4 下午4:05
    * @Description(获取request的参数，返回内容字节数组)
    * @param
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
			//获取http中body的字节数组
			payloadBytes = new byte[req.content().readableBytes()];
			req.content().readBytes(payloadBytes);
		}
		return payloadBytes;
	}


	//如果前端采用BASE64
//			if("BASE64".equals(ConfigForNettyMode.ENCODEDTYPE)){
//		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
//		decoder.offer(req);
//		List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
//		if(parmList.isEmpty())return null;
//		Attribute data = (Attribute) parmList.get(0);
//		String str = data.getValue();
//		if(ObjectUtil.isNull(str))
//			return null;
//		str = str.replaceAll("-", "+");
//		str = str.replaceAll("_", "/");
//		payloadBytes = Base64.decodeFast(str);
//	}


}
