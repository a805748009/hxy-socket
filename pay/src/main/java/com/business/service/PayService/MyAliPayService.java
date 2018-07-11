package com.business.service.PayService;

import com.business.bean.Shop;
import com.business.tools.EMailUtil;
import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.wx.bean.WxTransactionType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author 黄新宇
 * @Date 2018/7/11 上午10:16
 * @Description TODO
 **/
public class MyAliPayService extends AliPayService {

    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr541p91wDT7Prp1XQ0UI1zdT2acNkcROnH8UHOkFntFrMAn8jlHVl8DVWlacDDZ9QUPg/1k3VuKtz/R2FWsFZyXlb6DZ3WIYKoelwdg6D2ZAOrh9UGCUr6ZSlf/+7WzdchdyB7iApK8HOvEGXirMjjQN0U9taPu4bo+i+n2Jmxvnxb4lO/E3lhUElUAdLHUFCWYkk7OsgqFq/eXazJpVYPbejXlMtE7pcv65lX2AsrS/mwIfPdbY6dzB40Yy5eKm5atm47UB6GYPKIZGfF3qlmItKOQgmVynr/2ItlWINFuO4BO9q5+C8HiCtIpU8VMEYAt5gft/h0mK1IWwQQF4ZwIDAQAB";

    public static String prvateKey =
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCuzUKQQwQKbTHj\n" +
            "FSwqGZc3NxfFgmBkAI8LyP/wx1MPlCe6cRyE/kVg/c2gaEggrFQQCKf7WJLUCtDi\n" +
            "3LdYxbw71ob/by5kjJPhFXQ+ne+UrajRQI/4TaaAeFzBYNHR1PDCCKcZU9KCTKSH\n" +
            "0zpcuettRmINj/CkvRG31w79jb7haMoOjybntb5XNmpWD0BUtiKuiF7AZjtENwzd\n" +
            "JLUwaPwxkurUBPcEPKWFMUHY/SptibmoLLEwfKti2U5K0x0CVaJRgfcK+IY74sGf\n" +
            "czyrI9xUQ03d4SwlsOHL64uml4Ll5HR5zdZ66RPJdCjeUEcFrJrKbjLV1AqTyQ65\n" +
            "leKWGlahAgMBAAECggEAe3lIHIkKEKdjRsmM4cwj0X/cDcrzdZAjeyqnB5h0ppt7\n" +
            "mKoPgC3wKfv9xOIEmPfBhXXn2paNWLSvKqDH7i/2r7nwr/wEJEhcqPCzpQuA0V/9\n" +
            "0JmvWLDY/QSIWJygeXzV9+mOx0g7eYjT65GcNyWpFA5UcsoLLQLUwixqTVZTz/o+\n" +
            "jvQHM3YLoWxnAdD4RaT5AvfABcjibvqX4CFnFfIkOH32sNQom138bTVfdvu+RJKp\n" +
            "QMRpE0mmtvldibxzupZoCF4PJC1KnE2Dc1/JwZ5uULuydXQSTCwddKmkOF/jLmYT\n" +
            "W+aDqMOBSQm6qPg7kQdu/HS/z5dZMZKg6pxK0VXr1QKBgQDkTpO4x6UYZtEvv0Oy\n" +
            "XnOAonbHDb4Ul80CgXWvipU+qX74mB513qW4NKDhd3Q9I6+WK+NeTtTTzqKY1e5y\n" +
            "thL7B1Eo7kmWb/PhV3NEHQ8BDw+7SvDzViVR62U7K5RcTsRtN6KOpu2+5WIUD7Om\n" +
            "gXFci1kJszwxRMyvhvb/VKWqdwKBgQDEATua2suC/3m8uw+bLNyx6yZlfl8LGJ6U\n" +
            "7Lp09vCW49YJdKC/Ka2wifumLE66VVgE0b5HSWfFho3ep421gzM6FbGj3ftcxd2z\n" +
            "iH1GOh/DFMq0yvF2RnckTaXCMZWe4I51SIpSTOITaxXZT8Ed0be6j7B2q1aEyd5p\n" +
            "xnmV5g61pwKBgQCDx2c0zOfagd5UtQ2aMzJwGVZ95szEjipjO952CIPEu4jybLuI\n" +
            "RY9aF+aUvaJ2d9at039VvCLUbv0jQrhzteyWwM5z1bbcApq+gv+hXTR/4/WsocEa\n" +
            "c6ru8fRohusHrN9sOyS1Ol0YRsap1bZYcXbBeB1YoAAqYA/VcQ24q19EWQKBgQCq\n" +
            "/XxXMqq6BE/agsIxmGiL2l2ryxv1HSwzNXXQViE7MVXm99TID/8TdLyRkjO4QH3B\n" +
            "7ox1uicKXcDkTf3FLMkC0iwfjaccnw0y805+dnSBBmF0aVtO2FhV1ltV11X0vWfq\n" +
            "DzeEZsgPMqtj4jzR28PRwEqsq/qVa1pZoqktSP7qHwKBgQCIim+FLLQjQhT468Qf\n" +
            "E6ZMg6WQ/6a5T7b6oO5slPT1YClyFtLKc68/hFfEhSQr/DQag4FPHaJM0m8L8IpI\n" +
            "clisK1ngNtRD6fopZc0xAKVfzTkkUHGBxJEeGrVVIeXmxOPaxFm09PnhOJaT8VQB\n" +
            "aT30U7hrTYi10ZyL3AZTGS59pw==\n" ;

    public MyAliPayService(PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    private static MyAliPayService payService = null;

    private static EMailUtil emailUtil = new EMailUtil();

    static{
        AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
//        aliPayConfigStorage.setPid("合作者id");
        aliPayConfigStorage.setAppId("2018060160286553");
        aliPayConfigStorage.setKeyPublic(publicKey);
        aliPayConfigStorage.setKeyPrivate(prvateKey);
        aliPayConfigStorage.setNotifyUrl("http://47.96.10.28:3001/pay/alicPay");
//        aliPayConfigStorage.setReturnUrl("同步回调地址");
        aliPayConfigStorage.setSignType("RSA2");
//        aliPayConfigStorage.setSeller("收款账号");
        aliPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
//        aliPayConfigStorage.setTest(true);
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
    public Map<String, Object> unifiedOrder(String orderId, Shop shop) {
        PayOrder order = new PayOrder(shop.getSubject(), shop.getBody(),  new BigDecimal(shop.getPrice()/100) ,orderId, AliTransactionType.APP );
        Map<String, Object> data = this.orderInfo(order);
        System.out.println("====="+data);
        return data;
    }
}
