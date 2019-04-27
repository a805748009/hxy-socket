package com;

import nafos.NafosServer;
import nafos.core.Enums.Protocol;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com","nafos"})
@EnableScheduling
public class Run {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Run.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE)
                .run(args);

       new NafosServer(Run.class)
               .registDefaultProtocol(Protocol.JSON)
               .startupHttp(8050);

//        new NafosServer(Run.class)
//                .registDefaultProtocol(Protocol.JSON)
//                .registRemoveUnSafeSocketChannel(1*60*1000) //1分钟踢掉没有safe的连接
//                .startupSocket(7999);

    }
}
