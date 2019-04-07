//package com;
//
//import nafos.NafosServer;
//import nafos.core.Enums.Protocol;
//import nafos.security.currentLimiting.LimitEnum;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//@ComponentScan({"com","nafos"})
//@EnableScheduling
//class Run {
//
//    public static void main(String[] args) {
//
//       new NafosServer(Run.class)
//               .registLimit(LimitEnum.LOCALAll,1,2)
//               .registDefaultProtocol(Protocol.JSON)
//               .startupHttp(8050);
//
//    }
//}
