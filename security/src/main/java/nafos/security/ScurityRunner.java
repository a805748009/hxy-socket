package nafos.security;

import nafos.bootStrap.handle.currency.HttpLimitingHandle;
import nafos.core.helper.SpringApplicationContextHolder;
import nafos.core.mode.runner.NafosRunner;
import nafos.security.cache.FiberDo;
import nafos.security.currentLimiting.SecurityHttpLimitingHandle;
import nafos.security.filter.SessionTimeUpdateFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ScurityRunner implements NafosRunner {
    @Override
    public void run() {
        // 1.注册定时清理cacheMap
        FiberDo.Companion.doClearTimeOutSession(TimeUnit.HOURS.toMillis(2));

        // 2.注册sessionUpdateHandle,在刷新用户session时间时调用
        new SessionTimeUpdateFactory().init();

        // 3.配置限流器
        String limitType = SpringApplicationContextHolder.getSpringBeanForClass(SecurityHttpLimitingHandle.class).limitOnType;
        if ("LOCAL".equals(limitType) || "REDIS".equals(limitType)) {
            registLimiting();
        }

    }


    public void registLimiting() {
        //已经加载过了，就不再重新加载
        if(SpringApplicationContextHolder.getSpringBean("httpLimitingHandle").getClass().equals(SecurityHttpLimitingHandle.class))
            return;
        BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory)SpringApplicationContextHolder.getContext().getAutowireCapableBeanFactory();
        beanDefReg.getBeanDefinition("httpLimitingHandle");
        beanDefReg.removeBeanDefinition("httpLimitingHandle");
        BeanDefinitionBuilder beanDefBuilder = BeanDefinitionBuilder.genericBeanDefinition(SecurityHttpLimitingHandle.class);
        BeanDefinition beanDef = beanDefBuilder.getBeanDefinition();
        if (!beanDefReg.containsBeanDefinition( "httpLimitingHandle")) {
            beanDefReg.registerBeanDefinition( "httpLimitingHandle", beanDef);
        }
        System.out.println(SpringApplicationContextHolder.getSpringBean("httpLimitingHandle"));
    }
}
