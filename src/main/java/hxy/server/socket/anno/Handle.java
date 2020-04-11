package hxy.server.socket.anno;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Component
@Inherited
public @interface Handle {


    @AliasFor(
            annotation = Handle.class,
            attribute = "value"
    )
    int code() default Integer.MIN_VALUE;

    @AliasFor(
            annotation = Handle.class,
            attribute = "code"
    )
    int value() default Integer.MIN_VALUE;

}
