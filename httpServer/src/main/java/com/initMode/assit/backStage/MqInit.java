package com.initMode.assit.backStage;

import com.result.NettyGoConstant;
import com.result.base.tools.CastUtil;
import com.result.base.tools.ConfigUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ResourceBundle;

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
