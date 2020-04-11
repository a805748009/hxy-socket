package hxy.server.socket.anno;

import hxy.server.socket.configuration.SocketConfiguration;
import hxy.server.socket.engine.choose.WsChannelChoose;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({WsChannelChoose.class,SocketConfiguration.class})
public @interface EnableProtocolBuffer {
}
