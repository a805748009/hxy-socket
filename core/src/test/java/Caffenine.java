import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import nafos.core.util.SnowflakeIdWorker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午3:46
 * @Description TODO
 **/
public class Caffenine {

    public static void main(String[] args) {


        ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<>();
        long startTime1 =System.currentTimeMillis();
        map.put("2121","121");
        for (int i =0;i<100000;i++){
            String key = SnowflakeIdWorker.getStringId();
            map.put(key,"22");
        }
        long endTime1=System.currentTimeMillis();
        System.out.println("       程序1耗时："+(endTime1-startTime1)+"ms");



        LoadingCache<String, Object> loadingCache = Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build(key -> jj());

        long startTime =System.currentTimeMillis();
        loadingCache.put("2121","121");
        for (int i =0;i<100000;i++){
                String key = SnowflakeIdWorker.getStringId();
                loadingCache.put(key,"22");
        }
        long endTime=System.currentTimeMillis();
        System.out.println("       程序耗时："+(endTime-startTime)+"ms");





        long startTime3 =System.currentTimeMillis();
        loadingCache.get("2121");
        long endTime3=System.currentTimeMillis();
        System.out.println("       程序3耗时："+(endTime3-startTime3)+"ms");

        long startTime2 =System.currentTimeMillis();
        map.get("2121");
        long endTime2=System.currentTimeMillis();
        System.out.println("       程序2耗时："+(endTime3-startTime3)+"ms");



    }

    public static String  jj(){
        return "222";
    }
}
