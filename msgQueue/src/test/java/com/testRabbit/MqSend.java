package com.testRabbit;

import nafos.core.entry.ClassAndMethod;
import nafos.core.util.SpringApplicationContextHolder;
import nafos.msgQueue.rabbitMq.MqConfig;
import nafos.msgQueue.rabbitMq.RabbitSendUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/11/7 下午7:14
 * @Description TODO
 **/
public class MqSend {

    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(MqListenerStart.class, args);
        SpringApplicationContextHolder.getSpringBeanForClass(MqConfig.class).setMqHost("127.0.0.1");
        SpringApplicationContextHolder.getSpringBeanForClass(MqConfig.class).setMqUserName("hxy");
        SpringApplicationContextHolder.getSpringBeanForClass(MqConfig.class).setMqPassWord("rinzz25889");
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RabbitSendUtil.sendRabbitMsg("tstst",123,new ClassAndMethod(null,null,123));
        }

    }
}
