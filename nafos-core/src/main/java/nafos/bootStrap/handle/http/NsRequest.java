package nafos.bootStrap.handle.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import nafos.bootStrap.handle.http.BuildHttpObjectAggregator;
import nafos.core.util.AESUtil;
import nafos.core.util.CastUtil;
import nafos.core.util.JsonUtil;
import nafos.core.util.ObjectUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public class NsRequest extends BuildHttpObjectAggregator.AggregatedFullHttpRequest {

    private final String cookieStart = "nafosCookie";

    private String securityCookieId = null;//登陆的cookieId

    private Set<Cookie> cookies = new HashSet<>();//cookieList

    private Map<String, String> requestParams;

    private Map<String, Object> bodyParams;

    NsRequest(HttpRequest request, ByteBuf content, HttpHeaders trailingHeaders) {
        super(request, content, trailingHeaders);
    }


    public String stringQueryParam(String key) {
        return requestParams().get(key);
    }

    public Integer intQueryParam(String key) {
        String value = requestParams().get(key);
        if (value == null) return null;
        return Integer.valueOf(value);
    }

    public Boolean booleanQueryParam(String key) {
        String value = requestParams().get(key);
        if (value == null) return null;
        return Boolean.valueOf(value);
    }

    public Long longQueryParam(String key) {
        String value = requestParams().get(key);
        if (value == null) return null;
        return Long.valueOf(value);
    }


    public String stringBodyParam(String key) {
        Object value = bodyParams().get(key);
        if (value == null) return null;
        return String.valueOf(value);
    }

    public Integer intBodyParam(String key) {
        Object value = bodyParams().get(key);
        if (value == null) return null;
        return Integer.valueOf(value.toString());
    }

    public Boolean booleanBodyParam(String key) {
        Object value = bodyParams().get(key);
        if (value == null) return null;
        return Boolean.valueOf(value.toString());
    }

    public Long longBodyParam(String key) {
        Object value = bodyParams().get(key);
        if (value == null) return null;
        return Long.valueOf(value.toString());
    }


    /**
     * 获取cookie列表
     *
     * @return
     */
    public Set<Cookie> getCookies() {
        if (cookies.isEmpty()) {
            String cookieStr = headers().get("Cookie");
            if (ObjectUtil.isNotNull(cookieStr)) {
                cookies = ServerCookieDecoder.LAX.decode(cookieStr);
            }
        }
        return cookies;
    }

    public String getCookie(String name) {
        Iterator<Cookie> it = getCookies().iterator();
        while (it.hasNext()) {
            Cookie cookie = it.next();
            if (cookie.name().equals(name)) {
                return cookie.value();
            }
        }
        return null;
    }


    public String getNafosCookieId() {
        //H5跨域不能设置cookie问题，暂用此方法解决
        if (ObjectUtil.isNotNull(headers().get(cookieStart)) &&
                !headers().get(cookieStart).equals("undefined") &&
                !headers().get(cookieStart).equals("null") &&
                !headers().get(cookieStart).equals("[object Null]")) {
            try {
                securityCookieId = AESUtil.decrypt(URLDecoder.decode(headers().get(cookieStart)));
                return securityCookieId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String nafosCookieAes = getCookie(cookieStart);
        if(nafosCookieAes == null){
            return null;
        }
        //其他模拟器，安卓正常走流程
        securityCookieId = AESUtil.decrypt(URLDecoder.decode(nafosCookieAes));
        return securityCookieId;
    }


    /**
     * restful风格的postJSON解析
     */
    private Map<String, Object> bodyParams() {
        if (bodyParams == null) {
            // 处理POST请求
            String strContentType = headers().get("Content-Type");
            strContentType = ObjectUtil.isNotNull(strContentType) ? strContentType.trim() : "";
            if (strContentType.contains("application/json")) {
                bodyParams = getJSONParams();
            } else {
                bodyParams = getFormParams();
            }
        }
        return bodyParams;
    }


    /**
     * 解析from表单数据（Content-Type = x-www-form-urlencoded）,默认格式
     */
    public Map<String, Object> getFormParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), this);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }

    /**
     * 解析json数据（Content-Type = application/json）
     */
    public Map<String, Object> getJSONParams() {
        ByteBuf jsonBuf = content();
        String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
        if(jsonStr == null || jsonStr.trim().length() == 0) return new HashMap<>();
        return JsonUtil.jsonToMap(jsonStr);
    }


    /**
     * 解析queryparams
     *
     * @return
     */
    private Map<String, String> requestParams() {
        if (requestParams == null) {
            Map<String, String> map = new HashMap<>();
            QueryStringDecoder decoder = new QueryStringDecoder(uri());
            for (Map.Entry<String, List<String>> entry : decoder.parameters().entrySet()) {
                map.put(entry.getKey(), entry.getValue().get(0));
            }
            requestParams = map;
        }
        return requestParams;
    }

    /**
     * xml风格的postJSON解析
     *
     * @param clazz
     * @return
     */
    private Object xmlJsonEncode(Class<?> clazz) {
        Object fieldObj = null;
        Map<String, String> postMap = new HashMap<>();
        HttpPostRequestDecoder httpPostRequestDecoder = new HttpPostRequestDecoder(this);
        httpPostRequestDecoder.offer(this);
        List<InterfaceHttpData> parmList = httpPostRequestDecoder.getBodyHttpDatas();
        for (InterfaceHttpData parm : parmList) {
            Attribute data = (Attribute) parm;
            try {
                postMap.put(data.getName(), data.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Map.class.isAssignableFrom(clazz)) {
            try {
                BeanUtils.populate(fieldObj, postMap);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            fieldObj = postMap;
        }
        return fieldObj;
    }

    public Map<String, String> getRequestParams() {
        return requestParams();
    }

    public Map<String, Object> getBodyParams() {
        return bodyParams();
    }

}
