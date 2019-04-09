package nafos.core.Thread;


import java.util.concurrent.*;

/**
 * 
 * @author huangxinyu
 * 
 * @version 创建时间：2018年1月4日 上午11:48:25 
 * 初始化线程池操作
 *
 */
public class ExecutorPool {
	private ExecutorPool() {
	}
	
	// 单例模式
	private static ExecutorService instance = null;
	static {
		//线程池最小2个，最大是cpu核数*2个线程
		instance = new ThreadPoolExecutor(Processors.getProcess()*2, 300,
				2L, TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>(500),new NamedThreadFactory("nafos"));
	}

	public static ExecutorService getInstance() {
		return instance;
	}
}
