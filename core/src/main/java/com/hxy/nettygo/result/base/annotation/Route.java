package com.hxy.nettygo.result.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月4日 下午2:31:06 
* netty路由注解类
* 第一次模拟requestmaping写路由，感觉这是一条不归路。emmmmmmmm
*/


@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE}) 
@Component
public @interface Route {

}
