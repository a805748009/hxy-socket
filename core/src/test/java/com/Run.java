package com;

import nafos.NafosServer;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"com","nafos"})
@EnableScheduling
public class Run {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Run.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE)
                .run(args);


        new NafosServer(Run.class).startupHttp(5050);

//       new NafosServer(Run.class)
////               .registRunWatch(5000)
////               .registSystemMonitor(7000)
//               .registDefaultProtocol(Protocol.JSON)
//               .startupHttp(8050);

    }
}
