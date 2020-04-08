package hxy.server.socket.anno;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})
@Component
@Inherited
public @interface Handle {

    int code() default 0;

}
