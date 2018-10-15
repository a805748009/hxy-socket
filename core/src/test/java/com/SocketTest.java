package com;

import nafos.core.util.ArrayUtil;
import nafos.core.util.HttpUtil;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SnowflakeIdWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author 黄新宇
 * @Date 2018/10/11 上午11:21
 * @Description TODO
 **/
public class SocketTest {
    public static void main(String[] args) throws IOException, InterruptedException {
//        Socket socket = new Socket("127.0.0.1", 9988);
//        socket.getOutputStream().write("socket".getBytes());
//        socket.getOutputStream().flush();
//        Thread.sleep(10000);
//        socket.close();

//
//            String po = SnowflakeIdWorker.getStringId();
//        for(int i =0;i<100;i++){
//            int p = po.hashCode();
//            System.out.println(Math.abs(p%10));
//        }
        Socket socket = new Socket("127.0.0.1", 9988);
//        socket.getOutputStream().write("2".getBytes());
//        socket.getOutputStream().flush();
//        Thread.sleep(2000);

//
        byte[] b = "{\"22\":1}".getBytes();
        byte[] m = ArrayUtil.concat(ArrayUtil.concat(ArrayUtil.intToByteArray(1000),ArrayUtil.intToByteArray(1000)),b);
        byte[] p = ArrayUtil.concat(ArrayUtil.intToByteArray(m.length),m);
        System.out.println(p.length);
        socket.getOutputStream().write(p);
        socket.getOutputStream().flush();


        Scanner scanner=new Scanner(System.in);
        BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String readline;
        while(true)                                                 //循环发消息
        {
            System.out.println(1);
            readline=scanner.nextLine();
//            write.write(readline+'\n');                            //write()要加'\n'
//            write.flush();
//			socket.shutdownOutput();
            System.out.println(in.readLine());
        }


//        socket.close();

    }


}
