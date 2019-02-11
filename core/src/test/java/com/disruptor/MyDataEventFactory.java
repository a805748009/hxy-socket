package com.disruptor;


import com.lmax.disruptor.EventFactory;

public class MyDataEventFactory implements EventFactory<MyDataEvent> {

    @Override
    public MyDataEvent newInstance() {
        return new MyDataEvent();
    }

}
