package nafos.network.factory;

import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;

/**
 * @Author 黄新宇
 * @Date 2018/10/23 上午10:34
 * @Description start空方法，让boot启动的时候不去启动tomcat等容器，我们自己使用netty
 **/
public class EmbeddedNullContainer implements EmbeddedServletContainer {



    @Override
    public void start() throws EmbeddedServletContainerException {
    }

    @Override
    public void stop() throws EmbeddedServletContainerException {

    }

    @Override
    public int getPort() {
        return 0;
    }
}
