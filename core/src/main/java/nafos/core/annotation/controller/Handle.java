package nafos.core.annotation.controller;

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
    String method() default "POST";



    /**
     * 编码方式 PROTO 或者 JSON
     */
    String type() default "PROTO";



    /**
     * 是否打印运行时间
     */
    boolean printLog() default false;



    /**
     * 是否直接用work线程运行
     */
    boolean runOnWorkGroup() default false;
}
