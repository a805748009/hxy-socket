package com;

import nafos.network.bootStrap.NafosServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@MapperScan("com.business.dao")
@ComponentScan(basePackages = {"com","nafos"})
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@EnableEurekaClient
public class RunApp {


	public static void main(String[] args) {
		NafosServer.startup(RunApp.class,args);
	}

}