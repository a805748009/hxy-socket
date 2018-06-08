package com;

import com.business.dao.UserDao;
import com.mode.init.NettyModeInit;
import com.result.base.inits.InitMothods;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@MapperScan("com.business.dao")
public class RunApp {


	public static void main(String[] args) {
		// 启动spring容器
		ApplicationContext ac = SpringApplication.run(RunApp.class, args);
		ac.getBean(NettyModeInit.class).configurtaion();
		new InitMothods().initApplicationContext(ac);
	}


}
