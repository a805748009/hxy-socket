package com.business.service.PayService;

import com.business.bean.Shop;
import com.business.cache.PayInfoCache;
import com.business.service.MyPayService;
import com.business.tools.EMailUtil;
import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.bean.PayOrder;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/7/11 上午10:16
 * @Description TODO
 **/
public class MyAliPayService extends AliPayService implements MyPayService {


    public MyAliPayService(PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    private static MyAliPayService payService = null;

    private static EMailUtil emailUtil = new EMailUtil();

    static{
        AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
        aliPayConfigStorage.setAppId(PayInfoCache.jsonObject.getJSONObject("ali").get("appId").toString());
        aliPayConfigStorage.setKeyPublic(PayInfoCache.jsonObject.getJSONObject("ali").get("publicKey").toString());
        aliPayConfigStorage.setKeyPrivate(PayInfoCache.jsonObject.getJSONObject("ali").get("privateKey").toString());
        aliPayConfigStorage.setNotifyUrl(PayInfoCache.jsonObject.getJSONObject("ali").get("notifyUrl").toString());
        aliPayConfigStorage.setSignType("RSA2");
        aliPayConfigStorage.setInputCharset("utf-8");
//      aliPayConfigStorage.setPid("合作者id");
//      aliPayConfigStorage.setSeller("收款账号");
//      aliPayConfigStorage.setReturnUrl("同步回调地址");
        //是否为测试账号，沙箱环境
//      aliPayConfigStorage.setTest(true);
        payService = new MyAliPayService(aliPayConfigStorage);
    }

    public static MyAliPayService gePayService(){
        return payService;
    }



    /**
     * @Author 黄新宇
     * @date 2018/7/10 下午5:51
     * @Description(统一下单)
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> unifiedOrder(String orderId, Shop shop) {
        PayOrder order = new PayOrder(shop.getSubject(), shop.getBody(),  new BigDecimal(shop.getPrice()/100) ,orderId, AliTransactionType.APP );
        Map<String, Object> data = this.orderInfo(order);
        return data;
    }

}
