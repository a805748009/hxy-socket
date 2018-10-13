package nafos.core.util;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月10日 上午11:26:32
 * 类说明
 */
public class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    private static final String CHARSET = "UTF-8";

    /**
     * Post请求
     *
     * @param url     地址
     * @param content 内容
     */
    public static String httpPost(String url, String content, String mime) {

        String resultStr = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            if (!StringUtils.isEmpty(content)) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(content, CHARSET);
                entity.setContentEncoding(CHARSET);
                if (StringUtils.isEmpty(mime)) {
                    entity.setContentType("application/x-www-form-urlencoded");
                } else {
                    entity.setContentType(mime);
                }
                post.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(post);
            if (result.getStatusLine().getStatusCode() == HttpResponseStatus.OK.code()) {
                resultStr = EntityUtils.toString(result.getEntity(), CHARSET);
            }
        } catch (Exception e) {
            LOGGER.error("post请求提交失败:" + url, e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("httpClient 关闭异常", e);
            }
        }
        return resultStr;
    }

    /**
     * Get请求
     *
     * @param url 地址带拼接参数
     */
    public static String httpGet(String url) {
        String resultStr = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpResponseStatus.OK.code()) {
                resultStr = EntityUtils.toString(response.getEntity(), CHARSET);
            }
        } catch (Exception e) {
            LOGGER.error("get请求提交失败:" + url, e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("httpClient关闭异常", e);
            }
        }
        return resultStr;
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return
     */
    public static String sendGet(String url) {

        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送HttpPost请求，参数为map
     *
     * @param url
     * @param map
     * @return
     */
    public static String mapPost(String url, Map<String, String> map) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity1 = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity1);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送HttpPost请求，参数为map
     *
     * @param url
     * @param map
     * @return byte[]
     */
    public static byte[] sendPostReturnByte(String url, Map<String, String> map) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity1 = response.getEntity();
        byte[] result = null;
        try {
            result = EntityUtils.toByteArray(entity1);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送不带参数的HttpPost请求
     *
     * @param url
     * @return
     */
    public static String sendPost(String url) {
        HttpPost httppost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 发送二进制post
     *
     * @param url
     * @return
     */
    public static byte[] binyPost(String url, byte[] bytes, String contentType) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new ByteArrayEntity(bytes));
        if (contentType != null)
            httpPost.setHeader("Content-type", contentType);
        CloseableHttpResponse httpResponse = HttpClients.createDefault().execute(httpPost);
        try {
            HttpEntity entityResponse = httpResponse.getEntity();
            int contentLength = (int) entityResponse.getContentLength();
            if (contentLength <= 0)
                return null;
//	                throw new IOException("No response");  
            byte[] respBuffer = new byte[contentLength];
            if (entityResponse.getContent().read(respBuffer) != respBuffer.length)
                throw new IOException("Read response buffer error");
            return respBuffer;
        } finally {
            httpResponse.close();
        }
    }

    public static byte[] binyPost(String url, byte[] bytes) throws IOException {
        return binyPost(url, bytes, null);
    }


}
