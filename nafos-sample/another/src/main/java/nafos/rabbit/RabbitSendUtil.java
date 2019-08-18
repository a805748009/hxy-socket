//package nafos.another.rabbitmq;
//
//import com.rabbitmq.client.AMQP;
//import nafos.core.util.ArrayUtil;
//import nafos.core.util.ObjectUtil;
//import nafos.core.util.ProtoUtil;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeoutException;
//
///**
// * @Author 黄新宇
// * @Date 2018/5/11 下午12:44
// * @Description
// **/
//
//public class RabbitSendUtil {
//
//
//    private final static Map<String,QueueProducer> queueProducerMap = new HashMap<>();
//
//
//    /**
//     * 发送MQ消息
//     * @param queueName
//     * @param code
//     * @param object
//     */
//    public static void sendRabbitMsg(String queueName,int code,Object object){
//        sendRabbitMsg(queueName,code,"",false,null,object);
//    }
//
//    public static void sendRabbitMsg(String queueName, int code, String exchange, AMQP.BasicProperties props, Object object){
//        sendRabbitMsg(queueName,code,exchange,false,props,object);
//    }
//
//    public static void sendRabbitMsg(String queueName, int code, String exchange, boolean mandatory, AMQP.BasicProperties props, Object object){
//        sendRabbitMsg(queueName,code,exchange,mandatory,false,props,object);
//    }
//
//    public static void sendRabbitMsg(String queueName, int code, String exchange, boolean mandatory, boolean immediate, AMQP.BasicProperties props, Object object){
//        QueueProducer queueProducer = getQueueProducer(queueName);
//        try {
//            queueProducer.sendMessage(exchange,mandatory,immediate,props, ArrayUtil.concat(ArrayUtil.intToByteArray(code),ProtoUtil.serializeToByte(object)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private static QueueProducer getQueueProducer(String queueName){
//        //如果不存在次队列
//        if(ObjectUtil.isNull(queueProducerMap.get(queueName))){
//            try {
//                queueProducerMap.put(queueName,new QueueProducer(queueName));
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//            }
//        }
//        return queueProducerMap.get(queueName);
//    }
//
//}
