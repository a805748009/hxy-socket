package nafos.security;

import nafos.core.entry.ClassAndMethod;
import nafos.core.entry.error.BizException;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.InitMothods;
import nafos.core.util.ObjectUtil;
import nafos.security.cache.CacheMapDao;
import nafos.security.cache.RedisSessionDao;
import nafos.security.config.SecurityConfig;

import java.util.Collection;
import java.util.Set;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月25日 下午9:41:34
 * 安全操作工具类
 */
public class SecurityUtil {

    private static boolean isUseRedis;

    static {
        isUseRedis = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIsUseRedis();
    }


    /**
     * 获取登录用户
     *
     * @param sessionId
     * @return
     */
    public static <T> T getLoginUser(String sessionId) {
        T obj = (T) CacheMapDao.doReadCache(sessionId);
        if (ObjectUtil.isNotNull(obj)) {
            return obj;
        } else {
            if (!isUseRedis)
                return null;
            obj = RedisSessionDao.doReadSession(sessionId);
            if (ObjectUtil.isNotNull(obj)) {
                CacheMapDao.saveCache(sessionId, obj);
                CacheMapDao.saveTimeCache(sessionId, RedisSessionDao.doReadSessionTime(sessionId));
                return obj;
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
        CacheMapDao.saveTimeCache(sessionId, System.currentTimeMillis());
        if (isUseRedis) {
            RedisSessionDao.saveSession(sessionId, obj);
        }
    }

    /**
     * 获取本地机器存活的session
     *
     * @param <T>
     */
    public static <T> Collection<T> getBaseActiveLoginUser() {
        Set<T> sessions = (Set<T>) CacheMapDao.getActiveCache();
        return sessions;
    }

    /**
     * 获取所有集群机器存活的session
     *
     * @param <T>
     */
    public static <T> Collection<T> getAllActiveLoginUser() {
        return (Collection<T>) RedisSessionDao.getActiveSessions();
    }

    //注销
    public static void logout(String sessionId) {
        if (isUseRedis) {
            RedisSessionDao.delete(sessionId);
        } else {
            CacheMapDao.deleteCache(sessionId);
        }
    }

    /**
     * 是否登录
     *
     * @param sessionId
     * @return
     */
    public static boolean isLogin(String sessionId) {
        boolean exists = CacheMapDao.exists(sessionId);
        if (exists) return true;
        if (isUseRedis)
            return RedisSessionDao.existsSession(sessionId);
        return false;
    }


    /**
     * 更新缓存时间
     *
     * @param sessionId
     */
    public static void updateSessionTime(String sessionId) {
        if (ObjectUtil.isNull(sessionId)) throw BizException.LOGIN_SESSION_TIME_OUT;
        if (CacheMapDao.isFourFifthsExpiryTime(sessionId)) {
            CacheMapDao.saveTimeCache(sessionId, System.currentTimeMillis());
            if (isUseRedis) {
                RedisSessionDao.update(sessionId);
            }
            //附带的外置操作
            ClassAndMethod route = InitMothods.getFilter("sessionUpdate");
            if(route == null) return;
            route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()), route.getIndex(),
                    new Object[]{sessionId});
        }
    }

}
