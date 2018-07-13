package com.hxy.nettygo.result.serverbootstrap.assist.rabbitMq;

public interface QueueMessageListener {

    void messageListener(byte[] bytes);
}
