package com.result.base.pool;

import com.result.base.config.ConfigForNettyMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger logger = LoggerFactory.getLogger(MyHttpRunnable.class);

	private ExecutorPool() {
	}
	
	// 单例模式
	private static ExecutorService instance = null;
	static {
		logger.debug("初始化线程池===========minThread:"+ ConfigForNettyMode.EXECUTORPOOLMINSIZE+"maxThread:"+ConfigForNettyMode.EXECUTORPOOLMAXSIZE);
		//模拟cache线程池，但是限制最大线程数以免撑爆服务器
		instance = new ThreadPoolExecutor(ConfigForNettyMode.EXECUTORPOOLMINSIZE, ConfigForNettyMode.EXECUTORPOOLMAXSIZE,
				2L, TimeUnit.MINUTES, 
				new SynchronousQueue<Runnable>(), 
				Executors.defaultThreadFactory(),new OutThreadPool());
	}

	public static ExecutorService getInstance() {
		return instance;
	}
}
