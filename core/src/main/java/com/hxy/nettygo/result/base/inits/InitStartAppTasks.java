package com.hxy.nettygo.result.base.inits;

import com.hxy.nettygo.result.base.task.StartAppTask;
import com.hxy.nettygo.result.base.tools.SpringApplicationContextHolder;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月26日 下午2:40:37 
* 类说明 
*/
public class InitStartAppTasks {
	
	private boolean started = false;

	/**
	 * 应用启动任务
	 */
	public  void startApp(){
		if(started)
			return;
		
		ApplicationContext context = SpringApplicationContextHolder.getContext();
		//1.获取所有实现StartAppTask.class接口的类
		String[] InterfaceBeanNameArrayForStartAppTask = context.getBeanNamesForType(StartAppTask.class);
		if(InterfaceBeanNameArrayForStartAppTask.length==0)return;
		for(String beanName:InterfaceBeanNameArrayForStartAppTask){
			 try {
				 //2.获取类中的run方法，执行
				Method method  = context.getType(beanName).getMethod("run");
				method.invoke(SpringApplicationContextHolder.getSpringBeanForClass(context.getType(beanName)));
				
			} catch (NoSuchBeanDefinitionException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}			
		}
		started = true;
	}
}
