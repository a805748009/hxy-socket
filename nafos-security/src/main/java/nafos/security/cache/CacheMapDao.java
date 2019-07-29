package nafos.security.cache;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.security.SecurityUtil;
import nafos.security.config.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月29日 上午11:41:46
 * 登录的一级缓存 cache
 */
public class CacheMapDao {
    private static Logger logger = LoggerFactory.getLogger(CacheMapDao.class);

    private static Cache<String, Object> securityCache;

    private static Cache<String, Long> securityTimeCache;

    private static int sessionTimeout = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getSessionTimeOut();

    public static void init() {
        init(1, TimeUnit.HOURS, 1000);
    }

    public static void init(long timeOut, TimeUnit unit, int max) {
        securityCache = Caffeine.newBuilder().expireAfterWrite(timeOut, unit).expireAfterAccess(timeOut, unit).maximumSize(max).build();
        securityTimeCache = Caffeine.newBuilder().expireAfterWrite(timeOut, unit).expireAfterAccess(timeOut, unit).maximumSize(max).build();
    }

    private static Cache<String, Object> getSecurityCache() {
        if (securityCache == null) {
            synchronized (CacheMapDao.class) {
                if (securityCache == null) {
                    init();
                }
            }
        }
        return securityCache;
    }


    /**
     * 删除session和cache
     */
    public static void deleteCache(String sessionId) {
        getSecurityCache().invalidate(sessionId);
    }

    /**
     * 获取存活的Cache
     */
    public static Set<Object> getActiveCache() {
        return (Set<Object>) getSecurityCache().asMap().values();
    }


    /**
     * 获取cache
     */
    public static Object doReadCache(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        return getSecurityCache().getIfPresent(sessionId);
    }

    /**
     * 判断是否存在key
     */
    public static boolean exists(String sessionId) {
        if (sessionId == null) {
            return false;
        }
        return getSecurityCache().getIfPresent(sessionId) == null;
    }


    /**
     * 保存cache,session并存储过期时间
     *
     * @param sessionId
     * @param obj
     */
    public static void saveCache(String sessionId, Object obj) {
        if (obj == null) {
            logger.error("要存入的session-velue为空");
            return;
        }
        getSecurityCache().put(sessionId, obj);
    }

    public static void saveTimeCache(String sessionId, long time) {
        securityTimeCache.put(sessionId, time);
    }

    /**
     * 销毁
     */
    public static void destory() {
        getSecurityCache().invalidateAll();
        if (securityTimeCache != null) securityTimeCache.invalidateAll();
    }

    /**
     * 是否超出存活时间的4/5
     *
     * @param sessionId
     * @return
     */
    public static boolean isFourFifthsExpiryTime(String sessionId) {
        Long time = securityTimeCache.getIfPresent(sessionId);
        if (time == null) {
            SecurityUtil.getLoginUser(sessionId);
            time = securityTimeCache.getIfPresent(sessionId);
            if (time == null) {
                // 时间已经完全到期，加不了了返回false直接重新登录
                return false;
            }
        }
        return System.currentTimeMillis() - time > sessionTimeout * 4 / 5;

    }

}
