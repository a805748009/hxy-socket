package nafos.core.annotation.socket;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
* @Author 黄新宇
* @Description(socket断开连接处理方法标记)
*/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})
@Component
public @interface DisConnect {
}
