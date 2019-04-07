package tool.ringbuffer;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import nafos.bootStrap.handle.http.NsRespone;


public class RingbufferTest {
    public static void main(String[] args) {




        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            new NsRespone(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        }
        long end1 = System.currentTimeMillis();

        System.out.println(end1-start1);


    }
}
