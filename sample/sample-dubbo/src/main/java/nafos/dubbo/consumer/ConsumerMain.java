package nafos.dubbo.consumer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @Author 黄新宇
 * @Date 2018/11/8 上午11:36
 * @Description API :https://github.com/apache/incubator-dubbo-spring-boot-project/blob/master/README_CN.md
 **/

@SpringBootApplication
public class ConsumerMain {



    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ap= SpringApplication.run(ConsumerMain.class, args);
        while (true){
            ap.getBean(TestConsumer.class).testDubbo();
            Thread.sleep(1000);
        }

    }


}
