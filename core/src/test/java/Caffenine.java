import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import nafos.core.cache.CaffenineCache;
import nafos.core.cache.LongAdderMap;
import nafos.core.util.SnowflakeIdWorker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;

/**
 * @Author 黄新宇
 * @Date 2018/10/9 下午3:46
 * @Description TODO
 **/
public class Caffenine {

    public static void main(String[] args) {

        CaffenineCache map = new CaffenineCache(5, TimeUnit.HOURS, () -> {
            System.out.println(1);return new LongAdder();});
//        Map map = new HashMap();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                ((LongAdder) map.get("10")).add(1);
                countDownLatch.countDown();
            }).start();

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(map.get("10"));
    }


}
