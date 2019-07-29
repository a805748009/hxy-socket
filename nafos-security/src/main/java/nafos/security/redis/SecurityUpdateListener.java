package nafos.security.redis;


import nafos.core.mode.runner.NafosRunner;
import nafos.security.cache.CacheMapDao;
import nafos.security.config.SecurityConfig;
import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年2月6日 下午9:05:09
 * 类说明  开机自动启动redis监听
 */
@Component
public class SecurityUpdateListener implements NafosRunner {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUpdateListener.class);

    private static boolean redisListenered = false;
    private static String securityListener = "nafos:security:listener";
    private static RTopic topic ;

    public static void publishDel(String sessionId){
        topic.publish(new SecurityUpdate("LoginSessionDelete",sessionId));
    }
    public static void publishUpdate(String sessionId){
        topic.publish(new SecurityUpdate("LoginSessionUpdate",sessionId));
    }

    /**
     * 开启redis监听
     */
    public void runRedisListener() {
        if (redisListenered)
            return;
        new Thread(() -> subcribe()).start();
        redisListenered = true;
    }


    public void subcribe() {
        topic.addListener((MessageListener<SecurityUpdate>) (channel, update) -> {
            logger.debug("收到redis监听：{} ， {}", update.getSessionId(), update.getSessionId());
            if ("LoginSessionDelete".equals(update.getAction())) {
                CacheMapDao.deleteCache(update.getSessionId());
            }
            // 其他缓存修改了用户信息，这边删除缓存，使其去redis中拿取最新的数据
            if ("LoginSessionUpdate".equals(update.getAction())) {
                CacheMapDao.deleteCache(update.getSessionId());
            }
        });
    }

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public void run() {
        topic = RedissonManager.getRedisson().getTopic(securityListener);
        if (securityConfig.getIsUseRedis()){
            logger.info("security已开启redis监听");
            runRedisListener();
        }

    }
}


