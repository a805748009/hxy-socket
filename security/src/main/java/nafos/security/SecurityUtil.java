package nafos.security;

import nafos.core.entry.ClassAndMethod;
import nafos.core.mode.InitMothods;
import nafos.core.util.CastUtil;
import nafos.core.util.ObjectUtil;
import nafos.core.util.ProtoUtil;
import nafos.core.util.SpringApplicationContextHolder;
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
 * 类说明
 */
public class SecurityUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private static boolean isUseRedis ;

    private static String isValidate ;

    private static List<String> oppositeHttpList;

    private static List<Integer> oppositeCodeList = new ArrayList<>();

    static{

        isUseRedis = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIsUseRedis();

        isValidate = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIsValidate();

        oppositeHttpList = Arrays.asList(SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getOppositeHttpList().split(","));

        for (String s : SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getOppositeCodeList().split(",")) {
            oppositeCodeList.add(CastUtil.castInt(s));
        }

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
     * 验证此次请求是否需要登录，
     *
     * @param uri
     * @return boolean  true 阻止， false 放行
     */
    public static boolean isNeedLogin(String sessionId, String uri) {
        //远程调用的直接放行
        if(uri.startsWith("/nafosRemoteCall"))return false;

        //开启全验证状态
        if ("ALLVALIDATE".equals(isValidate)) {
            if (!isLogin(sessionId) && !oppositeHttpList.contains(uri)) {
                return true;
            }
            return false;
        }

        //开启全不验证状态
        if ("NOVALIDATE".equals(isValidate)) {
            if (!isLogin(sessionId) && oppositeHttpList.contains(uri)) {
                return true;
            }
            return false;
        }

        //否则是不开启验证状态
        return false;
    }



    /**
     * 验证此次请求是否需要登录，
     *
     * @param code
     * @return boolean  true 阻止， false 放行
     */
    public static boolean isNeedLogin(String sessionId, int code) {

        //开启全验证状态
        if ("ALLVALIDATE".equals(isValidate)) {
            if (!isLogin(sessionId) && !oppositeCodeList.contains(code)) {
                return true;
            }
            return false;
        }

        //开启全不验证状态
        if ("NOVALIDATE".equals(isValidate)) {
            if (!isLogin(sessionId) && oppositeCodeList.contains(code)) {
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
            if(isUseRedis)
                RedisSessionDao.update(sessionId);
            CacheMapDao.setExpiryTime(RedisKey.CACHEKEY + sessionId);
            //附带的外置操作
            ClassAndMethod route = InitMothods.getFilter("sessionUpdate");
            route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),route.getIndex(),
                    new Object[]{sessionId});
        }
    }

}
