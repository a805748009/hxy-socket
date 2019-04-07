//package com.testRabbit;
//
//import nafos.core.util.SpringApplicationContextHolder;
//import nafos.msgQueue.rabbitMq.MqConfig;
//import nafos.msgQueue.rabbitMq.QueueConsumer;
//import nafos.msgQueue.rabbitMq.QueueMessageHandleInit;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.ComponentScan;
//
//import java.io.IOException;
//import java.util.concurrent.TimeoutException;
//
///**
// * @Author 黄新宇
// * @Date 2018/11/7 下午6:57
// * @Description TODO
// **/
//@SpringBootApplication
//@ComponentScan(basePackages = {"com","nafos"})
//public class MqListenerStart {
//
//
//    public static void main(String[] args) {
//        ApplicationContext ac = SpringApplication.run(MqListenerStart.class, args);
//        SpringApplicationContextHolder.getSpringBeanForClass(MqConfig.class).setMqHost("127.0.0.1");
//        SpringApplicationContextHolder.getSpringBeanForClass(MqConfig.class).setMqUserName("hxy");
//        SpringApplicationContextHolder.getSpringBeanForClass(MqConfig.class).setMqPassWord("rinzz25889");
//        try {
//            new QueueConsumer("tstst").run();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
//    }
//}
