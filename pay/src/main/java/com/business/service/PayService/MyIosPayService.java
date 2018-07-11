package com.business.service.PayService;

import com.alibaba.fastjson.JSONObject;
import com.business.bean.IosReceiptMd5;
import com.business.bean.PayOrder;
import com.business.bean.Shop;
import com.business.cache.PayInfoCache;
import com.business.cache.ShopCache;
import com.business.dao.IosReceiptMd5Mapper;
import com.business.tools.MD5Util;
import com.result.base.tools.CastUtil;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Author 黄新宇
 * @Date 2018/7/11 下午6:07
 * @Description TODO
 **/
@Component
public class MyIosPayService {
    @Autowired
    IosReceiptMd5Mapper iosReceiptMd5Mapper;

    /**
     * IOS二次验证
     */
    public int iosPayVerify(String receiptData,PayOrder payOrder) {
        String jsStr = buyAppVerify(receiptData, PayInfoCache.jsonObject.getJSONObject("ios").get("url_verify").toString());
        JSONObject xmlData = JSONObject.parseObject(jsStr);

        // 1.判断该订单返回状态
        if(CastUtil.castInt(xmlData.get("status"))==21007){
            //测试环境验证
            jsStr = buyAppVerify(receiptData,PayInfoCache.jsonObject.getJSONObject("ios").get("url_sandbox").toString());
            xmlData = JSONObject.parseObject(jsStr);
        }

        //2.判断是否支付成功，！0 失败
        if(CastUtil.castInt(xmlData.get("status"))!=0){
            return 0;
        }



        String productId = null;
        String transactionId = null;

        //ios7以上系统，返回格式
        if(jsStr.contains("in_app")){
            JSONArray json = JSONArray.fromObject(JSONObject.parseObject(xmlData.get("receipt").toString()).get("in_app").toString());
            net.sf.json.JSONObject inApp = json.getJSONObject(0);

            productId = inApp.get("product_id").toString();
        }else{
            //ios7以下返回格式
            productId = JSONObject.parseObject(xmlData.get("receipt").toString()).get("product_id").toString();
        }


//        //判断payId是否相等
        if(ShopCache.shopCacheMap.get(payOrder.getShopId()).getIosPayId().equals(productId)){
            iosReceiptMd5Mapper.insert(new IosReceiptMd5(MD5Util.MD5Encode(receiptData,"UTF-8"),payOrder.getOrderId()));
            return 1;
        }

        return 0;
    }



    /**
     * 苹果服务器验证
     *
     * @param receipt
     *            账单
     * @url 要验证的地址
     * @return null 或返回结果 沙盒 https://sandbox.itunes.apple.com/verifyReceipt
     *
     */
    public String buyAppVerify(String receipt,String url) {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/json");
            conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());

            String str = String.format(Locale.CHINA, "{\"receipt-data\":\"" + receipt + "\"}");
            hurlBufOus.write(str.getBytes());
            hurlBufOus.flush();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
