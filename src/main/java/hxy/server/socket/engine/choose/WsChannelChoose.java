package hxy.server.socket.engine.choose;

import hxy.server.socket.engine.factory.SocketHandlerBuilder;
import hxy.server.socket.engine.factory.WebsocketHandlerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName WsChannelChoose
 * @Description 选择websocket服务启动
 * @Author hxy
 * @Date 2020/4/8 19:57
 */
@Configuration
public class WsChannelChoose {

    @Bean
    public SocketHandlerBuilder socketHandlerBuilder() {
        return new WebsocketHandlerBuilder();
    }


}
