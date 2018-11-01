import nafos.network.bootStrap.NafosServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年1月15日 下午8:12:49 类说明
 */

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com","nafos"})
@EnableScheduling
@EnableEurekaClient
public class RunApp {


	public static void main(String[] args) {
		NafosServer.startup(RunApp.class,args);
	}


}
