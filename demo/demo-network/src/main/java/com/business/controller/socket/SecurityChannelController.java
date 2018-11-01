package com.business.controller.socket;

import com.business.entry.Curren;
import com.business.entry.bean.User;
import io.netty.channel.Channel;
import nafos.core.annotation.controller.Controller;
import nafos.core.annotation.controller.Handle;
import nafos.core.util.AESUtil;
import nafos.core.util.ObjectUtil;
import nafos.game.manager.ChannelConnectInitialize;
import nafos.security.SecurityUtil;
import nafos.security.manager.ChannelConnectManager;

/**
 * @Author 黄新宇
 * @Date 2018/10/17 上午11:44
 * @Description 连接成功后优先发送这个通知过 nafos自带的sericyty的安全验证，否则在连接五分钟之后将会被视为垃圾连接踢出
 **/
@Controller
public class SecurityChannelController {

    @Handle(code = 1000,runOnWorkGroup = true)
    public void tokenlogin(Channel channel,Curren curren,byte[] id) throws Exception {
        String sessionId = AESUtil.decrypt(curren.getStringParam());
        // 验证token
        if (ObjectUtil.isNull(sessionId) || sessionId.length()!=18||!SecurityUtil.isLogin(sessionId)) {
            return;
        }
        ChannelConnectInitialize.initChannel(channel,sessionId,"message", SecurityUtil.getLoginUser(sessionId,User.class));
        ChannelConnectManager.safe(channel);
    }
}
