package hxy.server.socket.anno;

import hxy.server.socket.engine.SimpleCodeHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SimpleCodeHandler.class})
public @interface EnableCodeHandler {
}
