package com;

import nafos.network.bootStrap.NafosServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.net.Socket;


/**
 * @Author 黄新宇
 * @Date 2018/10/10 上午10:57
 * @Description TODO
 **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
@SpringBootApplication
@ComponentScan(basePackages = {"com","nafos.core"})
public class BootRun {
    public static void main(String[] args) {
        NafosServer.startup(BootRun.class,args);
    }

    public void tt() throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 9988);
        socket.getOutputStream().write("socket".getBytes());
        socket.getOutputStream().flush();
        Thread.sleep(10000);
        socket.close();
    }
}
