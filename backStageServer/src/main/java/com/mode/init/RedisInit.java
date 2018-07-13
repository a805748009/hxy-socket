package com.mode.init;

import com.hxy.nettygo.result.base.redis.RedisUtil;
import com.hxy.nettygo.result.base.tools.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月14日 下午3:52:34 
* 类说明 
*/
public class RedisInit {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisInit.class);
	
	public static void initRedisPool(){
		try {
			// 加载redis配置文件
			ResourceBundle bundle = ConfigUtil.getBundle("/backStageConf/redis.properties");

			
			if (bundle == null) {
				throw new IllegalArgumentException(
						"[redis.properties] is not found!");
			}
			int maxActivity = Integer.valueOf(bundle
					.getString("redis.pool.maxActive"));
			int maxIdle = Integer.valueOf(bundle
					.getString("redis.pool.maxIdle"));
			long maxWait = Long.valueOf(bundle.getString("redis.pool.maxWait"));
			boolean testOnBorrow = Boolean.valueOf(bundle
					.getString("redis.pool.testOnBorrow"));
			boolean onreturn = Boolean.valueOf(bundle
					.getString("redis.pool.testOnReturn"));
			// 创建jedis池配置实例
			JedisPoolConfig config = new JedisPoolConfig();
			// 设置池配置项值
			config.setMaxTotal(maxActivity);//最大连接数, 默认8个
			config.setMaxIdle(maxIdle);  //最大空闲连接数
			config.setMaxWaitMillis(maxWait);//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
			config.setTestOnBorrow(testOnBorrow);//在获取连接的时候检查有效性, 默认false
			config.setTestOnReturn(onreturn);// 跟验证有关
			//下面实例化连接池，若redis没有设置密码连接，就吧最后的密码注释掉即可
			JedisPool jedisPool   = new JedisPool(config, bundle.getString("redis.ip"),
					Integer.valueOf(bundle.getString("redis.port")), 10000,
					bundle.getString("redis.password"));
//			JedisPool jedisPool = new JedisPool(config, bundle.getString("redis.ip"),
//					Integer.valueOf(bundle.getString("redis.port")), 10000);
			// slave链接
			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			JedisShardInfo info = new JedisShardInfo(bundle.getString("redis.ip"), Integer
					.valueOf(bundle.getString("redis.port1")));
			info.setPassword(bundle.getString("redis.password"));
			shards.add(info);
			ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, shards);
			//初始化
			RedisUtil.setJedisPool(jedisPool, shardedJedisPool);
			logger.info("初始化Redis连接池success");
		} catch (Exception e) {
			logger.error("初始化Redis连接池 出错！", e);
		}
	}

}
