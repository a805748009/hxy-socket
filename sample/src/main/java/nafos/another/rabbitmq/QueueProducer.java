package nafos.another.rabbitmq;

import com.rabbitmq.client.AMQP.BasicProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author 黄新宇
 * @Date 2018/5/10 下午8:41
 * @Description TODO
 **/
public class QueueProducer extends MQPoint {

    public QueueProducer(String queueName) throws IOException,TimeoutException {
        super(queueName);
    }

    public void sendMessage(byte[] content) throws IOException{
        channel.basicPublish("", queueName, null, content);
    }

    public void sendMessage(String exchange, BasicProperties props,byte[] content) throws IOException{
        channel.basicPublish(exchange, queueName, props, content);
    }

    public void sendMessage(String exchange, boolean mandatory, BasicProperties props, byte[] content) throws IOException{
        channel.basicPublish(exchange, queueName,mandatory, props, content);
    }

    public void sendMessage(String exchange, boolean mandatory, boolean immediate, BasicProperties props, byte[] content) throws IOException{
        channel.basicPublish(exchange, queueName, mandatory,immediate,props,content);
    }



    //exchange 交换机
    //routingKey：路由键，#匹配0个或多个单词，*匹配一个单词
    //mandatory：true：如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，那么会调用basic.return方法将消息返还给生产者。false：出现上述情形broker会直接将消息扔掉
    //immediate：true：如果exchange在将消息route到queue(s)时发现对应的queue上没有消费者，那么这条消息不会放入队列中。当与消息routeKey关联的所有queue(一个或多个)都没有消费者时，该消息会通过basic.return方法返还给生产者。
    //BasicProperties ：需要注意的是BasicProperties.deliveryMode，0:不持久化 1：持久化 这里指的是消息的持久化，配合channel(durable=true),queue(durable)可以实现，即使服务器宕机，消息仍然保留

}
