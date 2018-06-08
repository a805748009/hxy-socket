package com.result.serverbootstrap.assist.rabbitMq;

public interface QueueMessageListener {

    void messageListener(byte[] bytes);
}
