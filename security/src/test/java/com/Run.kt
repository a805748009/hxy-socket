package com

import nafos.NafosServer
import nafos.core.Enums.Protocol
import nafos.security.currentLimiting.LimitEnum
import nafos.security.registLimiting
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@ComponentScan("com", "nafos")
@EnableScheduling
class Run {


}

fun main(args: Array<String>) {
    NafosServer(Run::class.java)
            .registLimiting(LimitEnum.LOCALAll, 1, 1)
            .registDefaultProtocol(Protocol.JSON)
            .startupHttp(8050)

}
