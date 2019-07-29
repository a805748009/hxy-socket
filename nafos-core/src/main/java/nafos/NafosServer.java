package nafos;

import nafos.bootStrap.NettyStartup;
import nafos.core.Enums.Connect;
import nafos.core.Enums.Protocol;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.InitMothods;
import nafos.core.mode.RouteFactory;
import nafos.core.mode.runner.NafosRunnerExecute;
import nafos.core.monitor.RunWatch;
import nafos.core.monitor.SystemMonitor;
import nafos.core.monitor.UnSafeSocketChannel;
import nafos.core.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/10/8 下午2:09
 * @Description TODO
 **/
@Component
public class NafosServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyStartup.class);

    @Value("${nafos.httpServerPort:0}")
    private int httpServerPort;
    @Value("${nafos.socketServerPort:0}")
    private int socketServerPort;

    @Value("${nafos.security.channelUnSafeConnectTime:300000}")
    private long channelUnSafeConnectTime;

    private static int httpPort = 0;

    private static int socketPort = 0;

    private static NettyStartup nettyStartup = null;

    private static ApplicationContext ac = null;

    private Class runClazz;

    private boolean isInit = false;


    public NafosServer() {

    }

    public NafosServer(Class clazz) {
        runClazz = clazz;
    }

    private NafosServer init() {
        synchronized (this.getClass()) {
            if (isInit) {
                return this;
            }
            ac = SpringApplicationContextHolder.getContext();
            if (ac == null) {
                if (AnnotationUtils.findAnnotation(runClazz, ComponentScan.class) == null) {
                    throw new IllegalStateException("startup class [" + runClazz.getName() + "] must be Annotation ComponentScan and choose scan package");
                }
                AnnotationConfigApplicationContext annoContext = new AnnotationConfigApplicationContext();
                annoContext.register(runClazz);
                annoContext.refresh();
                ac = annoContext;
            } else {
                logger.info("ApplicationContext is running");
            }
            isInit = true;
            logger.info("context-init:"+ac);
            nettyStartup = ac.getBean(NettyStartup.class);
        }
        setPort();
        InitMothods.init(ac);

        // 配置文件注册事件
        if (channelUnSafeConnectTime > 0) {
            new UnSafeSocketChannel(channelUnSafeConnectTime);
        }

        // 执行开机启动任务
        new NafosRunnerExecute().execute();

        return this;
    }


    private void setPort() {
        httpPort = httpServerPort;
        socketPort = socketServerPort;
    }


    /**
     *  启动HTTP和SOCKET服务
     */
    public NafosServer startupAll() {
        return startupAll(httpPort, socketPort);
    }

    public NafosServer startupAll(int httpPort, int socketPort) {
        new Thread(() -> {
            startupHttp(httpPort);
        }).start();
        startupSocket(socketPort);
        return this;
    }

    public NafosServer startupHttp() {
        return startupHttp(httpPort);
    }

    public NafosServer startupHttp(int port) {
        init();
        nettyStartup.startup(port, Connect.HTTP.name());
        return this;
    }

    public NafosServer startupSocket() {
        return startupSocket(socketPort);
    }

    public NafosServer startupSocket(int port) {
        init();
        nettyStartup.startup(port, Connect.SOCKET.name());
        return this;
    }


    /**
     * 注册停机事件
     *
     * @return
     */

    public NafosServer registShutDown(Runnable target) {
        //0.注册停机事件
        Runtime.getRuntime().addShutdownHook(new Thread(target));
        return this;
    }

    /**
     * 注册handle时间监听，millisecond 每多少毫秒打印一次记录
     *
     * @param millisecond
     * @return
     */
    public NafosServer registRunWatch(long millisecond) {
        RunWatch.openRunWatch();
        if (millisecond > 0) {
            RunWatch.cronPrint(millisecond);
        }
        return this;
    }

    /**
     * 注册打印应用GC和线程日志，millisecond 每多少毫秒打印一次记录
     *
     * @param millisecond
     * @return
     */
    public NafosServer registSystemMonitor(long millisecond) {
        if (millisecond > 0) {
            SystemMonitor.cronAllLog(millisecond);
        }
        return this;
    }

    /**
     * 注册默认的通信协议
     *
     * @param protocol
     * @return
     */
    public NafosServer registDefaultProtocol(Protocol protocol) {
        RouteFactory.defaultProtocol = protocol;
        return this;
    }

    /**
     * 注册socket连接，剔除不安全连接的时间
     * socket连接之后，需要对channel进行ChannelConnectManager.safe(channel) 处理，否则在这个时间后会断开连接。不设置默认不踢
     *
     * @param millisecond
     * @return
     */
    public NafosServer registRemoveUnSafeSocketChannel(long millisecond) {
        if (channelUnSafeConnectTime > 0) {
            throw new IllegalStateException("已在配置文件中注册，请勿反复注册");
        }
        new UnSafeSocketChannel(millisecond);
        return this;
    }

    /**
     * 注册snowflakeIdWorker 唯一id注册器  （存在时间回拨问题，没有绝对唯一，但是日常应用不考虑。如有强制绝对，参考redis集中发id）
     *
     * @param workerId,queneceId
     * @return
     */
    public NafosServer registSnowFlake(long workerId, long queneceId) {
        SnowflakeIdWorker.init(workerId, queneceId);
        return this;
    }

    /**
     * 注册长连接心跳时长，默认5S
     *
     * @param second
     * @return
     */
    public NafosServer registerScoketHeartTimeOut(long second) {
        NettyStartup.setHeartTimeout(second);
        return this;
    }


}
