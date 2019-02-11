package com.disruptor;


import com.MyDisruptor;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyDataEventHandler implements EventHandler<MyDataEvent> {
    private static Logger logger = LoggerFactory.getLogger(MyDataEventHandler.class);

    @Override
    public void onEvent(MyDataEvent myDataEvent, long arg1, boolean arg2)
            throws Exception {
        // 处理事件 ....
        logger.info("处理事件，打印数据： " + myDataEvent.getValue());
    }

}
