package com.business.service.PayService;

import com.alibaba.fastjson.JSONObject;
import com.business.bean.Shop;
import com.business.cache.PayInfoCache;
import com.business.tools.EMailUtil;
import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.wx.api.WxPayConfigStorage;
import com.egzosn.pay.wx.api.WxPayService;
import com.egzosn.pay.wx.bean.WxTransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author 黄新宇
 * @Date 2018/7/10 下午5:51
 * @Description TODO
 **/
public class MyWxPayService extends WxPayService  {
    private static final Logger logger = LoggerFactory.getLogger(MyWxPayService.class);

    private static MyWxPayService payService = null;

    private static EMailUtil emailUtil = new EMailUtil();

    public MyWxPayService(PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    static{
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setMchId(PayInfoCache.jsonObject.getJSONObject("wx").get("mchId").toString());
        wxPayConfigStorage.setAppid(PayInfoCache.jsonObject.getJSONObject("wx").get("appId").toString());
        wxPayConfigStorage.setSecretKey(PayInfoCache.jsonObject.getJSONObject("wx").get("secretKey").toString());
        wxPayConfigStorage.setNotifyUrl(PayInfoCache.jsonObject.getJSONObject("wx").get("notifyUrl").toString());
        wxPayConfigStorage.setSignType("MD5");
        wxPayConfigStorage.setInputCharset("utf-8");
//      wxPayConfigStorage.setReturnUrl("http://47.96.10.28:3001/pay/wxPay");
//      wxPayConfigStorage.setKeyPublic("转账公钥，转账时必填");
        payService = new MyWxPayService(wxPayConfigStorage);
    }


    public static MyWxPayService gePayService(){
        return payService;
    }


    /**
     * @Author 黄新宇
     * @date 2018/7/10 下午5:51
     * @Description(统一下单)
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> unifiedOrder(String orderId,Shop shop) {
        PayOrder order = new PayOrder(shop.getSubject(), shop.getBody(),  new BigDecimal(shop.getPrice()) ,orderId,WxTransactionType.APP );
        Map<String, Object> data = this.orderInfo(order);
        return data;
    }


    public Map<String, Object> orderInfo(PayOrder order) {
        JSONObject result = this.unifiedOrder(order);
        if (this.verify(result)) {
            if (WxTransactionType.NATIVE != order.getTransactionType() && WxTransactionType.MICROPAY != order.getTransactionType() && WxTransactionType.MWEB != order.getTransactionType()) {
                SortedMap<String, Object> params = new TreeMap();
                if (WxTransactionType.JSAPI == order.getTransactionType()) {
                    params.put("signType", this.payConfigStorage.getSignType());
                    params.put("appId", this.payConfigStorage.getAppid());
                    params.put("timeStamp", System.currentTimeMillis() / 1000L);
                    params.put("nonceStr", result.get("nonce_str"));
                    params.put("package", "prepay_id=" + result.get("prepay_id"));
                } else if (WxTransactionType.APP == order.getTransactionType()) {
                    params.put("partnerid", this.payConfigStorage.getPid());
                    params.put("appid", this.payConfigStorage.getAppid());
                    params.put("prepayid", result.get("prepay_id"));
                    params.put("timestamp", System.currentTimeMillis() / 1000L);
                    params.put("noncestr", result.get("nonce_str"));
                    params.put("package", "Sign=WXPay");
                }
                String paySign = this.createSign(SignUtils.parameterText(params), this.payConfigStorage.getInputCharset());
                params.put("sign", paySign);
                return params;
            } else {
                return result;
            }
        } else {
            emailUtil.setTitle("微信支付异常通知邮件");
            emailUtil.setContent(result.getString("return_msg"));
            emailUtil.SendEMailMsg();
            logger.error("=======>>>>>>>>>获取预付支付异常，"+result.getString("return_msg"));
            return null;
        }
    }
}
