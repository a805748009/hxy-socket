package hxy.server.socket.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;

/**
 * @ClassName SpringApplicationContextHolder
 * @Description springboot 附加容器
 * @Author hxy
 * @Date 2020/4/8 17:30
 */
public class SpringApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public static void setAc(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return (T) getApplicationContext().getBean(requiredType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) getApplicationContext().getBean(name);
    }

    public static <T> T getBeanOrNull(String name) {
        assertContextInjected();
        return Optional.ofNullable(getApplicationContext().getBean(name)).map(o -> (T) o).orElse(null);
    }

    public static void assertContextInjected() {
        if (context == null) {
            throw new NullPointerException("application未注入 ，请在springContext.xml中注入SpringHolder!");
        }
    }
}
