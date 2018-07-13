package com.mode.task;

import com.business.bean.Shop;
import com.business.cache.ShopCache;
import com.business.dao.ShopMapper;
import com.mode.assit.backStage.MqInit;
import com.mode.assit.druidDataSource.DruidMonitorInit;
import com.mode.init.RedisInit;
import com.result.base.task.StartAppTask;
import com.result.base.tools.SpringApplicationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月10日 上午10:19:17 
* 类说明 
*/
@Component
public class StartTask implements StartAppTask {

	@Autowired
	ShopMapper shopMapper;


	public void run() {
		// 1) 设置公告
//		setNotice();
		//2）redis初始化
//		SpringApplicationContextHolder.getContext().getBean(RedisInit.class).initRedisPool();
		//4) 初始化MQ
		SpringApplicationContextHolder.getContext().getBean(MqInit.class).initMQ();
		//5) 初始化对druid的监控，用于发送至自己后台
		new DruidMonitorInit().init();
		initShopCache();
	}


	//初始化shop
	public void initShopCache(){
		List<Shop> shopList = shopMapper.selectList();
		shopList.forEach(shop -> {
			ShopCache.shopCacheMap.put(shop.getShopId(),shop);
		});
	}
	


}
