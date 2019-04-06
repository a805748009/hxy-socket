package nafos.core.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Classname LongAdderMap
 * @Description TODO 线程安全的自加map
 * @Date 2019/2/28 18:55
 * @Created by xinyu.huang
 */
public class LongAdderMap<K, V> {
    private final LoadingCache<K, LongAdder> caches;

    public LongAdderMap() {
        this.caches = Caffeine.newBuilder().build(key -> new LongAdder());
    }

    public void incr(K k) {
        incr(k, 1);
    }

    public void decr(K k) {
        incr(k, -1);
    }

    public void incr(K k, long num) {
        caches.get(k).add(num);
    }

    public LongAdder get(K k) {
        return caches.get(k);
    }

    public void add(K k, long num) {
        incr(k, num);
    }

    public long getAndIncr(K k, long num) {
        long v = caches.get(k).longValue();
        incr(k, num);
        return v;
    }

    public long incrAndGet(K k, long num) {
        incr(k, num);
        return caches.get(k).longValue();
    }

    public ConcurrentMap<K, LongAdder> asMap() {
        return caches.asMap();
    }

    public void clear() {
        caches.invalidateAll();
    }


}
