package hxy.server.socket.engine;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hxy.server.socket.anno.ExceptionHandler;
import hxy.server.socket.anno.Socket;
import hxy.server.socket.configuration.SocketConfiguration;
import hxy.server.socket.engine.factory.CodeHandlerRouteFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Map;

@Configuration
public class EngineConfiguration {
    private static Logger logger = LoggerFactory.getLogger(EngineConfiguration.class);

    @Bean
    public EngineStarter engineStarter() {
        return new EngineStarter();
    }


    @Bean
    public SocketMsgHandler socketMsgHandler(ApplicationContext context) {
        String SIMPLE_CODE_HANDLER_NAME = "hxy.server.socket.engine.SimpleCodeHandler";
        // 开启系统提供的code解析器
        boolean isEnableSimpleCodeHandler = context.containsBean(SIMPLE_CODE_HANDLER_NAME);
        if (isEnableSimpleCodeHandler) {
            CodeHandlerRouteFactory.load(context);
            logger.info(">>>>>>enable SimpleCodeHandler: {}", SIMPLE_CODE_HANDLER_NAME);
            return (SocketMsgHandler) context.getBean(SIMPLE_CODE_HANDLER_NAME);
        }
        //寻找用户自定义消息解析器第一个
        SocketMsgHandler socketMsgHandler = null;
        Map<String, Object> taskBeanMap = context.getBeansWithAnnotation(Socket.class);
        for (Map.Entry<String, Object> stringObjectEntry : taskBeanMap.entrySet()) {
            if (stringObjectEntry.getValue() instanceof SocketMsgHandler) {
                socketMsgHandler = (SocketMsgHandler) stringObjectEntry.getValue();
                logger.info(">>>>>>init SocketMsgHandler: {}", stringObjectEntry.getKey());
                break;
            }
        }
        return socketMsgHandler;
    }

    @Bean
    @ConditionalOnMissingBean(name="objectMapper")
    public ObjectMapper objectMapper(SocketConfiguration socketConfiguration){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(socketConfiguration.getJacksonPropertyNamingStrategy().getPropertyNamingStrategy());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(fmt);
        return objectMapper;
    }

    @Bean
    @ConditionalOnMissingBean(annotation= ExceptionHandler.class)
    public HandlerExceptionAdvice exceptionHandler(){
        return (ctx, e) -> e.printStackTrace();
    }



}
