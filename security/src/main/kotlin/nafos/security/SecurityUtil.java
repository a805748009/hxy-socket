package nafos.security;

import nafos.security.cache.CacheMapDao;
import nafos.security.cache.RedisSessionDao;
import nafos.security.config.SecurityConfig;
import nafos.security.filter.SessionTimeUpdateFactory;
import nafos.server.BizException;
import nafos.server.ClassAndMethod;
import nafos.server.SpringApplicationContextHolder;

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
        isUseRedis = SecurityConfig.isUseRedis();
    }


   /***
    *@Description 获取登录用户
    *@Author      xinyu.huang
    *@Time        2019/12/1 18:02
    */
    public static <T> T getLoginUser(String sessionId) {
        T obj = (T) CacheMapDao.doReadCache(sessionId);
        if (obj != null) {
            return obj;
        } else {
            if (!isUseRedis) {
                return null;
            }
            obj = RedisSessionDao.doReadSession(sessionId);
            if (obj != null) {
                CacheMapDao.saveCache(sessionId, obj);
                CacheMapDao.saveTimeCache(sessionId, RedisSessionDao.doReadSessionTime(sessionId));
                return obj;
            }
            return null;
        }
    }

    /***
     *@Description 设置登录用户
     *@Author      xinyu.huang
     *@Time        2019/12/1 17:54
     */
    public static void setLoginUser(String sessionId, Object obj) {
        CacheMapDao.saveCache(sessionId, obj);
        CacheMapDao.saveTimeCache(sessionId, System.currentTimeMillis());
        if (isUseRedis) {
            RedisSessionDao.saveSession(sessionId, obj);
        }
    }

    /***
     *@Description 获取本地机器存活的session
     *@Author      xinyu.huang
     *@Time        2019/12/1 17:54
     */
    public static <T> Collection<T> getBaseActiveLoginUser() {
        Set<T> sessions = (Set<T>) CacheMapDao.getActiveCache();
        return sessions;
    }

   /***
    *@Description 获取所有集群机器存活的session
    *@Author      xinyu.huang
    *@Time        2019/12/1 17:54
    */
    public static <T> Collection<T> getAllActiveLoginUser() {
        return (Collection<T>) RedisSessionDao.getActiveSessions();
    }

    /***
     *@Description 注销
     *@Author      xinyu.huang
     *@Time        2019/12/1 17:53
     */
    public static void logout(String sessionId) {
        if (isUseRedis) {
            RedisSessionDao.delete(sessionId);
        } else {
            CacheMapDao.deleteCache(sessionId);
        }
    }

   /***
    *@Description 是否登录
    *@Author      xinyu.huang
    *@Time        2019/12/1 17:53
    */
    public static boolean isLogin(String sessionId) {
        boolean exists = CacheMapDao.exists(sessionId);
        if (exists) {
            return true;
        }
        if (isUseRedis) {
            return RedisSessionDao.existsSession(sessionId);
        }
        return false;
    }


  /***
   *@Description 更新缓存时间
   *@Author      xinyu.huang
   *@Time        2019/12/1 17:53
   */
    public static void updateSessionTime(String sessionId) {
        if (sessionId.isEmpty() || sessionId == null) {
            throw BizException.LOGIN_SESSION_TIME_OUT;
        }
        if (CacheMapDao.isFourFifthsExpiryTime(sessionId)) {
            CacheMapDao.saveTimeCache(sessionId, System.currentTimeMillis());
            if (isUseRedis) {
                RedisSessionDao.update(sessionId);
            }
            //附带的外置操作
            for (ClassAndMethod route : SessionTimeUpdateFactory.handles) {
                route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()), route.getIndex(),
                        new Object[]{sessionId});
            }
        }
    }

}
