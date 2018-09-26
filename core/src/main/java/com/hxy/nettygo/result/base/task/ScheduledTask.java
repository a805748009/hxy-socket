package com.hxy.nettygo.result.base.task;

import com.hxy.nettygo.result.base.config.ConfigForSystemMode;
import com.hxy.nettygo.result.base.monitor.SystemMonitor;
import com.hxy.nettygo.result.base.security.CacheMapDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年2月8日 上午10:42:52 
* 类说明 
*/
@Component
public class ScheduledTask {
	
	private static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

	 @Scheduled(cron="0 0 4 * * ?") 
	    public void executeFileDownLoadTask() {
		 	CacheMapDao.delTimeOut();
	        logger.info("过时的session清除完毕============");
	    }

	@Scheduled(cron="0 0/1 * * * ?")
	public void systemMonitor() throws Exception {
	 	if(ConfigForSystemMode.IS_LOG_SYSTEM_MONITOR){
			Thread.currentThread().setName( "SystemMonitor");
			SystemMonitor.gcLog();
			SystemMonitor.memoryLog();
			SystemMonitor.threadLog();
		}
	}
}
