package hxy.server.socket.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description 全局异常标记
 * @Author xinyu.huang
 * @Time 2020/4/11 20:59
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Component
@Inherited
public @interface ExceptionHandler {
    /**
     * Please do not modify
     * This is used to mark exception handling
     */
    String value() default  "ExceptionHandler";
}
