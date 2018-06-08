package com.mode.tasks;

import com.mode.init.RedisInit;
import com.mode.init.RouteMapInit;
import com.result.base.task.StartAppTask;
import com.result.base.tools.SpringApplicationContextHolder;
import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月10日 上午10:19:17 
* 项目启动时运行
*/
@Component
public class StartTask implements StartAppTask {

	public void run() {
		//1）redis初始化
		SpringApplicationContextHolder.getContext().getBean(RedisInit.class).initRedisPool();
	}
}
