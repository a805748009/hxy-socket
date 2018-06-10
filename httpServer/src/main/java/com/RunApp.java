package com;

import com.business.service.test;
import com.mode.init.NettyModeInit;
import com.result.base.inits.InitMothods;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
@SpringBootApplication
@MapperScan("com.business.dao")
@EnableFeignClients
@EnableDiscoveryClient
public class RunApp {


	public static void main(String[] args) {
		// 启动spring容器
		ApplicationContext ac = SpringApplication.run(RunApp.class, args);
		System.out.println(ac.getBean(test.class).sayHiFromClientOne("dsdad"));
		new InitMothods().initApplicationContext(ac);
		ac.getBean(NettyModeInit.class).configurtaion();
	}


}
