package hxy.server.socket.engine.choose;

import hxy.server.socket.engine.EngineStarter;
import hxy.server.socket.engine.SocketInitHandler;
import hxy.server.socket.engine.TcpsocketInitHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName TsChannelChoose
 * @Author hxy
 * @Date 2020/4/8 19:57
 */
@Configuration
public class TsChannelChoose {

    @Bean
    public SocketInitHandler socketInitHandler(){
        return new TcpsocketInitHandler();
    }

    @Bean(initMethod = "run",destroyMethod = "shutdown")
    public EngineStarter engineStarter(ApplicationContext applicationContext){
        return new EngineStarter(applicationContext);
    }
}
