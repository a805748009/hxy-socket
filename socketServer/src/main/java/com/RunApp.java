package com;

import com.mode.init.NettyModeInit;
import com.result.base.inits.InitMothods;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月15日 下午8:12:49 类说明
 */

@SpringBootApplication
public class RunApp {


	public static void main(String[] args) {
		// 启动spring容器
		ApplicationContext ac = SpringApplication.run(RunApp.class, args);
		ac.getBean(NettyModeInit.class).configurtaion();
		new InitMothods().initApplicationContext(ac);
	}


}
