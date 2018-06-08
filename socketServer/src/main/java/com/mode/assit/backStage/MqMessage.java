package com.mode.assit.backStage;

import com.result.base.cache.IoCache;
import com.result.base.entry.backStageBean.ActiveCount;
import com.result.base.entry.backStageBean.JVMmemory;
import com.result.base.tools.CastUtil;
import com.result.base.tools.DateUtil;
import com.result.base.tools.SnowflakeIdWorker;
import com.result.serverbootstrap.assist.rabbitMq.QueueProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;


/**
 * @Author 黄新宇
 * @Date 2018/5/11 上午10:45
 * @Description TODO
 **/
public class MqMessage implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MqMessage.class);

    private String uri;

    private QueueProducer queueProducer;

    public MqMessage(String uri, QueueProducer queueProducer){
        this.uri = uri;
        this.queueProducer = queueProducer;
    }

    @Override
    public void run() {
        switch(uri)
        {
            case "onlineCount":
                sendOnlineCount();
                break;
            case "memory":
                sendMemory();
                break;
            default:logger.error("------------->>>>>>>>>找不到方法执行");
        }
    }

    //获取各个namespace中在线人数
    public void sendOnlineCount(){
        IoCache.spaceClientMap.keySet().forEach((String gameName) ->{
            int onlineCount = IoCache.spaceClientMap.get(gameName).size();
            String id = CastUtil.castString(SnowflakeIdWorker.getSnowflakeIdWorker().nextId());
            sendMessage(new ActiveCount(uri,id,onlineCount, DateUtil.getNowTime(),gameName));
        });
    }

    //获取当前JVM内存使用情况
    public  void sendMemory(){
        MemoryMXBean mm =(MemoryMXBean) ManagementFactory.getMemoryMXBean();
        String memoryUse = CastUtil.castString(mm.getHeapMemoryUsage().getUsed());
        String id = CastUtil.castString(SnowflakeIdWorker.getSnowflakeIdWorker().nextId());
        sendMessage(new JVMmemory("memory",id,memoryUse,"H5SOCKET", DateUtil.getNowTime()));
    }


    private void sendMessage(Object object){
        try {
            queueProducer.sendMessage(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
