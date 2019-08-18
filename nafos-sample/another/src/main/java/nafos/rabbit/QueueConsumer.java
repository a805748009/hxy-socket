//package nafos.another.rabbitmq;
//
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.Consumer;
//import com.rabbitmq.client.Envelope;
//import com.rabbitmq.client.ShutdownSignalException;
//import nafos.core.entry.ClassAndMethod;
//import nafos.core.helper.SpringApplicationContextHolder;
//import nafos.core.util.ArrayUtil;
//import nafos.core.util.ObjectUtil;
//import nafos.core.util.ProtoUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.util.concurrent.TimeoutException;
//
///**
// * @Author 黄新宇
// * @Date 2018/5/10 下午8:07
// * @Description TODO
// **/
//public class QueueConsumer extends MQPoint implements Runnable,Consumer {
//
//    private final static Logger logger = LoggerFactory.getLogger(QueueConsumer.class);
//
//    public QueueConsumer(String queueName) throws IOException, TimeoutException {
//        super(queueName);
//    }
//
//    public void run(){
//        try {
//            channel.basicConsume(queueName,true,this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void handleConsumeOk(String consumerTag) {
//        logger.info("------->>>>>>>>Consumer {} 注册成功",consumerTag);
//
//    }
//
//    @Override
//    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
//        byte[] idByte = new byte[4];
//        System.arraycopy(bytes, 0, idByte, 0, 4);
//        int code = ArrayUtil.byteArrayToInt(idByte);
//
//        byte[] messageBody = new byte[bytes.length-4];
//        if(bytes.length>4){
//            System.arraycopy(bytes, 4, messageBody, 0, bytes.length-4);
//        }
//
//        ClassAndMethod classAndMethod = (ClassAndMethod) QueueMessageHandleInit.getHandleRoute().get(code);
//        if(ObjectUtil.isNotNull(classAndMethod)){
//            classAndMethod.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(classAndMethod.getClazz()),
//                    classAndMethod.getIndex(), ProtoUtil.deserializeFromByte(messageBody,classAndMethod.getParamType()));
//        }
//    }
//
//    @Override
//    public void handleCancelOk(String consumerTag) {
//    }
//
//    @Override
//    public void handleCancel(String consumerTag) throws IOException {
//    }
//
//    @Override
//    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
//    }
//
//    @Override
//    public void handleRecoverOk(String consumerTag) {
//    }
//
//}