package test.tcp;

import hxy.server.socket.util.ByteUtil;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2019/12/1 14:00
 */
public class TcpTextClientDemo {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                connect();
            }).start();
        }
    }

    public static void connect(){
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



            byte[] msg = "hello".getBytes();
            //服务端socket包方案，在前面加上4个字节的长度.  websocket可以忽略
            byte[] sendMsg = ByteUtil.concat(ByteUtil.intToByteArray(msg.length), msg);

            for (;;) {
                //数据放入缓冲区
                byteBuffer.put(sendMsg);
                //复位buffer
                byteBuffer.flip();
                //写出数据
                socketChannel.write(byteBuffer);
                //清空数据
                byteBuffer.clear();
                Thread.sleep(10);
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
        System.out.println("lengthByte:" + ByteUtil.byteArrayToInt(lengthByte));



        //最终消息体
        byte[] content = new byte[bytes.length - 4];//前端传过来的ID，原样返回
        System.arraycopy(bytes, 4, content, 0, bytes.length - 4);

        //转换成类打印
        String body = new String(content, CharsetUtil.UTF_8);
        System.out.println("body: " + body);
    }
}
