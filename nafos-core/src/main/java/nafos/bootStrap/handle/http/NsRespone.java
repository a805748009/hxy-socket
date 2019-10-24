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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月25日 下午5:52:20
 * 类说明
 */
public class NsRespone extends DefaultFullHttpResponse {

    /**
     * cookie
     */
    private List<Cookie> cookies = new ArrayList<Cookie>();

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

    /**
     * @Desc 返回文件
     * @Param filePath  要返回的文件路径
     * @Param fileName  返回文件在前端的名字显示
     * @Author hxy
     * @Time 2019/10/24 12:19
     */
    public NsRespone returnFile(String filePath, String fileName) {
        File file = new File(filePath);
        this.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        this.setHeader(HttpHeaderNames.CONTENT_TYPE.toString(), "multipart/form-data");
        this.setStatus(HttpResponseStatus.OK);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            this.content().writeBytes(in.getChannel(), 0L, in.available());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * @Desc     重定向
     * @Author   hxy
     * @Time     2019/10/24 13:07
     */
    public NsRespone returnRedirect(String path){
        this.setHeader("location",path);
        this.setStatus(HttpResponseStatus.FOUND);
        return this;
    }
}
