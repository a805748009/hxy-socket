package com.controller;

import nafos.NafosServer;
import nafos.core.Enums.Protocol;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"com","nafos"})
@EnableScheduling
public class Run {

    public static void main(String[] args) {

       new NafosServer(Run.class)
               .registRunWatch(1000)
               .registSystemMonitor(500000)
               .registDefaultProtocol(Protocol.JSON)
               .startupHttp(8050);

    }
}
