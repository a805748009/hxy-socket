package com;

import nafos.core.Thread.Processors;
import nafos.core.util.SnowflakeIdWorker;
import nafos.network.bootStrap.NafosServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;


/**
 * @Author 黄新宇
 * @Date 2018/10/10 上午10:57
 * @Description TODO
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com","nafos"})
public class BootRun {
//    public static void main(String[] args) {
//        NafosServer.startup(BootRun.class,args);
//    }

//    public void tt() throws IOException, InterruptedException {
//        Socket socket = new Socket("127.0.0.1", 9988);
//        socket.getOutputStream().write("socket".getBytes());
//        socket.getOutputStream().flush();
//        Thread.sleep(10000);
//        socket.close();
//    }


    public static void main(String[] args) {
        System.out.println(Processors.getProcess());


    }

}
