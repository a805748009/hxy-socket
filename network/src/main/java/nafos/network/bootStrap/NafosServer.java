package nafos.network.bootStrap;

import nafos.core.mode.InitMothods;
import nafos.core.shutdown.ShutDownHandler;
import nafos.network.bootStrap.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/10/8 下午2:09
 * @Description TODO
 **/
public class NafosServer {




    public static void startup(Class<?> clazz,String[] args) {
        //0.注册停机事件
        ShutDownHandler shutDownHandler = new ShutDownHandler();
        shutDownHandler.registerSignal(getOSSignalType());

        // 1.开启springIOC容器
        ApplicationContext ac = SpringApplication.run(clazz, args);
        InitMothods.init(ac);

        // 2.开启nettyServer监听
        ac.getBean(NettyServer.class).serverRun();


    }


    private static String getOSSignalType()
    {
        return System.getProperties().getProperty("os.name").
                toLowerCase().startsWith("win")||System.getProperties().getProperty("os.name").
                toLowerCase().startsWith("mac") ? "INT" : "USR2";//mac下由于一般是idea开发，开发中idea退出传的是INT
    }
}
