package com.mode.task;

import com.hxy.nettygo.result.base.task.StartAppTask;
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
//		RedisInit.initRedisPool();
		//3)
//		new Assit().setAssit();

//		SpringApplicationContextHolder.getContext().getBean(MqInit.class).initMQ();

		// 1)开启mq消息队列监听
//		MyQueueMessageListener.startListener();
	}
}
