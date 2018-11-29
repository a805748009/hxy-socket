package nafos.core.entry.http;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import nafos.core.util.AESUtil;
import nafos.core.util.ObjectUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月25日 下午6:44:44
 * 类说明
 */
public class NafosRequest {

    private FullHttpRequest request;

    private String securityCookieId = null;//登陆的cookieId

    private Set<Cookie> cookies = new HashSet<>();//cookieList

    private final String cookieStart = "nafosCookie";


    public NafosRequest(FullHttpRequest request) {
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


    public String getNafosCookieId() {

        //H5跨域不能设置cookie问题，暂用此方法解决
        if (ObjectUtil.isNotNull(request.headers().get(cookieStart))&&
                !request.headers().get(cookieStart).equals("undefined")&&
                !request.headers().get(cookieStart).equals("null")&&
                !request.headers().get(cookieStart).equals("[object Null]"))
        {
            try {
                securityCookieId = AESUtil.decrypt(request.headers().get(cookieStart));
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
            if (cookie.name().equals(cookieStart)) {
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



}
