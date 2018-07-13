package com.mode.assit.backStage;

import com.mode.assit.druidDataSource.MyDruidStatLogger;
import com.hxy.nettygo.result.base.config.ConfigForMQConnect;
import com.hxy.nettygo.result.base.entry.backStageBean.LoginTime;
import com.hxy.nettygo.result.base.entry.backStageBean.UserCount;
import com.hxy.nettygo.result.base.pool.ExecutorPool;
import com.hxy.nettygo.result.base.tools.CastUtil;
import com.hxy.nettygo.result.base.tools.DateUtil;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.hxy.nettygo.result.base.tools.SnowflakeIdWorker;
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

    private static QueueMessageSendTask queueMessageSendTask = new QueueMessageSendTask();







    @Scheduled(cron="0 0/30 * * * ?")
    public void sendUserCount(){
        if(!ConfigForMQConnect.MQ_OPEN)
            return;
        getQueueProducer();
        int userCount = 2;//userDao.getUserCount();
        String id = CastUtil.castString(SnowflakeIdWorker.getSnowflakeIdWorker().nextId());
        UserCount uc = new UserCount("userCount",id,userCount,"H5", DateUtil.getNowTime());
        MqMessage ms = new MqMessage(uc,queueProducer);
        ExecutorPool.getInstance().execute(ms);
    }

    @Scheduled(cron="0 0 0/2 * * ?")
    public void sendSqlMonitor(){
        if(!ConfigForMQConnect.MQ_OPEN)
            return;
        getQueueProducer();
        MyDruidStatLogger.getList().forEach(druidMonitor ->{
            druidMonitor.setUri("druidMonitor");
            druidMonitor.setId(CastUtil.castString(SnowflakeIdWorker.getSnowflakeIdWorker().nextId()));
            MqMessage ms = new MqMessage(druidMonitor,queueProducer);
            ExecutorPool.getInstance().execute(ms);
        });
    }


    public void sendLoginUser(String userId,String gameType){
        if(!ConfigForMQConnect.MQ_OPEN)
            return;
        getQueueProducer();
        String id = CastUtil.castString(SnowflakeIdWorker.getSnowflakeIdWorker().nextId());
        LoginTime lt = new LoginTime(id,userId, DateUtil.getNowTime(),gameType);
        lt.setUri("loginTime");
        MqMessage ms = new MqMessage(lt,queueProducer);
        ExecutorPool.getInstance().execute(ms);
    }



    private QueueProducer getQueueProducer(){
        if(ObjectUtil.isNull(queueProducer)){
            try {
                queueProducer = new QueueProducer("test");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return queueProducer;
    }

    public static QueueMessageSendTask getInstance(){
        return queueMessageSendTask;
    }


}
