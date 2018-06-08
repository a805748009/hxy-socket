package com.result.base.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月9日 上午11:59:34 
* 路由方法体
*/
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE}) 
@Component
public @interface QueueListener {
	String value();
}
