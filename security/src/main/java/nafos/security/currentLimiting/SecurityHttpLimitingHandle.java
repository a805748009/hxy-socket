//package nafos.security.currentLimiting;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.http.HttpResponseStatus;
//import nafos.core.util.CastUtil;
//import nafos.core.util.NettyUtil;
//import nafos.core.util.ObjectUtil;
//import nafos.core.util.SpringApplicationContextHolder;
//import nafos.network.bootStrap.netty.handle.currency.HttpLimitingHandle;
//import nafos.security.config.SecurityConfig;
//import nafos.security.redis.RedisLock;
//import nafos.security.redis.RedisUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import redis.clients.jedis.Jedis;
//
//import java.net.InetSocketAddress;
//import java.util.LinkedList;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @Author 黄新宇
// * @Date 2018/10/29 下午4:28
// * @Description 限流器
// **/
//public class SecurityHttpLimitingHandle extends HttpLimitingHandle {
//    private static final Logger logger = LoggerFactory.getLogger(SecurityHttpLimitingHandle.class);
//
//
//
//    private final String allLimitingKey = "allLimiting";
//
//    private static String IPLimitingkey = "ipLimit";
//
//    private LinkedList<Long> tcpCount = new LinkedList();
//
//    private ConcurrentHashMap<String,LinkedList<Long>> ipLimitMap = new ConcurrentHashMap();
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        if("NO".equals(SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getLimitOnType()))
//            return;
//
//        //单机限流
//        if ("LOCAL".equals(SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getLimitOnType())){
//            //全限流状态
//            allLimitingForLinkedList(ctx);
//            //IP限流状态
//            ipLimitingForMap(ctx);
//        }else{
//
//            //------------集群限流---基于redis
//
//            //大于0的时候开启IP限流
//            if (SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitCount() > 0) {
//                ipLimitingForRedis(ctx);
//            }
//
//            //总限流
//            if (SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getAlllimitCount() > 0) {
//                if(allLimitingForRedis(SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getAlllimitTimeout()*1000l,
//                        SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getAlllimitCount()))
//                    return;
//
//                logger.info("总访问次数超过最大访问次数，已经限制了访问");
//                NettyUtil.sendError(ctx,HttpResponseStatus.GATEWAY_TIMEOUT);
//            }
//
//        }
//    }
//
//
//
//    /**
//     * 获取限流权限
//     * @param ctx
//     */
//    public  void allLimitingForLinkedList(ChannelHandlerContext ctx){
//        int allLimitCount = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getAlllimitCount();
//        if (allLimitCount > 0) {
//            synchronized ("tcpCount"){
//                if(tcpCount.size()>allLimitCount){
//                    //超过限额，看看时间在不在限定时间内
//                    if(System.currentTimeMillis() - tcpCount.getFirst() >
//                            SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getAlllimitTimeout()*1000l){
//                        tcpCount.add(System.currentTimeMillis());
//                        tcpCount.removeFirst();
//                    }else{
//                        logger.info("总访问次数超过最大访问次数，已经限制了访问");
//                        NettyUtil.sendError(ctx,HttpResponseStatus.GATEWAY_TIMEOUT);
//                    }
//
//                }else{
//                    tcpCount.add(System.currentTimeMillis());
//                }
//            }
//        }
//    }
//
//
//
//    /**
//     * 获取限流权限
//     * @param millisecond 毫秒数
//     * @param limitCount 限流次数
//     * @return
//     */
//    public  boolean allLimitingForRedis(Long millisecond, Integer limitCount){
//        String currentLimitingLock = RedisLock.lockWithTimeout(allLimitingKey,300,1000);
//        Jedis jedis = null;
//        try {
//            jedis = RedisUtil.getJedis();
//            if(ObjectUtil.isNotNull(currentLimitingLock)){
//                Long llen = jedis.llen(allLimitingKey);
//                if(llen < limitCount){
//                    jedis.lpush(allLimitingKey, System.currentTimeMillis()+"");
//                    return true;
//                }else{
//                    Long lastTime = Long.parseLong(jedis.lindex(allLimitingKey, -1));
//                    if((System.currentTimeMillis() - lastTime) >= millisecond){
//                        jedis.lpush(allLimitingKey, System.currentTimeMillis()+"");
//                        jedis.ltrim(allLimitingKey, 0, limitCount);
//                        return true;
//                    }
//                }
//            }
//        }finally {
//            RedisUtil.returnResource(jedis);
//            RedisLock.releaseLock(allLimitingKey,currentLimitingLock);
//        }
//        return false;
//    }
//
//
//
//    /**
//     * ip限流策略
//     * @param ctx
//     */
//    private void ipLimitingForMap(ChannelHandlerContext ctx){
//        if (SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitCount() > 0) {
//            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//            String clientIP = insocket.getAddress().getHostAddress();
//            int timeOut = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitTimeout();
//            LinkedList<Long> ipTcpList = ipLimitMap.get(clientIP);
//            long count = ipTcpList.size();
//            //超出了单个IP限制访问次数
//            synchronized (clientIP) {
//                if (count > SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitCount()) {
//                    //超过限额，看看时间在不在限定时间内
//                    if (System.currentTimeMillis() - ipTcpList.getFirst() > timeOut * 1000l) {
//                        ipTcpList.add(System.currentTimeMillis());
//                        ipTcpList.removeFirst();
//                    } else {
//                        logger.info("总访问次数超过最大访问次数，已经限制了访问");
//                        NettyUtil.sendError(ctx, HttpResponseStatus.GATEWAY_TIMEOUT);
//                    }
//                } else {
//                    ipTcpList.add(System.currentTimeMillis());
//                }
//            }
//        }
//    }
//
//
//
//
//    /**
//     * ip限流策略
//     * @param ctx
//     */
//    private void ipLimitingForRedis(ChannelHandlerContext ctx){
//        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//        String clientIP = insocket.getAddress().getHostAddress();
//        int timeOut = SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitTimeout();
//        long count = incrIpProtocCount(clientIP,timeOut);
//        //超出了单个IP限制访问次数
//        if(count >= SpringApplicationContextHolder.getSpringBeanForClass(SecurityConfig.class).getIplimitCount()){
//            logger.info("{}超过最大访问次数，已经限制其在{}秒时间内继续访问",clientIP,timeOut);
//            NettyUtil.sendError(ctx,HttpResponseStatus.GATEWAY_TIMEOUT);
//        }
//    }
//
//
//
//    /**
//     * 增加一次IP访问次数
//     * @param ip
//     * @return
//     */
//    public long incrIpProtocCount(String ip,int timeOut){
//        Jedis jedis = null;
//        long count = 0;
//        try{
//            jedis = RedisUtil.getJedis();
//            count =jedis.incr(IPLimitingkey+ip);
//            if(count == 1){
//                jedis.expire(IPLimitingkey+ip,timeOut);
//            }
//        }finally {
//            RedisUtil.returnResource(jedis);
//        }
//        return count;
//    }
//
//
//
//
//
//
//
//}
