package nafos.bootStrap.manager;

import io.netty.channel.Channel;

import nafos.core.annotation.socket.Connect;
import nafos.core.annotation.socket.DisConnect;
import nafos.core.annotation.socket.SocketActive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author 黄新宇
 * @Date 2018/10/15 下午9:15
 * @Description 连接创建或者关闭的管理
 **/
@SocketActive(index = 0)
public class SocketActiveManager {
    private static final Logger logger = LoggerFactory.getLogger(SocketActiveManager.class);

    @Connect
    public void connect(Channel channel) {
        ChannelConnectManager.connect(channel);
        logger.debug("[nafos-socket-connect]:   将channel-{} 连接至非安全线路", channel.toString());
    }


    @DisConnect
    public void Disconnect(Channel channel) {
        ChannelConnectManager.safe(channel);
    }
}
