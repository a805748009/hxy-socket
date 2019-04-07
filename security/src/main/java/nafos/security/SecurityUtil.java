package nafos.security;

import nafos.core.entry.ClassAndMethod;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.InitMothods;
import nafos.core.util.CastUtil;
import nafos.core.util.ObjectUtil;
import nafos.core.util.ProtoUtil;
import nafos.security.cache.CacheMapDao;
import nafos.security.cache.RedisSessionDao;
import nafos.security.config.SecurityConfig;
import nafos.security.redis.RedisKey;
import nafos.security.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月25日 下午9:41:34
 * 安全操作工具类
 */
public class SecurityUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private static boolean isUseRedis;

    static {
        isUseRedis = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIsUseRedis();
    }


    /**
     * 获取登录用户
     *
     * @param sessionId
     * @param cls
     * @return
     */
    public static <T> T getLoginUser(String sessionId, Class<T> cls) {
        Object obj = CacheMapDao.doReadCache(sessionId);
        if (ObjectUtil.isNotNull(obj)) {
            return (T) obj;
        } else {
            if (!isUseRedis)
                return null;
            obj = RedisSessionDao.doReadSession(sessionId);
            if (ObjectUtil.isNotNull(obj)) {
                T t = ProtoUtil.deserializeFromString((String) obj, cls);
                CacheMapDao.saveCache(sessionId, t);
                return t;
            }
            return null;
        }
    }

    /**
     * 设置登录用户
     *
     * @param sessionId
     * @param obj
     */
    public static void setLoginUser(String sessionId, Object obj) {
        CacheMapDao.saveCache(sessionId, obj);
        if (isUseRedis)
            RedisSessionDao.saveSession(sessionId, obj);
    }

    /**
     * 获取本地机器存活的session
     *
     * @param <T>
     */
    public static <T> Collection<T> getBaseActiveLoginUser(Class<T> cls) {
        Set<T> sessions = (Set<T>) CacheMapDao.getActiveCache();
        return sessions;
    }

    /**
     * 获取所有集群机器存活的session
     *
     * @param <T>
     */
    public static <T> Collection<T> getAllActiveLoginUser(Class<T> cls) {
        Set<T> sessions = new HashSet<>();
        Set<String> keys = RedisSessionDao.getActiveSessions();
        for (String key : keys) {
            sessions.add(ProtoUtil.deserializeFromString((String) RedisUtil.get(key), cls));
        }
        return sessions;
    }

    //注销
    public static void logout(String sessionId) {
        if (isUseRedis) {
            RedisSessionDao.delete(sessionId);
        } else {
            CacheMapDao.deleteCache(RedisKey.CACHEKEY + sessionId);
        }
    }

    /**
     * 是否登录
     *
     * @param sessionId
     * @return
     */
    public static boolean isLogin(String sessionId) {
        Object obj = CacheMapDao.doReadCache(sessionId);
        if (ObjectUtil.isNull(obj) && isUseRedis)
            obj = RedisSessionDao.doReadSession(sessionId);
        return ObjectUtil.isNotNull(obj);
    }


    /**
     * 更新缓存时间
     *
     * @param sessionId
     */
    public static void updateSessionTime(String sessionId) {
        if (ObjectUtil.isNull(sessionId)) return;
        if (CacheMapDao.isFourFifthsExpiryTime(sessionId)) {
            if (isUseRedis)
                RedisSessionDao.update(sessionId);
            CacheMapDao.setExpiryTime(RedisKey.CACHEKEY + sessionId);
            //附带的外置操作
            ClassAndMethod route = InitMothods.getFilter("sessionUpdate");
            route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()), route.getIndex(),
                    new Object[]{sessionId});
        }
    }

}
