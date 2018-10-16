package nafos.core.task;

import nafos.core.monitor.SystemMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年2月8日 上午10:42:52 
* 类说明 
*/
@Component
public class NafosCoreScheduledTask {

	@Value("${nafos.monnitor.showSystem:false}")
	private boolean showSystem;
	
	private static Logger logger = LoggerFactory.getLogger(NafosCoreScheduledTask.class);



	@Scheduled(cron="0 0/1 * * * ?")
	public void systemMonitor() throws Exception {
	 	if(showSystem){
			logger.info("============开始打印系统信息日志============");
			Thread.currentThread().setName( "SystemMonitor");
			SystemMonitor.gcLog();
			SystemMonitor.memoryLog();
			SystemMonitor.threadLog();
		}
	}
}
