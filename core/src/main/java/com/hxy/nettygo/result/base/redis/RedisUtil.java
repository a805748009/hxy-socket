package com.hxy.nettygo.result.base.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.result.tools.ConfigUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

public class RedisUtil {
	
	/**
	 * 这里在login服务器中，主要做session的缓存。
	 */
	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	//本机redis连接池
	private static JedisPool jedisPool = null;
	//集群jedis，这里主要做从机。 jedispool主机设置密码，给写权限，从机给只读权限，读写分离。
	private static ShardedJedisPool shardedJedisPool = null;
	/**
	 * 初始化Redis连接池
	 */
	public static void setJedisPool(JedisPool jedisPoolarg,ShardedJedisPool shardedJedisPoolarg){
		jedisPool = jedisPoolarg;
		shardedJedisPool=shardedJedisPoolarg;
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				throw new Exception("jedisPool为空");
			}
		} catch (Exception e) {
			logger.error("Redis缓存获取Jedis实例 出错！", e);
			logger.error("如果开启了redis作为nettgo缓存，请在业务中初始化=====>RedisUtil.setJedisPool(jedisPool，shardedJedisPool)");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取shardedJedis实例
	 * 
	 * @return
	 */
	public static ShardedJedis getShardedJedis() {
		try {
			if (shardedJedisPool != null) {
				ShardedJedis resource = shardedJedisPool.getResource();
				return resource;
			} else {
				throw new Exception("jedisPool为空");
			}
		} catch (Exception e) {
			logger.error("Redis缓存获取shardedJedis实例 出错！", e);
			logger.error("如果开启了redis作为nettgo缓存，请在业务中初始化=====>RedisUtil.setJedisPool(jedisPool，shardedJedisPool)");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 释放shardedJedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final ShardedJedis shardedJedis) {
		if (shardedJedis != null) {
			shardedJedis.close();
		}
	}

	/**
	 * 向缓存中设置字符串内容
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return
	 * @throws Exception
	 */
	public static boolean set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			if(jedis != null){
				jedis.set(key, value);
			}
			return true;
		} catch (Exception e) {
			logger.error("Redis缓存设置key值 出错！", e);
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 判断key是否存在
	 */
	public static boolean exists(String key){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis == null) {
				return false;
			} else {
				return jedis.exists(key);
			}
		} catch (Exception e) {
			logger.error("Redis缓存判断key是否存在 出错！", e);
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 删除缓存中的对象，根据key
	 * @param key
	 * @return
	 */
	public static boolean del(String ... key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.del(key);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	
	

	

	
	
	
	//*******************key-value****************start
	
	/**
	 * 向缓存中设置对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(String key, Object value) {
		Jedis jedis = null;
		try {
			String objectJson = JSONObject.fromObject(value).toString();
			jedis = getJedis();
			if (jedis != null) {
				jedis.set(key, objectJson);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 向缓存中设置有超时时间的对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setex(String key,int timeout, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			if (jedis != null) {
				jedis.setex(key,timeout,value);
				return true;
			}else{
				throw new Exception("jedis为空,请初始化数据池");
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 根据key 获取内容
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			Object value = jedis.get(key);
			return value;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			jedis.close();
		}
	}
	
	
	
	/**
	 * 根据key 获取对象
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> clazz) {
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis != null) {
				return (T) JSONObject.toBean(JSONObject.fromObject(jedis.get(key)), clazz);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 获取相关的key 
	 * 
	 * @param key
	 * @return
	 */
	public static  Set<String> keys(String keys) {
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis != null) {
				return  jedis.hkeys(keys);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	
	//*******************key-value****************end
	
	//*************** 操作list****************start
	/**
	 * 向缓存中设置对象 
	 * @param key
	 * @param list
	 * T string calss
	 * @return
	 */
	public static <T> boolean setList(String key,List<T> list){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			if (jedis != null) {
				for (T vz : list) {
					if (vz instanceof String) {
						jedis.lpush(key, (String) vz);
					} else {
						String objectJson = JSONObject.fromObject(vz).toString();
						jedis.lpush(key, objectJson);
					}
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getListEntity(String key,Class<T> entityClass){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis != null) {
				List<String> valueJson = jedis.lrange(key, 0, -1);
				JSONArray json = new JSONArray();
				json.addAll(valueJson);
				JSONArray jsonArray = JSONArray.fromObject(json.toString());
				return (List<T>) JSONArray.toCollection(jsonArray, entityClass);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}

	 /**
     * 
     * list在头部新加一个元素
     *
     * @author HXY
     * @date 2017年10月31日下午2:46:46
     * @param key
     * @param obj
     * @return
     */
    public static <T> boolean LpushOne(String key,String obj){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			if (jedis != null) {
				jedis.lpush(key,obj);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
    
	public static List<String> getListString(String key){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis != null) {
				return jedis.lrange(key, 0, -1);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	//*************** 操作list****************end
	
	//*************** 操作map****************start
	public static <K,V> boolean setMap(String key,Map<String,V> map){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			if (jedis != null) {
				Set<Map.Entry<String, V>> entry = map.entrySet();
				for (Iterator<Map.Entry<String, V>> ite = entry.iterator(); ite.hasNext();) {
					Map.Entry<String, V> kv = ite.next();
					if (kv.getValue() instanceof String) {
						jedis.hset(key, kv.getKey(), (String) kv.getValue());
					}else if (kv.getValue() instanceof List) {
						jedis.hset(key, kv.getKey(), JSONArray.fromObject(kv.getValue()).toString());
					} else {
						jedis.hset(key, kv.getKey(), JSONObject.fromObject(kv.getValue()).toString());
					}
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	public static boolean setMapKey(String key,String mapKey,Object value){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			if (jedis != null) {
				if (value instanceof String) {
					jedis.hset(key, mapKey, String.valueOf(value));
				} else if (value instanceof List) {
					jedis.hset(key, mapKey, JSONArray.fromObject(value).toString());
				} else {
					jedis.hset(key, mapKey, JSONObject.fromObject(value).toString());
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * seconds key和value 保存的有效时间（单位：秒）
	 * @return
	 */
	public static boolean setMapKeyExpire(String key,String mapKey,Object value, int seconds){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			if (jedis != null) {
				if (value instanceof String) {
					jedis.hset(key, mapKey, String.valueOf(value));
				} else if (value instanceof List) {
					jedis.hset(key, mapKey, JSONArray.fromObject(value).toString());
				} else {
					jedis.hset(key, mapKey, JSONObject.fromObject(value).toString());
				}
				jedis.expire(key, seconds);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V> Map<String,V> getMap(String key){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis != null) {
				Map<String, V> map = (Map<String, V>) jedis.hgetAll(key);
				return map;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V> Map<String,V> getMapEntityClass(String key,Class<V> clazz){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis != null) {
				Map<String, V> map = (Map<String, V>) jedis.hgetAll(key);
				Set<Map.Entry<String, V>> entry = map.entrySet();
				for (Iterator<Map.Entry<String, V>> ite = entry.iterator(); ite.hasNext();) {
					Map.Entry<String, V> kv = ite.next();
					map.put(kv.getKey(), (V) JSONObject.toBean(JSONObject.fromObject(kv.getValue()), clazz));
				}
				return map;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V> Map<String,List<V>> getMapList(String key,Class<V> clazz){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis != null) {
				Map<String, V> map = (Map<String, V>) jedis.hgetAll(key);
				Set<Map.Entry<String, V>> entry = map.entrySet();
				for (Iterator<Map.Entry<String, V>> ite = entry.iterator(); ite.hasNext();) {
					Map.Entry<String, V> kv = ite.next();
					JSONArray jsonArray = JSONArray.fromObject(kv.getValue());
					map.put(kv.getKey(), (V) JSONArray.toCollection(jsonArray, clazz));
				}
				return (Map<String, List<V>>) map;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getMapKeyListEntity(String key,String mapKey,
			Class<T> entityClass){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if (jedis != null) {
				String valueJson = jedis.hget(key, mapKey);
				JSONArray jsonArray = JSONArray.fromObject(valueJson);
				return (List<T>) JSONArray.toCollection(jsonArray, entityClass);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getMapKeyEntity(String key,String mapKey,
			Class<T> entityClass){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if(jedis != null){
			String valueJson=jedis.hget(key, mapKey);
			return (T) JSONObject.toBean(JSONObject.fromObject(valueJson), entityClass);
			}else{return null;}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	public static Object getMapKey(String key,String mapKey){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if(jedis != null){
			return jedis.hget(key, mapKey);
			}else{return null;}
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	public static boolean delMapKey(String key,String mapKey){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.hdel(key, mapKey);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	 /**
		 * 
		 * 获取map的value list
		 *
		 * @author HXY
		 * @date 2017年10月27日下午1:18:12
		 * @param key
		 * @param clazz
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static <T> List<T>  getMapEntityListClass(String key,Class<T> clazz){
			ShardedJedis jedis = null;
			try {
				jedis = getShardedJedis();
				if (jedis != null && exists(key)) {
					List<String> list = jedis.hvals(key);
					JSONArray json = new JSONArray();
					json.addAll(list);
					JSONArray jsonArray = JSONArray.fromObject(json.toString());
					return (List<T>) JSONArray.toCollection(jsonArray, clazz);
				} else {
					return null;
				}
			} catch (Exception e) {
				logger.error(e.toString());
				return null;
			} finally {
				returnResource(jedis);
			}
		}
	
	public static boolean hexists(String key,String mapKey){
		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			return jedis.hexists(key,mapKey);
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			jedis.close();
		}
	}
	
	/**
	 * 获取map里面的所有key
	 * @param key
	 * @return
	 */
	public static Set<String> getHkeys(String key){
		ShardedJedis jedis = null;
		try {
			jedis = getShardedJedis();
			if(jedis != null){
			return jedis.hkeys(key);
			}else{return null;}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	//*************** 操作map****************end
	
	//***************计数器应用INCR,DECR****************begin
	//Redis的命令都是原子性的，你可以轻松地利用INCR，DECR命令来构建计数器系统
	
	/**
	 * incr(key)：名称为key的string增1操作
	 */
	public static boolean incr(String key){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.incr(key);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * incrby(key, integer)：名称为key的string增加integer
	 */
	public static boolean incrBy(String key, int value){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.incrBy(key, value);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * decr(key)：名称为key的string减1操作
	 */
	public static boolean decr(String key){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.decr(key);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * decrby(key, integer)：名称为key的string减少integer
	 */
	public static boolean decrBy(String key, int value){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.decrBy(key, value);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	//***************计数器应用INCR,DECR****************end
	
	//***************使用sorted set(zset)甚至可以构建有优先级的队列系统***************begin
	/**
	 * 向名称为key的zset中添加元素member，score用于排序。
	 * 如果该元素已经存在，则根据score更新该元素的顺序
	 */
	public static boolean zadd(String key, double score, String member){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zadd(key, score, member);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 删除名称为key的zset中的元素member
	 */
	public static boolean zrem(String key, String... members){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.zrem(key, members);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	 /**
		 * decr(key)：名称为key的long减1操作
		 */
		public static long decrlong(String key){
			Jedis jedis = null;
			try {
				jedis = getJedis();
				return jedis.decr(key);
			} catch (Exception e) {
				logger.error(e.toString());
				return -99999;
			} finally {
				returnResource(jedis);
			}
		}
	
	/**
	 * 返回集合中score在给定区间的元素
	 */
	public static Set<String> zrangeByScore(String key, double min, double max){
		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			return jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 返回有序集合中排名介于start到stop之间的成员的分值和值（包括start和stop）
	 */
	public static Set<Tuple> zrangeWithScores(String key, long start, long stop){
		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			return jedis.zrangeWithScores(key, start, stop);
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 返回集合中score在给定区间的元素的分值和值（包括min和max）
	 */
	public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max){
		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	//***************使用sorted set(zset)甚至可以构建有优先级的队列系统***************end
	
	//***************sorted set 处理***************************************begin
	//zset 处理
	public static boolean zaddObject(String key, double score, Object value){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String objectJson = JSONObject.fromObject(value).toString();
			jedis.zadd(key, score, objectJson);
			return true;
		} catch (Exception e) {
			logger.warn(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * score值递减(从大到小)次序排列。
	 * @param key
	 * @param max score
	 * @param min score
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  <T> List<T> zrevrangeByScore(String key,double max,double min, 
			Class<T> entityClass){
		ShardedJedis jedis = null;
		try {
			jedis =shardedJedisPool.getResource();
			Set<String> set=jedis.zrevrangeByScore(key, max, min);
			List<T> list=new ArrayList<T>();
			for (String str : set) {  
				list.add((T) JSONObject.toBean(JSONObject.fromObject(str), entityClass));
			} 
			return list;
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 返回有序集合中分值介于min到max之间的所有成员
	 * @param key
	 * @param min score
	 * @param max score
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  <T> List<T> zrangeByScore(String key,double min,double max, 
			Class<T> entityClass){
		ShardedJedis jedis = null;
		try {
			jedis =shardedJedisPool.getResource();
			Set<String> set=jedis.zrangeByScore(key, min, max);
			List<T> list=new ArrayList<T>();
			for (String str : set) {  
				list.add((T) JSONObject.toBean(JSONObject.fromObject(str), entityClass));
			} 
			return list;
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	
	/**
	 * 返回有序集合中分值介于min到max之间的所有成员
	 * @param key
	 * @param min score
	 * @param max score
	 * @param entityClass
	 * @return
	 */
	public static  Set<String> zrangeByScoreStr(String key,double min,double max){
		ShardedJedis jedis = null;
		try {
			jedis =shardedJedisPool.getResource();
			Set<String> set=jedis.zrangeByScore(key, min, max);
			return set;
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	
	/**
	 * score值递减(从大到小)次序排列。
	 * @param key
	 * @param max score
	 * @param min score
	 * @param offset count (类似mysql的 LIMIT)
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  <T> List<T> zrevrangeByScore(String key,double max,double min,
			int offset, int count,Class<T> entityClass){
		ShardedJedis jedis = null;
		try {
			jedis =shardedJedisPool.getResource();
			Set<String> set=jedis.zrevrangeByScore(key, max, min,offset,count);
			List<T> list=new ArrayList<T>();
			for (String str : set) {  
				list.add((T) JSONObject.toBean(JSONObject.fromObject(str), entityClass));
			} 
			return list;
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	
	
	//得到总记录数
	public static long zcard(String key){
		ShardedJedis jedis = null;
		try {
			jedis =shardedJedisPool.getResource();
			return jedis.zcard(key);
		} catch (Exception e) {
			logger.warn(e.toString());
			return 0;
		} finally {
			returnResource(jedis);
		}
	}
	
	//删除 元素
	public static  boolean zremObject(String key, Object value){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String objectJson = JSONObject.fromObject(value).toString();
			jedis.zrem(key, objectJson);
			return true;
		} catch (Exception e) {
			logger.warn(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	//统计zset集合中score某个范围内（1-5），元素的个数
	public static long zcount(String key,double min, double max){
		ShardedJedis jedis = null;
		try {
			jedis =shardedJedisPool.getResource();
			return jedis.zcount(key,min,max);
		} catch (Exception e) {
			logger.warn(e.toString());
			return 0;
		} finally {
			returnResource(jedis);
		}
	}
	
	//查看zset集合中元素的score
	public static double zscore(String key, Object value){
		ShardedJedis jedis = null;
		try {
			jedis =shardedJedisPool.getResource();
			String objectJson = JSONObject.fromObject(value).toString();
			return jedis.zscore(key, objectJson);
		} catch (Exception e) {
			// 如client初始化正确，则这里引起异常是由于key不存在或者value不属于key内的元素     ——2017-07-20屏蔽
			// logger.warn(e.toString());
			return 0;
		} finally {
			returnResource(jedis);
		}
	}
	//**************sorted set******************************************end
	
	//***********************Redis Set集合操作**************************begin
	/**
	 * sadd:向名称为Key的set中添加元素,同一集合中不能出现相同的元素值。（用法：sadd set集合名称 元素值）
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean sadd(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.sadd(key, value);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * srem:删除名称为key的set中的元素。（用法：srem set集合名称 要删除的元素值）
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean srem(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.srem(key, value);
			return true;
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * sdiff:返回所有给定key与第一个key的差集。（用法：sdiff set集合1 set集合2）
	 * 
	 * @param key1
	 * @param key2
	 * @return
	 */
	public static Set<String> sdiff(String key1, String key2) {
		Jedis jedis = null;
		Set<String> diffList = null;
		try {
			jedis = getJedis();
			diffList = jedis.sdiff(key1, key2);
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			returnResource(jedis);
		}
		return diffList;
	}

	/**
	 * sismember:判断某个值是否是集合的元素。（用法：sismember 集合1 指定的元素值）
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean sismember(String key, String value) {
		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			return jedis.sismember(key, value);
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * smembers(key) ：返回名称为key的set的所有元素
	 * 
	 * @param key
	 * @return
	 */
	public static Set<String> smembers(String key) {
		Jedis jedis = null;
		Set<String> list = null;
		try {
			jedis = getJedis();
			list = jedis.smembers(key);
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			returnResource(jedis);
		}
		return list;
	}
	
	//***********************Redis Set集合操作****************************end
}
