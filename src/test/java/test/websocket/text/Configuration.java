package test.websocket.text;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hxy.server.socket.configuration.SocketConfiguration;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;

/**
 * @ClassName Configuration
 * @Description 创建自定义的objectmapper，否则用默认值
 * @Author hxy
 * @Date 2020/4/17 15:20
 */
@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
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
}
