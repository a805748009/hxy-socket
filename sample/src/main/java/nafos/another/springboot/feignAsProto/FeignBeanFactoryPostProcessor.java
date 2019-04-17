//package nafos.another.springboot.feignAsProto;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
///**
// * @Author 黄新宇
// * @Date 2018/10/23 上午11:34
// * @Description 解决关机eurake报错的问题
// **/
//@Component
//public class FeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        if (containsBeanDefinition(beanFactory, "feignContext", "eurekaAutoServiceRegistration")) {
//            BeanDefinition bd = beanFactory.getBeanDefinition("feignContext");
//            bd.setDependsOn("eurekaAutoServiceRegistration");
//        }
//    }
//
//    private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String... beans) {
//        return Arrays.stream(beans).allMatch(b -> beanFactory.containsBeanDefinition(b));
//    }
//}
