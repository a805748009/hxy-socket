package nafos.security.cache;

import nafos.core.entry.error.BizException;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.security.config.SecurityConfig;
import nafos.security.redis.RedissonManager;
import nafos.security.redis.SecurityUpdateListener;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author huangxinyu
 * @version 创建时间：2018年1月29日 下午1:38:35
 * 登录的二级缓存session-redis 做分布式
 */
public class RedisSessionDao {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

    private static int sessionTimeout;


    public static final String SESSIONKEY = "nafos:security:sessionId:";

    public static final String SESSIONKEY_TIME = "nafos:security:sessionId:time:";


    static {
        sessionTimeout = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getSessionTimeOut();
        logger.info("nafos:session保存时间：{},用户最后阶段活跃会自动刷新",sessionTimeout);
    }


    public static void update(String sessionId) {
        RedissonManager.getRedisson().getBucket(SESSIONKEY + sessionId).expire(sessionTimeout, TimeUnit.SECONDS);
        RedissonManager.getRedisson().getBucket(SESSIONKEY_TIME + sessionId).expire(sessionTimeout, TimeUnit.SECONDS);
    }

    /**
     * 删除session
     */
    public static void delete(String sessionId) {
        RedissonManager.getRedisson().getBucket(SESSIONKEY + sessionId).delete();
        SecurityUpdateListener.publishDel(sessionId);
    }

    /**
     * 判断是否存在
     *
     * @param sessionId
     * @return
     */
    public static boolean existsSession(String sessionId) {
        return RedissonManager.getRedisson().getBucket(SESSIONKEY + sessionId).isExists();
    }

    /**
     * 获取存活的sessions
     */
    public static Set<Object> getActiveSessions() {
        Set<Object> set = new HashSet<>();
        for (RBucket<Object> objectRBucket : RedissonManager.getRedisson().getBuckets().find(SESSIONKEY + "*")) {
            set.add(objectRBucket.get());
        }
        return set;
    }


    /**
     * 获取session
     */
    public static <T> T doReadSession(String sessionId) {
        if (sessionId == null) {
            throw BizException.LOGIN_SESSION_TIME_OUT;
        }
        T t = (T) RedissonManager.getRedisson().getBucket(SESSIONKEY + sessionId).get();
        return t;
    }

    /**
     * 获取session的创建时间
     * @param sessionId
     * @return
     */
    public static Long doReadSessionTime(String sessionId) {
        if (sessionId == null) {
            throw BizException.LOGIN_SESSION_TIME_OUT;
        }
        return (Long) RedissonManager.getRedisson().getBucket(SESSIONKEY_TIME + sessionId).get();
    }


    /**
     * 保存session并存储过期时间
     *
     * @param sessionId
     * @param obj
     */
    public static void saveSession(String sessionId, Object obj) {
        if (obj == null) {
            logger.error("要存入的session-value为空");
            return;
        }
        //设置过期时间
        RBatch batch = RedissonManager.getRedisson().createBatch();
        batch.getBucket(SESSIONKEY + sessionId).setAsync(obj);
        batch.getBucket(SESSIONKEY + sessionId).expireAsync(sessionTimeout, TimeUnit.SECONDS);
        batch.getBucket(SESSIONKEY_TIME + sessionId).setAsync(System.currentTimeMillis());
        batch.getBucket(SESSIONKEY_TIME + sessionId).expireAsync(sessionTimeout, TimeUnit.SECONDS);
        batch.execute();
        SecurityUpdateListener.publishUpdate(sessionId);
    }


}
