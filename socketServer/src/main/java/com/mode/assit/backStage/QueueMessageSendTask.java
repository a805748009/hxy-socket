package com.mode.assit.backStage;

import com.hxy.nettygo.result.base.config.ConfigForMQConnect;
import com.hxy.nettygo.result.base.pool.ExecutorPool;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.hxy.nettygo.result.serverbootstrap.assist.rabbitMq.QueueProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author 黄新宇
 * @Date 2018/5/11 下午12:44
 * @Description TODO
 **/
@Component
public class QueueMessageSendTask {

    private static QueueProducer queueProducer;


    private QueueProducer getQueueProducer(){
        if(ObjectUtil.isNull(queueProducer)){
            try {
                queueProducer = new QueueProducer("rinzz.backstage");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return queueProducer;
    }



    @Scheduled(cron="0 0/5 * * * ?")
    public void sendOnlineCount(){
        if(!ConfigForMQConnect.MQ_OPEN)
            return;
        getQueueProducer();
        MqMessage ms = new MqMessage("onlineCount",queueProducer);
        ExecutorPool.getInstance().execute(ms);
    }

    @Scheduled(cron="0 0/1 * * * ?")
    public void sendMemory(){
        if(!ConfigForMQConnect.MQ_OPEN)
            return;
        getQueueProducer();
        MqMessage ms = new MqMessage("memory",queueProducer);
        ExecutorPool.getInstance().execute(ms);
    }



}
