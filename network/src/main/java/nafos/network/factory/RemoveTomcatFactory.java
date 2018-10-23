package nafos.network.factory;

import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/10/23 上午11:10
 * @Description 这里是boot启动的时候查找selvet容器的类，我们重写EmbeddedNullContainer，使得boot调用空start。
 **/
@Component
public class RemoveTomcatFactory extends AbstractEmbeddedServletContainerFactory implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) {
        return new EmbeddedNullContainer();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
