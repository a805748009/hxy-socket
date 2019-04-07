package nafos.core.mode.shutdown;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @Author 黄新宇
 * @Date 2018/8/23 上午10:56
 * @Description TODO
 **/
public class ShutDownHandler implements SignalHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShutDownHandler.class);

    private ShutDownHandleInterface handleInterface;

    public void registerSignal(ShutDownHandleInterface handleInterface) {
        Signal signal = new Signal(getOSSignalType());
        this.handleInterface = handleInterface;
        Signal.handle(signal, this);
    }

    private String getOSSignalType() {
        return System.getProperties().getProperty("os.name").
                toLowerCase().startsWith("win") || System.getProperties().getProperty("os.name").
                toLowerCase().startsWith("mac") ? "INT" : "USR2";//mac下由于一般是idea开发，开发中idea退出传的是INT
    }

    @Override
    public void handle(Signal signal) {
        if (signal.getName().equals("USR2") || signal.getName().equals("INT") || signal.getName().equals("HUP")) {
            logger.info("收到关闭指令：" + signal.getName() + "======正在进行关机处理事件，请稍后");
            handleInterface.run();
        } else {
            logger.info("无效的关闭指令，将不执行关机处理事件");
        }
        logger.info("事件执行完毕，准备关机==========");
        Runtime.getRuntime().exit(0);
    }

}
