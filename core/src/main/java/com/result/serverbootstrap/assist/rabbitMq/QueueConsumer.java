package com.result.serverbootstrap.assist.rabbitMq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.result.base.entry.RouteClassAndMethod;
import com.result.base.inits.InitMothods;
import com.result.base.tools.ObjectUtil;
import com.result.base.tools.SpringApplicationContextHolder;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午8:07
 * @Description TODO
 **/
public class QueueConsumer extends MQPoint implements Runnable,Consumer {

    public QueueConsumer(String queueName) throws IOException, TimeoutException {
        super(queueName);
    }

    public void run(){
        try {
            channel.basicConsume(queueName,true,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        System.out.println("------->>>>>>>>Consumer "+consumerTag +" 注册成功");

    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        RouteClassAndMethod messageHandle = InitMothods.getMqQueueMessageLitener();
        if(ObjectUtil.isNotNull(messageHandle)){
            messageHandle.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(messageHandle.getClazz()),messageHandle.getIndex(),bytes);
        }
    }

    @Override
    public void handleCancelOk(String consumerTag) {
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
    }

}