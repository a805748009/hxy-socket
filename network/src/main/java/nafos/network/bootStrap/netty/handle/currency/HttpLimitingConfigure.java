package nafos.network.bootStrap.netty.handle.currency;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @Author 黄新宇
 * @Date 2018/10/29 下午4:25
 * @Description 如果外部没有限流装置，则用默认的限流
 **/
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class HttpLimitingConfigure {


    @Bean
    @ConditionalOnMissingBean
    public HttpLimitingHandle httpLimitingHandle(){
        return new HttpLimitingHandle();
    }
}
