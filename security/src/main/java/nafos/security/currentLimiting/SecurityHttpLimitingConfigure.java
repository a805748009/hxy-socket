package nafos.security.currentLimiting;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @Author 黄新宇
 * @Date 2018/10/29 下午4:25
 * @Description 如果外部没有限流装置，则用默认的限流
 **/
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE-1)
public class SecurityHttpLimitingConfigure {


    @Bean
    public SecurityHttpLimitingHandle httpLimitingHandle(){
        return new SecurityHttpLimitingHandle();
    }
}
