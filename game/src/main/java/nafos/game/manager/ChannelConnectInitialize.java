package nafos.game.manager;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import nafos.game.entry.BaseUser;
import nafos.game.relation.Client;
import nafos.game.relation.UserClient;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/10/15 下午3:49
 * @Description TODO
 **/
public class ChannelConnectInitialize {

    /**
     * 登录后初始化channel信息，才能使用room，client等
     * @param channel
     * @param token
     * @param nameSpace
     * @param gameUserInfo
     */
    public static void initChannel(Channel channel, String token, String nameSpace, BaseUser gameUserInfo){
        channel.attr(AttributeKey.valueOf("client")).set(new Client(channel,gameUserInfo));
        channel.attr(AttributeKey.valueOf("nameSpace")).set(nameSpace);
        channel.attr(AttributeKey.valueOf("token")).set(token);
        UserClient.setClient(gameUserInfo.getUserId(), (Client) channel.attr(AttributeKey.valueOf("client")).get());
    }
}
