package com.mode.init;

import com.result.base.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月14日 下午3:52:34 
* 类说明 
*/
@Component
public class RedisInit {
	private static final Logger logger = LoggerFactory.getLogger(RedisInit.class);

	@Value("${redis.pool.maxActive}")
	private int maxActive;
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	@Value("${redis.pool.maxWait}")
	private int maxWait;
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${redis.pool.testOnReturn}")
	private boolean testOnReturn;
	@Value("${redis.ip}")
	private String  ip;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.port1}")
	private int port1;
	@Value("${redis.password}")
	private String password;

	
	public  void initRedisPool(){
		try {
			// 创建jedis池配置实例  nettygo系统用，主要用来存储用户session
			JedisPoolConfig config = new JedisPoolConfig();
			// 设置池配置项值
			config.setMaxTotal(maxActive);//最大连接数, 默认8个
			config.setMaxIdle(maxIdle);  //最大空闲连接数
			config.setMaxWaitMillis(maxWait);//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
			config.setTestOnBorrow(testOnBorrow);//在获取连接的时候检查有效性, 默认false
			config.setTestOnReturn(testOnReturn);// 跟验证有关
			//下面实例化连接池，若redis没有设置密码连接，就吧最后的密码注释掉即可
			JedisPool jedisPool   = new JedisPool(config, ip,port, 10000,password);
			// slave链接
			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			JedisShardInfo info = new JedisShardInfo(ip, port1);
			info.setPassword(password);
			shards.add(info);
			ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, shards);
			//初始化
			RedisUtil.setJedisPool(jedisPool, shardedJedisPool);




			//业务redis连接池初始化 选择DB2   业务层用来存储
			JedisPool BSjedisPool   = new JedisPool(config, ip,port, 10000,password,2);
			// slave链接
			List<JedisShardInfo> BSshards = new ArrayList<JedisShardInfo>();
			JedisShardInfo BSinfo = new JedisShardInfo("http://userInfo:"+password+"@"+ip+":"+port1+"/2");
			BSshards.add(BSinfo);
			ShardedJedisPool BSshardedJedisPool = new ShardedJedisPool(config, BSshards);
			//初始化
			BSRedisUtil.setJedisPool(BSjedisPool, BSshardedJedisPool);



			logger.info("初始化Redis连接池success");
		} catch (Exception e) {
			logger.error("初始化Redis连接池 出错！", e);
		}
	}

}
