package com.hxy.nettygo.result.serverbootstrap.shutdown;

import com.hxy.nettygo.result.base.entry.RouteClassAndMethod;
import com.hxy.nettygo.result.base.inits.InitMothods;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.hxy.nettygo.result.base.tools.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @Author 黄新宇
 * @Date 2018/8/23 上午10:56
 * @Description TODO
 **/
public class ShutDownHandler implements SignalHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShutDownHandler.class);

    public void registerSignal(String signalName) {
        Signal signal = new Signal(signalName);
        Signal.handle(signal, this);
    }

    @Override
    public void handle(Signal signal) {
        if (signal.getName().equals("USR2")||signal.getName().equals("INT") || signal.getName().equals("HUP")) {
            logger.info("收到关闭指令："+signal.getName()+"======正在进行关机处理事件，请稍后");
            RouteClassAndMethod filter = InitMothods.getShutDownFilter();
            if(ObjectUtil.isNotNull(filter)){
                filter.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(filter.getClazz()),filter.getIndex(),null);
            }
        }  else {
            logger.info("无效的关闭指令，将不执行关机处理事件");
        }
        logger.info("事件执行完毕，准备关机==========");
        Runtime.getRuntime().exit(0);
    }

}
