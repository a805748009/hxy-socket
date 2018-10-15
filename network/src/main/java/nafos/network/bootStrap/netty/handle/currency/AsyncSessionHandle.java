package nafos.network.bootStrap.netty.handle.currency;

import nafos.core.Thread.ExecutorPool;
import nafos.core.cache.CaffenineCache;
import nafos.core.task.LineTask;
import nafos.core.task.TaskQueue;
import nafos.network.bootStrap.netty.handle.RouteRunnable;
import nafos.network.entry.RouteTaskQueue;

import java.util.concurrent.TimeUnit;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午4:19
 * @Description TODO
 **/
public class AsyncSessionHandle implements LineTask {
    private  static CaffenineCache<Integer,RouteTaskQueue> caffenineCache ;

    static{
        caffenineCache = new CaffenineCache<>(20, TimeUnit.SECONDS,()-> new RouteTaskQueue(ExecutorPool.getInstance()));
    }

    public static void runTask(Integer hashCode,RouteRunnable routeRunnable){
        ( caffenineCache.get(hashCode)).submit(routeRunnable);
    }

    public static RouteTaskQueue getTask(Integer hashCode){
       return  caffenineCache.get(hashCode);
    }

}
