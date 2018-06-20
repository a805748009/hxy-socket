package com.result.base.tools;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年1月13日 下午6:58:44 
* 类说明 
*/
@Component
public class SpringApplicationContextHolder implements ApplicationContextAware {  
	  
    private static ApplicationContext context;  
  
    @Override  
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringApplicationContextHolder.context = context;
    }  
  
    public static ApplicationContext getContext(){
    	return context;
    }
     
    public static Object getSpringBean(String beanName) {  
        return context==null?null:context.getBean(beanName);  
    }  
    
    public static <T> Object getSpringBeanForClass(Class<T> clazz) {  
        return context==null?null:context.getBean(clazz);  
    }  
  
    public static String[] getBeanDefinitionNames() {  
        return context.getBeanDefinitionNames();  
    }  
}
