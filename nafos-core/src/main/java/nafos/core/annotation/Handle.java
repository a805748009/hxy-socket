package nafos.core.annotation;

import nafos.core.Enums.Protocol;
import nafos.core.Enums.RequestMethod;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author 黄新宇
 * @Description(controller处理类中的具体处理方法)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})
@Component
@Inherited
public @interface Handle {


    /**
     *http模式下的uri
     */
    String uri() default "";


    /**
     *socket模式下的状态码
     */
    int code() default 0;

    /**
     *http模式下，POST 还是 GET
     */
    RequestMethod method() default RequestMethod.GET;



    /**
     * 编码方式 PROTO 或者 JSON
     */
    Protocol type() default Protocol.DEFAULT;



    /**
     * 是否打印运行时间
     */
    boolean printLog() default false;



    /**
     * 是否直接用work线程运行
     */
    boolean runOnWorkGroup() default false;
}
