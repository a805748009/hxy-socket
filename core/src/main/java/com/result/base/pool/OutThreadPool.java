package com.result.base.pool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author huangxinyu
 * 
 * @version 创建时间：2018年1月4日 上午11:47:33 
 * http超出线程池数量做丢弃处理
 *
 */
public class OutThreadPool implements RejectedExecutionHandler{
	private static final Logger logger = LoggerFactory.getLogger(OutThreadPool.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		logger.error("有新的请求超出线程池数量，已丢弃："+r.toString());
	}
	    
}
