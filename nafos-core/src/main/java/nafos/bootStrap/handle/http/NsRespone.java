package nafos.bootStrap.handle.http;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.util.AsciiString;
import nafos.core.util.AESUtil;
import nafos.core.util.SnowflakeIdWorker;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月25日 下午5:52:20
 * 类说明
 */
public class NsRespone extends DefaultFullHttpResponse {


    private List<Cookie> cookies = new ArrayList<Cookie>();//cookie

    public NsRespone(HttpVersion version, HttpResponseStatus status) {
        super(version, status);
    }

    public NsRespone setCookie(String key, String value) {
        cookies.add(new DefaultCookie(key, value));
        return this;
    }

    public NsRespone setNafosCookieId(String value) {
        cookies.add(new DefaultCookie("nafosCookie", URLEncoder.encode(AESUtil.encrypt(value))));
        return this;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public void setHeader(String key, String value) {
        headers().set(key, value);
    }
}
