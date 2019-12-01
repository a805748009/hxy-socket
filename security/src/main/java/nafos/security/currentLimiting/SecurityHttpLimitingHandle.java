package nafos.security.currentLimiting;//package nafos.security.currentLimiting;
//
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.handler.codec.http.HttpResponseStatus;
//import nafos.core.util.NettyUtil;
//import nafos.security.redis.SessionForRedis;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.net.InetSocketAddress;
//import java.util.LinkedList;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @Author 黄新宇
// * @Date 2018/10/29 下午4:28
// * @Description 限流器
// **/
//@Component
//@ChannelHandler.Sharable
//public class SecurityHttpLimitingHandle extends ChannelInboundHandlerAdapter {
//    private static final Logger logger = LoggerFactory.getLogger(SecurityHttpLimitingHandle.class);
//
//    @Value("${nafos.security.limitOnType:NO}") //NO 不开启限流  LOCAL 本地单机限流   REDIS redis集群限流
//    public String limitOnType;
//
//    @Value("${nafos.security.iplimitTimeout:2}")
//    public int iplimitTimeout;
//
//    @Value("${nafos.security.iplimitCount:0}")
//    public int iplimitCount;
//
//    @Value("${nafos.security.alllimitTimeout:2}")
//    public int alllimitTimeout;
//
//    @Value("${nafos.security.alllimitCount:0}")
//    public int alllimitCount;
//
//    @Autowired
//    SessionForRedis sessionForRedis;
//
//
//    private final String allLimitingKey = "allLimiting:";
//
//    private static String IPLimitingkey = "ipLimit:";
//
//    private LinkedList<Long> tcpCount = new LinkedList();
//
//    private ConcurrentHashMap<String, LinkedList<Long>> ipLimitMap = new ConcurrentHashMap();
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//
//        //单机限流
//        if ("LOCAL".equals(limitOnType)) {
//            //全限流状态
//            allLimitingForLinkedList(ctx);
//            //IP限流状态
//            ipLimitingForMap(ctx);
//        } else {
//
//            //------------集群限流---基于redis
//
//            //大于0的时候开启IP限流
//            if (iplimitCount > 0) {
//                ipLimitingForRedis(ctx);
//            }
//
//            //总限流
//            if (alllimitCount > 0) {
//                if (allLimitingForRedis(alllimitTimeout * 1000l,
//                        alllimitCount))
//                    return;
//
//                logger.warn("总访问次数超过最大访问次数，已经限制了访问");
//                NettyUtil.sendError(ctx, HttpResponseStatus.SERVICE_UNAVAILABLE);
//            }
//
//        }
//    }
//
//
//    /**
//     * 获取限流权限
//     *
//     * @param ctx
//     */
//    public void allLimitingForLinkedList(ChannelHandlerContext ctx) {
//        if (alllimitCount > 0) {
//            synchronized ("tcpCount") {
//                if (tcpCount.size() > alllimitCount) {
//                    //超过限额，看看时间在不在限定时间内
//                    if (System.currentTimeMillis() - tcpCount.getFirst() >
//                            alllimitTimeout * 1000l) {
//                        tcpCount.add(System.currentTimeMillis());
//                        tcpCount.removeFirst();
//                    } else {
//                        logger.warn("总访问次数超过最大访问次数，已经限制了访问");
//                        NettyUtil.sendError(ctx, HttpResponseStatus.SERVICE_UNAVAILABLE);
//                    }
//
//                } else {
//                    tcpCount.add(System.currentTimeMillis());
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 获取限流权限
//     *
//     * @param millisecond 毫秒数
//     * @param limitCount  限流次数
//     * @return
//     */
//    public boolean allLimitingForRedis(Long millisecond, Integer limitCount) {
//        Long llen = sessionForRedis.llen(allLimitingKey);
//        if (llen < limitCount) {
//            sessionForRedis.lpush(allLimitingKey, System.currentTimeMillis() + "");
//            return true;
//        } else {
//            Long lastTime = Long.parseLong(jedis.lindex(allLimitingKey, -1));
//            if ((System.currentTimeMillis() - lastTime) >= millisecond) {
//                sessionForRedis.lpush(allLimitingKey, System.currentTimeMillis() + "");
//                jedis.ltrim(allLimitingKey, 0, limitCount);
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    /**
//     * ip限流策略
//     *
//     * @param ctx
//     */
//    private void ipLimitingForMap(ChannelHandlerContext ctx) {
//        if (iplimitCount > 0) {
//            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//            String clientIP = insocket.getAddress().getHostAddress();
//            LinkedList<Long> ipTcpList = ipLimitMap.get(clientIP);
//            long count = ipTcpList.size();
//            //超出了单个IP限制访问次数
//            synchronized (clientIP) {
//                if (count > iplimitCount) {
//                    //超过限额，看看时间在不在限定时间内
//                    if (System.currentTimeMillis() - ipTcpList.getFirst() > iplimitTimeout * 1000l) {
//                        ipTcpList.add(System.currentTimeMillis());
//                        ipTcpList.removeFirst();
//                    } else {
//                        logger.warn("总访问次数超过最大访问次数，已经限制了访问");
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
//    /**
//     * ip限流策略
//     *
//     * @param ctx
//     */
//    private void ipLimitingForRedis(ChannelHandlerContext ctx) {
//        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//        String clientIP = insocket.getAddress().getHostAddress();
//        long count = incrIpProtocCount(clientIP, iplimitTimeout);
//        //超出了单个IP限制访问次数
//        if (count >= iplimitCount) {
//            logger.warn("{}超过最大访问次数，已经限制其在{}秒时间内继续访问", clientIP, iplimitTimeout);
//            NettyUtil.sendError(ctx, HttpResponseStatus.GATEWAY_TIMEOUT);
//        }
//    }
//
//
//    /**
//     * 增加一次IP访问次数
//     *
//     * @param ip
//     * @return
//     */
//    public long incrIpProtocCount(String ip, int timeOut) {
//        long count = sessionForRedis.inc(IPLimitingkey + ip);
//        if (count == 1) sessionForRedis.expire(IPLimitingkey + ip, timeOut);
//        return count;
//    }
//
//
//}
