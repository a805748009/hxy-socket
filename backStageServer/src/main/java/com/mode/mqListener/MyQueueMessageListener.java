package com.mode.mqListener;

import com.result.base.pool.ExecutorPool;
import com.result.serverbootstrap.assist.rabbitMq.QueueConsumer;
import com.result.serverbootstrap.assist.rabbitMq.QueueMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author 黄新宇
 * @Date 2018/5/11 上午9:16
 * @Description TODO
 **/
@Component
public class MyQueueMessageListener implements QueueMessageListener {

    @Override
    public void messageListener(byte[] bytes) {
        MyQueueMessageRoute mq = new MyQueueMessageRoute(bytes);
        ExecutorPool.getInstance().execute(mq);
    }


    public static void startListener(){
        Thread consumerThread = null;
        try {
            consumerThread = new Thread(new QueueConsumer("rinzz.backstage"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        consumerThread.start();
    }

}
