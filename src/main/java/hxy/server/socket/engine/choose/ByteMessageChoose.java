package hxy.server.socket.engine.choose;


import hxy.server.socket.engine.AbstractSocketServerHandler;
import hxy.server.socket.engine.ProtocolWebSocketServerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class ByteMessageChoose {

    @Bean
    @Scope("prototype")
    @Primary
    public AbstractSocketServerHandler socketServerHandler(){
        return new ProtocolWebSocketServerHandler();
    }
}
