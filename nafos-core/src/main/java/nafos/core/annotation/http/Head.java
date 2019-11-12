package nafos.core.annotation.http;

import nafos.core.Enums.Protocol;
import nafos.core.Enums.RequestMethod;
import nafos.core.annotation.Handle;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author 黄新宇
 * @Description(HEAD方式请求处理handle)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Component
@Handle(method = RequestMethod.HEAD)
public @interface Head {


    @AliasFor(
            annotation = Handle.class,
            attribute = "uri"
    )
    String uri() default "";

    @AliasFor(
            annotation = Handle.class,
            attribute = "uri"
    )
    String value() default "";


    @AliasFor(
            annotation = Handle.class,
            attribute = "type"
    )
    Protocol type() default Protocol.DEFAULT;


    @AliasFor(
            annotation = Handle.class,
            attribute = "printLog"
    )
    boolean printLog() default false;


    @AliasFor(
            annotation = Handle.class,
            attribute = "runOnWorkGroup"
    )
    boolean runOnWorkGroup() default false;
}
