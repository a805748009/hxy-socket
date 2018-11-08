package nafos.dubbo.product;

import com.alibaba.dubbo.config.annotation.Service;
import nafos.dubbo.ClassAndMethod;
import nafos.dubbo.ProductService;

/**
 * @Author 黄新宇
 * @Date 2018/11/8 上午11:47
 * @Description TODO
 **/
//@Service(
//        version = "${demo.service.version}",
//        application = "${dubbo.application.id}",
//        protocol = "${dubbo.protocol.id}",
//        registry = "${dubbo.registry.id}"
//)
//上面方式是测试环境中dubbourl直连方式，生产环境请使用zookeeper，下面方式
@Service
public class ProductServiceImpl implements ProductService{
    public int getIntParam(ClassAndMethod classAndMethod, String oi) {
        return 2;
    }
}
