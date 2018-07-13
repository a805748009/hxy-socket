package com.hxy.nettygo.result.base.entry;

import com.hxy.nettygo.result.base.tools.SerializationUtil;
import com.hxy.nettygo.result.base.redis.RedisUtil;
import com.hxy.nettygo.result.base.tools.AESUtil;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月25日 下午6:44:44
 * 类说明
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES)
public class GoRequest {

    private FullHttpRequest request;

    private String securityCookieId = null;//登陆的cookieId

    private Set<Cookie> cookies = new HashSet<>();//cookieList


    public GoRequest(FullHttpRequest request) {
        super();
        this.request = request;
    }


    public FullHttpRequest getRequest() {
        return request;
    }


    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }


    public Set<Cookie> getCookies() {
        String cookieStr = request.headers().get("Cookie");
        if (ObjectUtil.isNotNull(cookieStr)) {
            cookies = ServerCookieDecoder.LAX.decode(cookieStr);
        }
        return cookies;
    }


    public String getSecurityCookieId() {
        //H5跨域不能设置cookie问题，暂用此方法解决
        if (ObjectUtil.isNotNull(request.headers().get("GoSessionId")) && !request.headers().get("GoSessionId").equals("null")&&!request.headers().get("GoSessionId").equals("[object Null]")) {
            try {
                securityCookieId = AESUtil.decrypt(request.headers().get("GoSessionId"));
                return securityCookieId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //其他模拟器，安卓正常走流程
        getCookies();
        Iterator<Cookie> it = cookies.iterator();
        while (it.hasNext()) {
            Cookie cookie = it.next();
            if (cookie.name().equals("GoSessionId")) {
                try {
                    securityCookieId = AESUtil.decrypt(cookie.value());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return securityCookieId;
    }


    public <T> T getLoginUser(Class<T> cls) {
        getSecurityCookieId();
        Object obj = RedisUtil.get(securityCookieId);
        return SerializationUtil.deserializeFromString((String) obj, cls);
    }

}
