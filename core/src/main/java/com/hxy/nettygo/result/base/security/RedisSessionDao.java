package com.hxy.nettygo.result.base.security;
import java.util.Set;

import com.hxy.nettygo.result.base.redis.RedisKey;
import com.hxy.nettygo.result.base.tools.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hxy.nettygo.result.base.config.ConfigForSecurityMode;
import com.hxy.nettygo.result.base.redis.RedisUtil;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author huangxinyu
 * 
 * @version 创建时间：2018年1月29日 下午1:38:35 
 * 登录的二级缓存session-redis 做分布式
 *
 */
public class RedisSessionDao {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);


    

    public static void update(String sessionId){
        //通知其他服务器，修改session时间
        Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			if(jedis != null){
				jedis.expire(RedisKey.SESSIONKEY+sessionId, ConfigForSecurityMode.LOGINSESSIONTIMEOUT);//修改redis的过期时间
				jedis.publish("LoginSessionUpdate", sessionId);  
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			RedisUtil.returnResource(jedis);
		}
    }

    /**
     * 删除session
     */
    public static void delete(String sessionId) {
        RedisUtil.del(RedisKey.SESSIONKEY+sessionId);
        //通知其他服务器，删除session
        Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			if(jedis != null){
				jedis.publish("LoginSessionDelete", sessionId);  
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			RedisUtil.returnResource(jedis);
		}
      
    }

    /**
     * 获取存活的sessions
     * @param
     */
    public static Set<String> getActiveSessions() {
        return RedisUtil.keys(RedisKey.SESSIONKEY+"*");
    }


    /**
     * 获取session
     */
    public static Object doReadSession(String sessionId) {
         if(sessionId == null){  
                return null;  
            }  
        Object obj = RedisUtil.get(RedisKey.SESSIONKEY+sessionId);
     	if(obj==null){
     		return null;
     	}
         return obj;
    }

    
    /**
     * 保存session并存储过期时间
     */
    public static void saveSession(String sessionId,Object obj) {
        if (obj == null) {
            logger.error("要存入的session为空");  
            return;
        }
        //设置过期时间
        int expireTime = ConfigForSecurityMode.LOGINSESSIONTIMEOUT;
        String uiui = SerializationUtil.serializeToString(obj);
        RedisUtil.setex(RedisKey.SESSIONKEY+sessionId,expireTime,uiui);
    }


}
