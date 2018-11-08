package nafos.dubbo.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import nafos.dubbo.ClassAndMethod;
import nafos.dubbo.ProductService;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/11/8 下午1:19
 * @Description TODO 这里开发测试暂时采用直连的方式
 **/
@Component
public class TestConsumer {
//    @Reference(version = "${demo.service.version}",
//            application = "${dubbo.application.id}",
//            url = "dubbo://localhost:12345")
    //上面方式是测试环境中dubbourl直连方式，生产环境请使用zookeeper，下面方式
    @Reference
    private ProductService productService;

    public void testDubbo(){
        System.out.println(11212);
        System.out.println(productService.getIntParam(new ClassAndMethod(null,null,1),"2121"));
    }
}
