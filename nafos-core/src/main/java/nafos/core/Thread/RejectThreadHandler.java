package nafos.core.Thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 
 * @author huangxinyu
 * 
 * @version 创建时间：2018年1月4日 上午11:47:33 
 * http超出线程池数量做丢弃处理
 *
 */
public class RejectThreadHandler implements RejectedExecutionHandler{
	private static final Logger logger = LoggerFactory.getLogger(RejectThreadHandler.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		ExecutorPoolv2.getInstance().execute(r);
		logger.debug("有新的请求超出线程池v1数量，已转移至v2线程池");
	}
	    
}
