package nafos.server.annotation;

import org.springframework.core.annotation.AliasFor;
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
public @interface Controller {

    @AliasFor("value")
    String uri() default "";

    @AliasFor("uri")
    String value() default "";

    /**
     * 拦截器数组，根据先后顺序拦截
     */
    Class[] interceptor() default {};

    String[] interceptorParams() default{};
}
