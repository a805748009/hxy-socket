package nafos.core.monitor;

import nafos.bootStrap.manager.ChannelConnectManager;

import java.util.concurrent.TimeUnit;

public class UnSafeSocketChannel {

    public UnSafeSocketChannel(long millisecond) {
        new Thread(() -> {
            Thread.currentThread().setName("UnSafeSocketChannel");
            for (;;) {
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(30));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ChannelConnectManager.closeUnSafeChannel(millisecond);
            }
        }).start();
    }
}
