package nafos.server.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
* @Author 黄新宇
* @Description(方法拦截处理类)
*/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})
public @interface Interceptor {

    @AliasFor("interceptor")
    Class[] value() default {};

    /**
     * 拦截器数组，根据先后顺序拦截
     */
    @AliasFor("value")
    Class[] interceptor() default {};

    /**
     * 拦截器参数
     */
    String[] interceptorParams() default{};
}
