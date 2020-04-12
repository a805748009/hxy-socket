package test.websocket.bytearray;

import hxy.server.socket.util.ProtoUtil;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

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
        System.out.println("open----");
    }

    @Override
    public void onMessage(String s) {
        System.out.println("text message-" + s);
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
        System.out.println("close----");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("error----");
    }

    public static void main(String[] args) throws InterruptedException {
        URI uri = URI.create("ws://127.0.0.1:9090");

        MyWebSocketClient client = new MyWebSocketClient(uri);
        client.connect();
        while (!client.getReadyState().equals(READYSTATE.OPEN)) {
            Thread.sleep(10);
        }
        System.out.println("=====");
        for (;;){
            Thread.sleep(1000);
            client.send(ProtoUtil.serializeToByte(new Person("321321")));
        }
    }
}
