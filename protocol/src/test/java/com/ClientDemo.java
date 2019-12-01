package com;

import nafos.protocol.ProtoUtil;
import nafos.server.util.ArrayUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2019/12/1 14:00
 */
public class ClientDemo {

    public static void main(String[] args) {
        //创建连接的地址
        InetSocketAddress InetSocketAddress = new InetSocketAddress("127.0.0.1", 9090);
        //声明连接通道
        SocketChannel socketChannel = null;
        //建立向数据的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            //打开通道
            socketChannel = SocketChannel.open();
            //进行连接
            socketChannel.connect(InetSocketAddress);
            openReadThread(socketChannel);


            User user = new User();
            user.setUserName("小白");
            user.setPassword("123");

            byte[] code = ArrayUtil.intToByteArray(500);
            byte[] scCode = ArrayUtil.concat(code, code);
            byte[] msg = ArrayUtil.concat(scCode, ProtoUtil.serializeToByte(user));
            //服务端socket包方案，在前面加上4个字节的长度.  websocket可以忽略
            byte[] sendMsg = ArrayUtil.concat(ArrayUtil.intToByteArray(msg.length), msg);

            for (int i = 0; i < 100; i++) {
                //数据放入缓冲区
                byteBuffer.put(sendMsg);
                //复位buffer
                byteBuffer.flip();
                //写出数据
                socketChannel.write(byteBuffer);
                //清空数据
                byteBuffer.clear();
                System.out.println(i);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     *@Description 后台线程循环接收服务端发过来的值
     *@Author xinyu.huang
     *@Time 2019/12/1 14:29
     */
    private static void openReadThread(SocketChannel socketChannel) {
        new Thread(() -> {
            try {
                for (; ; ) {
                    readReturn(socketChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /***
     *@Description 消息读取
     *@Author xinyu.huang
     *@Time 2019/12/1 14:29
     */
    private static void readReturn(SocketChannel socketChannel) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int count = socketChannel.read(readBuffer);
        if (count == -1) {
            System.out.println("读取不到数据，通道关闭了！");
            return;
        }
        //若有数据则进行读取，需要先切换到读取的模式
        readBuffer.flip();
        //创建byte数组，接受缓冲区的数据
        byte[] bytes = new byte[readBuffer.remaining()];
        //接收数据
        readBuffer.get(bytes);

        //服务端封装的前四个字节（标明包长度，解决粘包的问题）
        byte[] lengthByte = new byte[4];
        System.arraycopy(bytes, 0, lengthByte, 0, 4);
        System.out.println("lengthByte:" + ArrayUtil.byteArrayToInt(lengthByte));

        //服务端回调的客户端ID
        byte[] idByte = new byte[4];
        System.arraycopy(bytes, 4, idByte, 0, 4);
        System.out.println("idByte:" + ArrayUtil.byteArrayToInt(idByte));

        //最终消息体
        byte[] content = new byte[bytes.length - 8];//前端传过来的ID，原样返回
        System.arraycopy(bytes, 8, content, 0, bytes.length - 8);

        //转换成类打印
        String body = ProtoUtil.deserializeFromByte(content, User.class).toString();
        System.out.println("body: " + body);
    }
}
