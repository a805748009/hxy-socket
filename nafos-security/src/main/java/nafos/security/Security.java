package nafos.security;

import nafos.core.annotation.Interceptor;
import nafos.security.filter.GlobalInterceptorFilter;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Interceptor
public @interface Security {


    @AliasFor(
            annotation = Interceptor.class,
            attribute = "value"
    )
    Class[] value() default {GlobalInterceptorFilter.class};

    @AliasFor(
            annotation = Interceptor.class,
            attribute = "interceptor"
    )
    Class[] interceptor() default {GlobalInterceptorFilter.class}; //拦截器数组，根据先后顺序拦截
}
