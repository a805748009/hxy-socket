package com.result.base.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.result.base.security.CacheMapDao;

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
	
}
