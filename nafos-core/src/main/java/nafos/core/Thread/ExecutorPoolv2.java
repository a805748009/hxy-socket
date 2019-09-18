package nafos.core.Thread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author huangxinyu
 * 
 * @version 创建时间：2018年1月4日 上午11:48:25 
 * 初始化线程池操作
 *
 */
public class ExecutorPoolv2 {
	private ExecutorPoolv2() {
	}
	
	/**
	 * @Desc     单例模式
	 * @Author   hxy
	 * @Time     2019/9/18 18:16
	 */
	private static ExecutorService instance = null;
	static {
		//线程池最小2个，最大是cpu核数*2个线程
		instance = new ThreadPoolExecutor(4, 300,
				2L, TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>(500),new NamedThreadFactory("nafosV2"));
	}

	public static ExecutorService getInstance() {
		return instance;
	}
}
