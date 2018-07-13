package com.mode.assit.backStage;

import com.hxy.nettygo.result.serverbootstrap.assist.rabbitMq.QueueProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @Author 黄新宇
 * @Date 2018/5/11 上午10:45
 * @Description TODO
 **/
public class MqMessage implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MqMessage.class);

    private Object object;

    private QueueProducer queueProducer;

    public MqMessage(Object object, QueueProducer queueProducer){
        this.object = object;
        this.queueProducer = queueProducer;
    }

    public void run() {
        sendMessage(object);
    }



    private void sendMessage(Object object){
        try {
            queueProducer.sendMessage(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
