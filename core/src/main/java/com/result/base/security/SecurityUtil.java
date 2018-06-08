package com.result.base.security;

import com.result.base.config.ConfigForSecurityMode;
import com.result.base.entry.RouteClassAndMethod;
import com.result.base.inits.InitMothods;
import com.result.base.redis.RedisKey;
import com.result.base.redis.RedisUtil;
import com.result.base.tools.ObjectUtil;
import com.result.base.tools.SerializationUtil;
import com.result.base.tools.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月25日 下午9:41:34
 * 类说明
 */
@Component
public class SecurityUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);


    /**
     * 获取登录用户
     *
     * @param sessionId
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getLoginUser(String sessionId, Class<T> cls) {
        Object obj = CacheMapDao.doReadCache(sessionId);
        if (ObjectUtil.isNotNull(obj)) {
            return (T) obj;
        } else {
            if (!ConfigForSecurityMode.ISSETSESSIONTOREDIS)
                return null;
            obj = RedisSessionDao.doReadSession(sessionId);
            if (ObjectUtil.isNotNull(obj)) {
                T t = SerializationUtil.deserializeFromString((String) obj, cls);
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
        if (ConfigForSecurityMode.ISSETSESSIONTOREDIS)
            RedisSessionDao.saveSession(sessionId, obj);
    }

    /**
     * 获取存活的sessions
     *
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> getActiveLoginUser(Class<T> cls) {
        Set<T> sessions = (Set<T>) CacheMapDao.getActiveCache();
        if (!sessions.isEmpty()) {
            return sessions;
        } else {
            if (!ConfigForSecurityMode.ISSETSESSIONTOREDIS)
                return sessions;
            sessions = new HashSet<>();
            Set<String> keys = RedisSessionDao.getActiveSessions();
            for (String key : keys) {
                sessions.add(SerializationUtil.deserializeFromString((String) RedisUtil.get(key), cls));
            }
            return sessions;
        }
    }

    //注销
    public static void logout(String sessionId) {
        if (ConfigForSecurityMode.ISSETSESSIONTOREDIS) {
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
        if (ObjectUtil.isNull(obj) && ConfigForSecurityMode.ISSETSESSIONTOREDIS)
            obj = RedisSessionDao.doReadSession(sessionId);
        return ObjectUtil.isNotNull(obj);
    }


    /**
     * 验证此次请求是否需要登录，
     *
     * @param uri
     * @return
     */
    public static boolean isGoLogin(String sessionId, String uri) {
        //开启全验证状态
        if ("ALLVALIDATE".equals(ConfigForSecurityMode.ISVALIDATE)) {
            if (!isLogin(sessionId) && !ConfigForSecurityMode.EXCEPTIONVALIDATE.contains(uri)) {
                return true;
            }
            return false;
        }

        //开启全不验证状态
        if ("NOVALIDATE".equals(ConfigForSecurityMode.ISVALIDATE)) {
            if (!isLogin(sessionId) && ConfigForSecurityMode.EXCEPTIONVALIDATE.contains(uri)) {
                return true;
            }
            return false;
        }

        //否则是不开启验证状态
        return false;
    }

    /**
     * 更新缓存时间
     *
     * @param sessionId
     */
    public static void updateSessionTime(String sessionId) {
        if (CacheMapDao.isFourFifthsExpiryTime(sessionId)) {
            if(ConfigForSecurityMode.ISSETSESSIONTOREDIS)
                RedisSessionDao.update(sessionId);
            CacheMapDao.setExpiryTime(RedisKey.CACHEKEY + sessionId);
            //附带的外置操作
            RouteClassAndMethod route = InitMothods.getSessionUpdateHandle();
            route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
                    new Object[]{sessionId});
        }
    }

}
