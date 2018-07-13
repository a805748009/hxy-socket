package com.mode.assit.mq;

import com.hxy.nettygo.result.NettyGoConstant;
import com.hxy.nettygo.result.base.tools.CastUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 黄新宇
 * @Date 2018/5/11 下午4:13
 * @Description TODO
 **/
@Configuration
public class MqInit {

    @Value("${mq.open}")
    private boolean open;
    @Value("${mq.host}")
    private String host;
    @Value("${mq.userName}")
    private String userName;
    @Value("${mq.passWord}")
    private String passWord;

    public void initMQ(){
        boolean mqOpen = CastUtil.castBoolean(open);
        String mqHost = CastUtil.castString(host);
        String mqUserName = CastUtil.castString(userName);
        String mqPassWord = CastUtil.castString(passWord);
        NettyGoConstant.setMqMode(mqOpen,mqHost,mqUserName,mqPassWord);
    }
}
