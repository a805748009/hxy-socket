package com.hxy.nettygo.result.base.monitor;

import com.hxy.nettygo.result.base.pool.ExecutorPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.*;
import java.util.List;
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
    * @param
    */
    public static long getMemoryUseForJvm(){
        MemoryMXBean mm =(MemoryMXBean) ManagementFactory.getMemoryMXBean();
        return mm.getHeapMemoryUsage().getUsed();
    }



    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:56
    * @Description(获取并发量)
    * @param
    * @return int
    */
    public static int getConcurrency(){
        return ((ThreadPoolExecutor) ExecutorPool.getInstance()).getActiveCount();
    }



    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:56
    * @Description(GC监控)
    * @param
    * @return void
    */
    public static void gcLog() throws Exception {
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean bean : beans) {
            logger.info("{} 发生 {} 次 gc, gc 总共消耗 {} 毫秒", bean.getName(), bean.getCollectionCount(), bean.getCollectionTime());
        }
    }

    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:56
    * @Description(线程监控)
    * @param
    * @return void
    */
    public static void threadLog() throws Exception {
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        int run = 0;
        int blocked = 0;
        int waiting = 0;
        for (long threadId : threadMXBean.getAllThreadIds()) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
            switch (threadInfo.getThreadState()) {
                case RUNNABLE:
                    run++;
                    break;
                // 受阻塞并等待某个监视器锁的线程处于这种状态
                case BLOCKED:
                    blocked++;
                    break;
                // 无限期地等待另一个线程来执行某一特定操作的线程处于这种状态。
                case WAITING:
                    waiting++;
                    break;
                default:
                    break;
            }
        }
        logger.info("运行状态线程数：{}, 阻塞状态线程数:{}, 等待状态线程数：{}", run, blocked, waiting);
    }


    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:56
    * @Description(内存监控)
    * @param
    * @return void
    */
    public static void memoryLog() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        // 最大可用内存
        long maxMemory = runtime.maxMemory();
        // 已分配内存
        long totalMemory = runtime.totalMemory();
        // 已分配内存中的剩余空间
        long useMemory = totalMemory - runtime.freeMemory();
        // 最大可用内存
        long usableMemory = maxMemory - useMemory;
        logger.info("最大堆内存={}, 已分配={}, 已使用={}, 还可用={}", maxMemory/1024/1024, totalMemory/1024/1024, useMemory/1024/1024, usableMemory/1024/1024);
    }
}
