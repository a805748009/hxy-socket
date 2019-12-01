package com

import io.netty.channel.Channel
import nafos.protocol.protocolSocketServer
import nafos.protocol.send
import nafos.server.annotation.Controller
import nafos.server.annotation.Handle
import org.springframework.context.annotation.ComponentScan

@ComponentScan(value = ["com", "nafos"])
class SocketRun

fun main() {
    protocolSocketServer().start(SocketRun::class.java)
}

@Controller
class TestSocketController {

    @Handle(code = 500)
    fun login(channel: Channel, user: User,clientCode:ByteArray){
        println(user)
        channel.send(clientCode,user)
    }
}

