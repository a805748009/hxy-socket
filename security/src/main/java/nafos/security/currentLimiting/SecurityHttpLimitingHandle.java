package nafos.security.currentLimiting;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import nafos.core.util.NettyUtil;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SpringApplicationContextHolder;
import nafos.network.bootStrap.netty.handle.currency.HttpLimitingHandle;
import nafos.security.config.SecurityConfig;
import nafos.security.redis.RedisLock;
import nafos.security.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;

/**
 * @Author 黄新宇
 * @Date 2018/10/29 下午4:28
 * @Description 限流器
 **/
public class SecurityHttpLimitingHandle extends HttpLimitingHandle {
    private static final Logger logger = LoggerFactory.getLogger(SecurityHttpLimitingHandle.class);



    private final String allLimitingKey = "allLimiting";

    private static String IPLimitingkey = "ipLimit";



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        if (!SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIsUseRedis())
            return;

        //大于0的时候开启IP限流
        if (SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitCount() > 0) {
            ipLimiting(ctx);
        }

        //总限流
        if (SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getAlllimitCount() > 0) {
            if(allLimiting(SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getAlllimitTimeout()*1000l,
                    SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getAlllimitCount()))
                return;

            logger.info("总访问次数超过最大访问次数，已经限制了访问");
            NettyUtil.sendError(ctx,HttpResponseStatus.GATEWAY_TIMEOUT);
        }

    }

    /**
     * 获取限流权限
     * @param millisecond 毫秒数
     * @param limitCount 限流次数
     * @return
     */
    public  boolean allLimiting(Long millisecond, Integer limitCount){
        String currentLimitingLock = RedisLock.lockWithTimeout(allLimitingKey,300,1000);
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            if(ObjectUtil.isNotNull(currentLimitingLock)){
                Long llen = jedis.llen(allLimitingKey);
                if(llen < limitCount){
                    jedis.lpush(allLimitingKey, System.currentTimeMillis()+"");
                    return true;
                }else{
                    Long lastTime = Long.parseLong(jedis.lindex(allLimitingKey, -1));
                    if((System.currentTimeMillis() - lastTime) >= millisecond){
                        jedis.lpush(allLimitingKey, System.currentTimeMillis()+"");
                        jedis.ltrim(allLimitingKey, 0, limitCount);
                        return true;
                    }
                }
            }
        }finally {
            RedisUtil.returnResource(jedis);
            RedisLock.releaseLock(allLimitingKey,currentLimitingLock);
        }
        return false;
    }

    /**
     * ip限流策略
     * @param ctx
     */
    private void ipLimiting(ChannelHandlerContext ctx){
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        int timeOut = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitTimeout();
        long count = incrIpProtocCount(clientIP,timeOut);
        //超出了单个IP限制访问次数
        if(count >= SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitCount()){
            logger.info("{}超过最大访问次数，已经限制其在{}秒时间内继续访问",clientIP,timeOut);
            NettyUtil.sendError(ctx,HttpResponseStatus.GATEWAY_TIMEOUT);
        }
    }

    /**
     * 增加一次IP访问次数
     * @param ip
     * @return
     */
    public long incrIpProtocCount(String ip,int timeOut){
        Jedis jedis = null;
        long count = 0;
        try{
            jedis = RedisUtil.getJedis();
            count =jedis.incr(IPLimitingkey+ip);
            if(count == 1){
                jedis.expire(IPLimitingkey+ip,timeOut);
            }
        }finally {
            RedisUtil.returnResource(jedis);
        }
        return count;
    }

}
