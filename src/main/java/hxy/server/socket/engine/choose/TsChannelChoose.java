package hxy.server.socket.engine.choose;

import hxy.server.socket.engine.factory.SocketHandlerBuilder;
import hxy.server.socket.engine.factory.TcpsocketHandlerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName TsChannelChoose
 * @Author hxy
 * @Date 2020/4/8 19:57
 */
@Configuration
public class TsChannelChoose {

    @Bean
    public SocketHandlerBuilder socketHandlerBuilder(){
        return new TcpsocketHandlerBuilder();
    }

}
