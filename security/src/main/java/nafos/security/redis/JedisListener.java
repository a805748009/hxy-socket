package nafos.security.redis;


import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.runner.NafosRunner;
import nafos.security.cache.CacheMapDao;
import nafos.security.config.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年2月6日 下午9:05:09
 * 类说明  开机自动启动redis监听
 */
public class JedisListener implements NafosRunner {
    private static final Logger logger = LoggerFactory.getLogger(JedisListener.class);

    private static boolean redisListenered = false;


    /**
     * 开启redis监听
     */
    public void runRedisListener() {
        if (redisListenered)
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                subcribe();
            }
        }).start();

        redisListenered = true;
    }


    //开启监听
    public void subcribe() {
        JedisPubSub jedisSessionUpdate = new LoginSessionUpdate();
        Jedis jedisUpdate = null;
        try {
            jedisUpdate = RedisUtil.getJedis();
            if (jedisUpdate != null) {
                //监听管道
                jedisUpdate.subscribe(jedisSessionUpdate, "LoginSessionUpdate", "LoginSessionDelete");
            }
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            RedisUtil.returnResource(jedisUpdate);
        }
    }

    @Override
    public void run() {
        if (SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIsUseRedis())
            runRedisListener();
    }
}


//redis监听操作
class LoginSessionUpdate extends JedisPubSub {
    @Override
    public void proceed(Client client, String... channels) {
        super.proceed(client, channels);
    }

    @Override
    public void onMessage(String channel, String message) {
        if ("LoginSessionDelete".equals(channel)) {
            CacheMapDao.deleteCache(RedisKey.CACHEKEY + message);
        }
        if ("LoginSessionUpdate".equals(channel)) {
            CacheMapDao.setExpiryTime(RedisKey.CACHEKEY + message);
        }
        //消息处理函数
        super.onMessage(channel, message);
    }


}
