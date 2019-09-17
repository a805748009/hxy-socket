package nafos.bootStrap.manager;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 黄新宇
 * @Date 2018/10/15 下午4:53
 * @Description channel连接管理，最开始连接的channel都在这里，
 * 登录之后或者通过安全验证确实是有效用户之后删除
 * 否则在一定时间内关闭连接
 **/
public class ChannelConnectManager {
    private static final Logger logger = LoggerFactory.getLogger(ChannelConnectManager.class);

    private static ConcurrentHashMap<Channel, Long> NoSecurityChannelMap = new ConcurrentHashMap<>(1024);

    /**
     * 连接
     *
     * @param channel
     */
    public static void connect(Channel channel) {
        NoSecurityChannelMap.put(channel, System.currentTimeMillis());
    }

    /**
     * 通过安全验证
     *
     * @param channel
     */
    public static void safe(Channel channel) {
        NoSecurityChannelMap.remove(channel);
    }

    /**
     * 关闭不安全的链接
     */
    public static void closeUnSafeChannel(long timeOut) {
        long time = System.currentTimeMillis();
        NoSecurityChannelMap.entrySet().forEach(m -> {
            if (time - m.getValue() > timeOut) {
                m.getKey().close();
                NoSecurityChannelMap.remove(m.getKey());
                logger.debug("删除无效的用户连接：{}", m.getKey().toString());
            }
        });

    }
}
