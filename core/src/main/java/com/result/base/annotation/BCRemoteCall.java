package com.result.base.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月4日 下午2:31:06 
* netty路由注解类
* 第一次模拟requestmaping写路由，感觉这是一条不归路。emmmmmmmm
*/

@Component
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})  
public @interface BCRemoteCall {

}
