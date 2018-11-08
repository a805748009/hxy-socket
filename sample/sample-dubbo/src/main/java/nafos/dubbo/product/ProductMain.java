package nafos.dubbo.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author 黄新宇
 * @Date 2018/11/8 上午 11:36
 * @Description TODO API :https://github.com/apache/incubator-dubbo-spring-boot-project/blob/master/README_CN.md
 **/

@SpringBootApplication
public class ProductMain {



    public static void main(String[] args) throws InterruptedException {
        SpringApplication app = new SpringApplication(ProductMain.class);
        app.setAdditionalProfiles("product");
        app.run(args);



        Thread.sleep(100000000);
    }
}
