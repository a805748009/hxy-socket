package nafos.core.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.*;
import java.util.List;

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
        MemoryMXBean mm = ManagementFactory.getMemoryMXBean();
        return mm.getHeapMemoryUsage().getUsed();
    }


    public static void allLog(){
        StringBuilder sb = new StringBuilder();
        sb.append("============开始打印系统信息日志============\n");
        sb.append( "                                                    " );
        sb.append(getGcLog()+"\n");
        sb.append( "                                                    " );
        sb.append(getMmemoryLog()+"\n");
        sb.append( "                                                    " );
        sb.append(getThreadLog()+"\n");
        logger.info(sb.toString());
    }

    public static void cronAllLog(long millisecond){
        new Thread(()->{
            Thread.currentThread().setName( "SystemMonitor");
            for(;;){
                try {
                    Thread.sleep(millisecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                allLog();
            }
        }).start();
    }

    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:56
    * @Description(GC监控)
    * @param
    * @return void
    */
    public static String getGcLog() {
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        String log = "";
        for (GarbageCollectorMXBean bean : beans) {
            log += "{"+bean.getName()+"} 发生 {"+bean.getCollectionCount()+"} 次 gc, gc 总共消耗 {"+bean.getCollectionTime()+"} 毫秒";
        }
        return log;
    }

    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:56
    * @Description(线程监控)
    * @param
    * @return void
    */
    public static String getThreadLog(){
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
        return "运行状态线程数：{"+run+"}, 阻塞状态线程数:{"+blocked+"}, 等待状态线程数：{"+waiting+"}";
    }


    /**
    * @Author 黄新宇
    * @date 2018/5/22 上午10:56
    * @Description(内存监控)
    * @param
    * @return void
    */
    public static String getMmemoryLog()  {
        Runtime runtime = Runtime.getRuntime();
        // 最大可用内存
        long maxMemory = runtime.maxMemory();
        // 已分配内存
        long totalMemory = runtime.totalMemory();
        // 已分配内存中的剩余空间
        long useMemory = totalMemory - runtime.freeMemory();
        // 最大可用内存
        long usableMemory = maxMemory - useMemory;
        return  "最大堆内存={"+maxMemory/1024/1024+"}, 已分配={"+totalMemory/1024/1024+"}, 已使用={"+useMemory/1024/1024+"}, 还可用={"+usableMemory/1024/1024+"}";
    }
}
