package nafos.security;

import nafos.core.annotation.Controller;
import nafos.security.filter.GlobalInterceptorFilter;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Component
@Controller
public @interface SecurityController {

    @AliasFor(
            annotation = Controller.class,
            attribute = "uri"
    )
    String uri() default "";

    @AliasFor(
            annotation = Controller.class,
            attribute = "value"
    )
    String value() default "";

    @AliasFor(
            annotation = Controller.class,
            attribute = "interceptor"
    )
    Class[] interceptor() default {GlobalInterceptorFilter.class}; //拦截器数组，根据先后顺序拦截
}
