package com.mode.task;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;



/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年4月10日 上午10:19:17 
* 类说明 
*/
@Component
public class StartTask implements ApplicationRunner {



	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("=== hello word ===");
	}





}
