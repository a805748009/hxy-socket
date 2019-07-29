package nafos.core.helper;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import nafos.core.Thread.ThreadLocalHelper;
import nafos.core.annotation.http.RequestParam;
import nafos.bootStrap.handle.http.NsRequest;
import nafos.core.entry.HttpRouteClassAndMethod;
import nafos.bootStrap.handle.http.NsRespone;
import nafos.core.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午5:25
 * @Description TODO
 **/
public class RequestHelper {

    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);


    public static Object[] getRequestParams(NsRequest req, HttpRouteClassAndMethod route) {
        return getRequestParams(req, route, null);
    }

    public static Object[] getRequestParams(NsRequest nsRequest, HttpRouteClassAndMethod route, byte[] content) {
        Map<String, String> requestParams = nsRequest.getRequestParams();
        LinkedList linkedList = new LinkedList();
        for (Parameter parameter : route.getParameters()) {
            Object fieldObj = null;
            RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
            //url 参数
            if (ObjectUtil.isNotNull(requestParam)) {
                Object object = nsRequest.stringQueryParam("".equals(requestParam.value()) ? requestParam.name() : requestParam.value());
                if (ObjectUtil.isNull(object) && requestParam.required()) {
                    if (requestParam.required()) {
                        logger.error("======{},参数{}不能为空 ", route.getMethod().toString(), requestParam.value());
                        return null;
                    } else {
                        linkedList.add(null);
                        continue;
                    }
                }
                fieldObj = castClass(object, parameter.getType());
                linkedList.add(fieldObj);
                continue;
            }

            //request
            if (NsRequest.class.isAssignableFrom(parameter.getType())) {
                linkedList.add(nsRequest);
                continue;
            }

            //respone
            if (NsRespone.class.isAssignableFrom(parameter.getType())) {
                linkedList.add(ThreadLocalHelper.getRespone());
                continue;
            }

            if (ObjectUtil.isNull(content)) {
                //JSON传输的方法
                if (nsRequest.method() == HttpMethod.GET) {
                    if (Map.class.isAssignableFrom(parameter.getType())) {
                        linkedList.add(requestParams);
                    }
                } else {
                    //远程调用的restful-json处理
                    if (Map.class.isAssignableFrom(parameter.getType())) {
                        linkedList.add(nsRequest.getBodyParams());
                    } else {
                        //接受参数为实体类，json下直接json转（否则map无法转嵌套），fromdata用map转。
                        String strContentType = nsRequest.headers().get("Content-Type");
                        strContentType = ObjectUtil.isNotNull(strContentType) ? strContentType.trim() : "";
                        if (strContentType.contains("application/json")) {
                            ByteBuf jsonBuf = nsRequest.content();
                            String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
                            linkedList.add(JsonUtil.json2Object(jsonStr,parameter.getType()));
                        } else {
                            linkedList.add(BeanToMapUtil.mapToObject(nsRequest.getFormParams(), parameter.getType()));
                        }
                    }
                }
                continue;
            } else {
                getRequestParamsForByte(parameter, linkedList, content);
                continue;
            }

        }

        return linkedList.toArray();
    }


    public static void getRequestParamsForByte(Parameter parameter, LinkedList linkedList, byte[] content) {
        linkedList.add(ProtoUtil.deserializeFromByte(content, parameter.getType()));
    }


    /**
     * 把Object 变成对应的calssObj
     *
     * @param object
     * @param clazz
     * @return
     */
    private static Object castClass(Object object, Class<?> clazz) {

        if (clazz.equals(String.class))
            object = CastUtil.castString(object);

        if (clazz.equals(int.class) || clazz.equals(Integer.class))
            object = CastUtil.castInt(object);

        if (clazz.equals(boolean.class))
            object = CastUtil.castBoolean(object);

        if (clazz.equals(double.class))
            object = CastUtil.castDouble(object);

        if (clazz.equals(long.class))
            object = CastUtil.castLong(object);

        return object;
    }


    /**
     * @param
     * @return byte[]
     * @Author 黄新宇
     * @date 2018/7/4 下午4:05
     * @Description(获取request的参数，返回内容字节数组)
     */
    public static byte[] getRequestParamsObj(FullHttpRequest req) {
        byte[] payloadBytes = null;
        // 处理get请求
        if (req.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
            String params = decoder.parameters().get("params").get(0);
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
