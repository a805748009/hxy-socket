package nafos.core.annotation.rpc;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月4日 下午2:31:06 
* netty路由注解类
*
*/

@Component
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})  
public @interface RemoteCall {

}
