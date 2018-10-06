package com.hxy.nettygo.result.base.security;

import com.hxy.nettygo.result.base.redis.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月29日 上午11:41:46 
* 登录的一级缓存 cache
*/
public class CacheMapDao {
	private static Logger logger = LoggerFactory.getLogger(CacheMapDao.class);
	
	private static ExpiryMap<String,Object> exMap = new ExpiryMap<>();
	
	

    public static void updateCacheAndSession(String sessionId,Object obj){
    	saveCache(sessionId,obj);
    }

    /**
     * 删除session和cache
     */
    public static void deleteCache(String sessionId) {
    	exMap.del(RedisKey.CACHEKEY+sessionId);
    }

    /**
     * 获取存活的Cache
     * @param <T>
     * @param <T>
     */
    public static  Set<Object> getActiveCache() {
        return (Set<Object>) exMap.values();
    }


    /**
     * 获取cache
     */
    public static Object doReadCache(String sessionId) {
         if(sessionId == null){  
                return null;  
            }  
        Object obj = exMap.get(RedisKey.CACHEKEY+sessionId);
     	if(obj==null){
     		return null;
     	}
         return obj;
    }

    
    /**
     * 保存cache,session并存储过期时间
     * @param session
     * @throws UnknownSessionException
     */
    public static void saveCache(String sessionId,Object obj) {
        if (obj == null) {
            logger.error("要存入的session为空");  
            return;
        }
        exMap.put(RedisKey.CACHEKEY+sessionId,obj);
    }
    
    /**
     * 是否超过过期时间的4/5
     * @param key
     * @return
     */
    public static boolean isFourFifthsExpiryTime(Object key ){
    	return exMap.isFourFifthsExpiryTime(key);
    }
    
	  /**
	   * 重新设置过期时间
	   * @param sessionId
	   */
  	public static void setExpiryTime(String sessionId){
  		exMap.setExpiryTime(sessionId);
  	}
  	
  	/**
	 * 删除失效的key-value
	 */
  	public static void delTimeOut(){
  		exMap.delTimeOut();
  	}

}
