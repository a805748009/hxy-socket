package com.controller.socket;


import io.netty.channel.Channel;
import nafos.core.annotation.Controller;
import nafos.core.annotation.Handle;
import nafos.core.util.ArrayUtil;


import java.util.Map;

@Controller
public class TestController {

    @Handle(code = 530)
    public void login(Channel channel, Map map, byte[] id){
            channel.writeAndFlush(ArrayUtil.concat(id,BeanJsonToBinaryUtil.to(map))); //channel发送消息方式，暂时只支持二进制发送，所以JSON需要转成字符串然后转byte[]
        }
    }

