package nafos.core.task;

import nafos.core.monitor.RunWatch;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年2月8日 上午10:42:52 
* 类说明 
*/
@Component
public class NafosCoreScheduledTask {

	@Scheduled(cron="0 0 1 ? * MON")
	public void runWatchMonitor() {
		ArrayList<RunWatch.RunInfo> infoList =  RunWatch.info();
		RunWatch.addCurWeekInfo(infoList);
		RunWatch.resetInfo();
	}

}
