package com.business.controller;

import com.alibaba.fastjson.JSON;
import com.business.bean.PayOrder;
import com.business.bean.Shop;
import com.business.bean.comEntry.OrderInfo;
import com.business.cache.ShopCache;
import com.business.enums.GameType;
import com.business.enums.PayType;
import com.business.service.OrderService;
import com.business.service.PayService.MyAliPayService;
import com.business.service.PayService.MyIosPayService;
import com.business.service.ShopService;
import com.business.service.PayService.MyWxPayService;
import com.hxy.nettygo.result.base.tools.AESUtil;
import com.hxy.nettygo.result.base.tools.DateUtil;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.hxy.nettygo.result.base.tools.SnowflakeIdWorker;
import com.mode.error.MyHttpResponseStatus;
import com.hxy.nettygo.result.base.annotation.Nuri;
import com.hxy.nettygo.result.base.annotation.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.Random;

/**
 * @Author 黄新宇
 * @Date 2018/7/10 下午2:46
 * @Description TODO
 **/
@Route
@Nuri(uri="/pay")
public class PayController {
    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    ShopService shopService;

    @Autowired
    MyIosPayService myIosPayService;




    /**
    * @Author 黄新宇
    * @date 2018/7/10 下午2:48
    * @Description(创建订单)
    * @param
    * @return java.lang.Object
    */
    @Nuri(uri="/createOrder")
    public Object createOrder(OrderInfo orderInfo) throws Exception  {
        int shopId = orderInfo.getShopId();
        String payType = orderInfo.getPayType();
        //1.获取游戏类别生成orderId前三位标识符
        String gameNumber = null;
        GameType selfGameType = null;
        for (GameType gameType : GameType.values()) {
            if (gameType.getName().equals(orderInfo.getGameName())) {
                gameNumber = gameType.getNumber();
                selfGameType = gameType;
            }
        }
        //2.生成订单号 3+6+18+5
        String orderId = gameNumber+ DateUtil.getNowYYMMDD()+ SnowflakeIdWorker.getStringId()+new Random().nextInt(99999);
        //3.获取用户ID
        String userId = AESUtil.decrypt(orderInfo.getUserToken(),selfGameType.getKey());
        PayOrder payOrder = null;
        //4.新增订单
        payOrder = new PayOrder(orderId,userId,payType,0,shopId,DateUtil.getNowTime());
        int createOrderStatus = orderService.insertOrder(payOrder);
        if(createOrderStatus==0)
            return MyHttpResponseStatus.CREATE_ORDER_FAILED;

        orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        Shop shop = ShopCache.shopCacheMap.get(shopId);



        //----------苹果支付创建订单
        if(PayType.IOS.getType().equals(payType)){
            String iosPayId = shop.getIosPayId();
            orderInfo.setIosPayId(iosPayId);
            return orderInfo;
        }

        //----------微信生成预付单
        if(PayType.WX.getType().equals(payType)){
            Map<String,Object> payInfoMap = MyWxPayService.gePayService().unifiedOrder(orderId, shop);
            if(ObjectUtil.isNull(payInfoMap))
                return MyHttpResponseStatus.CREATE_ORDER_FAILED;
            orderInfo.setAliwxReturnInfo(JSON.toJSONString(payInfoMap));
            return orderInfo;
        }

        //----------阿里生成预付单
        if(PayType.ALI.equals(payType)){
            Map<String,Object> payInfoMap = MyWxPayService.gePayService().unifiedOrder(orderId, shop);
            orderInfo.setAliwxReturnInfo(JSON.toJSONString(payInfoMap));
            return orderInfo;
        }

        //----------华为||OPPO||小米
        if(PayType.HUAWEI.equals(payType)||PayType.OPPO.equals(payType)||PayType.XIAOMI.equals(payType)){
            orderInfo.setSubject(shop.getSubject());
            orderInfo.setBody(shop.getBody());
            orderInfo.setPrice(shop.getIsDiscount()?shop.getDiscountPrice():shop.getPrice());
            return orderInfo;
        }

        return MyHttpResponseStatus.CREATE_ORDER_FAILED;
    }


    /**
    * @Author 黄新宇
    * @date 2018/7/11 下午3:33
    * @Description(获取支付状态)
    * @param ( orderInfo)
    * @return java.lang.Object
    */
    @Nuri(uri="/getPayStatus")
    public Object payStatus(OrderInfo orderInfo)  {
        String orderId = orderInfo.getOrderId();
        PayOrder payOrder = orderService.getOrderById(orderId);
        orderInfo.setOrderId(null);
        //已经完成支付
        if(payOrder.getPayStatus()==1){
            orderInfo.setPayStatus(1);
            return orderInfo;
        }

        //苹果支付的
        if(PayType.IOS.getType().equals(payOrder.getPayType())){
            orderInfo.setPayStatus(myIosPayService.iosPayVerify(orderInfo.getReceiptData(),payOrder));
            return orderInfo;
        }

        orderInfo.setPayStatus(0);
        return orderInfo;
    }


    public static void main(String[] args) {
        Shop shop = new Shop(10001, "2123", true,1, false, 0, "测试1", "测试1");

        Map<String,Object> wxpayInfoMap = MyAliPayService.gePayService().unifiedOrder("217094701319702397109270", shop);

//        Map<String,Object> wxpayInfoMap = MyWxPayService.gePayService().unifiedOrder("217094701319702397109270", shop);
        System.out.println(JSON.toJSONString(wxpayInfoMap));

    }


}
