package com.result.base.monitor;

import com.result.base.pool.ExecutorPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author 黄新宇
 * @Date 2018/5/22 上午9:57
 * @Description 系统监控
 **/
public class SystemMonitor {

    private static final Logger logger = LoggerFactory.getLogger(SystemMonitor.class);

    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:21
    * @Description(获取内存使用)
    * @param []
    */
    public static long getMemoryUseForJvm(){
        MemoryMXBean mm =(MemoryMXBean) ManagementFactory.getMemoryMXBean();
        return mm.getHeapMemoryUsage().getUsed();
    }

    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:56
    * @Description(获取并发量)
    * @param []
    * @return int
    */
    public static int getConcurrency(){
        return ((ThreadPoolExecutor) ExecutorPool.getInstance()).getActiveCount();
    }
}
