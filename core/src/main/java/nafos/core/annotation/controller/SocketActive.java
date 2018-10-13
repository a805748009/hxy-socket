package nafos.core.annotation.controller;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
* @Author 黄新宇
* @Description(controller处理类)
*/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})
@Component
public @interface SocketActive {
}
