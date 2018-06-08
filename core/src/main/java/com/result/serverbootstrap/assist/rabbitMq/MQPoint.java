package com.result.serverbootstrap.assist.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.result.base.config.ConfigForMQConnect;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午8:06
 * @Description TODO
 **/
public abstract class MQPoint {
    protected Channel channel;
    protected Connection connection;
    protected String queueName;

    /**
    * @Author 黄新宇
    * @date 2018/5/10 下午9:52
    * @Description(获取新的连接)
    * @param [queueName]
    * @return
    */
    public MQPoint(String queueName) throws IOException, TimeoutException {
        this.queueName = queueName;

        //创建连接工厂
        ConnectionFactory cf = new ConnectionFactory();

        //设置rabbitmq服务器地址
        cf.setHost(ConfigForMQConnect.MQ_HOST);

        //设置rabbitmq服务器用户名
        cf.setUsername(ConfigForMQConnect.MQ_USERNAME);

        //设置rabbitmq服务器密码
        cf.setPassword(ConfigForMQConnect.MQ_PASSWORD);

        //获取一个新的连接
        connection = cf.newConnection();

        //创建一个通道
        channel = connection.createChannel();

        //申明一个队列，如果这个队列不存在，将会被创建
//        durable：true、false true：在服务器重启时，能够存活
//        exclusive ：是否为当前连接的专用队列，在连接断开后，会自动删除该队列，生产环境中应该很少用到吧。
//        autodelete：当没有任何消费者使用时，自动删除该队列。
        channel.queueDeclare(queueName, false, false, false, null);
    }




    /**
    * @Author 黄新宇
    * @date 2018/5/10 下午9:14
    * @Description(关闭连接)
    * @param
    * @return void
    */

    public void close() throws IOException, TimeoutException {
        this.channel.close();
        this.connection.close();
    }
}