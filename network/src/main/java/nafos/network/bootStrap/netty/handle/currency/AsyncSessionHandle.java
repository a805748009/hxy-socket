package nafos.network.bootStrap.netty.handle.currency;

import nafos.core.Thread.ExecutorPool;
import nafos.core.entry.AsyncTaskMode;
import nafos.core.task.LineTask;
import nafos.network.entry.RouteTaskQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午4:19
 * @Description TODO
 **/
public class AsyncSessionHandle implements LineTask {
//    private  static CaffenineCache<Integer,RouteTaskQueue> caffenineCache ;

    private final static Map<Integer,RouteTaskQueue> map = new ConcurrentHashMap<>();

//    static{
//        caffenineCache = new CaffenineCache<>(20, TimeUnit.SECONDS,()-> new RouteTaskQueue(ExecutorPool.getInstance()));
//    }

    public static void runTask(Integer hashCode, AsyncTaskMode asyncTaskMode){

        if(!map.containsKey(hashCode)){
            map.put(hashCode,new RouteTaskQueue(ExecutorPool.getInstance()));
        }
        map.get(hashCode).submit(asyncTaskMode);
//        caffenineCache.get(hashCode).submit(asyncTaskMode);
    }

    public static RouteTaskQueue getTask(Integer hashCode){
        if(!map.containsKey(hashCode))
            map.put(hashCode,new RouteTaskQueue(ExecutorPool.getInstance()));
       return  map.get(hashCode);
    }

}
