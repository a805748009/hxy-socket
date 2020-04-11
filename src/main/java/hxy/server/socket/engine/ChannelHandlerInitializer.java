package hxy.server.socket.engine;

import hxy.server.socket.anno.Socket;
import hxy.server.socket.engine.factory.CodeHandlerRouteFactory;
import hxy.server.socket.util.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @Description handler初始化
 * @Author xinyu.huang
 * @Time 2020/4/8 22:33
 */
public class ChannelHandlerInitializer {

    private static Logger logger = LoggerFactory.getLogger(ChannelHandlerInitializer.class);

    private static ApplicationContext context = SpringApplicationContextHolder.getApplicationContext();

    private static SocketMsgHandler socketMsgHandler = null;

    private static String SIMPLE_CODE_HANDLER_NAME = "hxy.server.socket.engine.SimpleCodeHandler";

    public static SocketMsgHandler getSocketMsgHandler() {
        return socketMsgHandler;
    }

    public static void chooseMsgHandler() {
        // 开启系统提供的code解析器
        boolean isEnableSimpleCodeHandler = context.containsBean(SIMPLE_CODE_HANDLER_NAME);
        if (isEnableSimpleCodeHandler) {
            socketMsgHandler = (SocketMsgHandler) context.getBean(SIMPLE_CODE_HANDLER_NAME);
            CodeHandlerRouteFactory.load(context);
            logger.info(">>>>>>enable SocketMsgHandler: {}", SIMPLE_CODE_HANDLER_NAME);
            return;
        }
        //寻找用户自定义消息解析器第一个
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(Socket.class);
        for (Map.Entry<String, Object> stringObjectEntry : taskBeanMap.entrySet()) {
            if (stringObjectEntry.getValue() instanceof SocketMsgHandler) {
                socketMsgHandler = (SocketMsgHandler) stringObjectEntry.getValue();
                logger.info(">>>>>>init SocketMsgHandler: {}", stringObjectEntry.getKey());
                break;
            }
        }
    }


}
