package com.hxy.nettygo.result.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月9日 上午11:59:34 
* 路由方法体
*/
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE}) 
@Component
public @interface On {
	String value();

	boolean printLog() default false;
}
