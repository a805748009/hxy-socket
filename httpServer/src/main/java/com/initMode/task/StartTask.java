package com.initMode.task;

import com.alibaba.druid.pool.DruidDataSource;
import com.initMode.assit.Assit;
import com.initMode.assit.backStage.MqInit;
import com.initMode.assit.druidDataSource.DruidMonitorInit;
import com.result.base.task.StartAppTask;
import com.initMode.RedisInit;
import com.result.base.tools.SpringApplicationContextHolder;
import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月10日 上午10:19:17 
* 类说明 
*/
@Component
public class StartTask implements StartAppTask {

	public void run() {
		// 1) 设置公告
//		setNotice();
		//2）redis初始化
		SpringApplicationContextHolder.getContext().getBean(RedisInit.class).initRedisPool();
		//3) 初始化集群/分布式机器信息
//		new Assit().setAssit();
		//4) 初始化MQ
		SpringApplicationContextHolder.getContext().getBean(MqInit.class).initMQ();
		//5) 初始化对druid的监控，用于发送至自己后台
		new DruidMonitorInit().init();
	}
	
	


}
