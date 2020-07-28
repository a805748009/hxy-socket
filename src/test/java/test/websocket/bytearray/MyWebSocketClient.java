package test.websocket.bytearray;

import hxy.server.socket.util.ProtoUtil;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description
 * @Author xinyu.huang
 * @Time 2020/4/12 18:04
 */
public class MyWebSocketClient extends WebSocketClient {
    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
//        System.out.println("open----");
    }

    @Override
    public void onMessage(String s) {
//        System.out.println("text message-" + s);
    }

    public void onMessage(ByteBuffer bytes) {
        byte[] contentBytes = decode(bytes);
        System.out.println("byte message:" + ProtoUtil.deserializeFromByte(contentBytes, Person.class));
    }

    public byte[] decode(ByteBuffer bytes) {
        int len = bytes.limit() - bytes.position();
        byte[] bytes1 = new byte[len];
        bytes.get(bytes1);
        return bytes1;
    }


    @Override
    public void onClose(int i, String s, boolean b) {
//        System.out.println("close----");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("error----");
    }

    public static void main(String[] args) throws InterruptedException {
        List<MyWebSocketClient> list = new CopyOnWriteArrayList();
        new Thread(() -> {
            for (int i = 0; i < 3000; i++) {

                URI uri = URI.create("ws://127.0.0.1:9090");
                MyWebSocketClient client = new MyWebSocketClient(uri);
                client.connect();
                while (!client.getReadyState().equals(READYSTATE.OPEN)) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String p ="1000|1000|{\"app_id\":\"GLOBAL\",\"user_id\":\"123\"}";
                client.send(p);

                list.add(client);
            }
        }
        ).start();

        new Thread(() -> {
            for (; ; ) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (MyWebSocketClient myWebSocketClient : list) {
                    System.out.println("send"+list.size());
                    myWebSocketClient.send("");
                }
            }
        }).start();

        Thread.sleep(500000);
    }
}
