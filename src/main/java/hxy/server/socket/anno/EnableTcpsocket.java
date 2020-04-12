package hxy.server.socket.anno;

import hxy.server.socket.configuration.SocketConfiguration;
import hxy.server.socket.engine.EngineConfiguration;
import hxy.server.socket.engine.choose.TsChannelChoose;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TsChannelChoose.class, SocketConfiguration.class, EngineConfiguration.class})
public @interface EnableTcpsocket {
}
