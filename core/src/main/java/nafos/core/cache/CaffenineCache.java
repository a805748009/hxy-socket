package nafos.core.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author 黄新宇
 * @Date 2018/8/9 下午4:07
 * @Description Caffenine缓存
 **/
public class CaffenineCache<K, V> {
    private final LoadingCache<K, V> caches;

    public CaffenineCache(long timeOut, TimeUnit unit, Supplier<? extends V> loading) {
        this.caches = Caffeine.newBuilder().expireAfterWrite(timeOut, unit).expireAfterAccess(timeOut, unit).build(key -> loading.get());
    }

    public V get(K key) {
        return caches.get(key);
    }

    public void del(K key) {
        caches.invalidate(key);
    }
}
