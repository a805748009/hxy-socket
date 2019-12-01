package com

import io.netty.channel.Channel
import nafos.server.annotation.Controller
import nafos.server.annotation.Handle
import nafos.server.relation.Client
import nafos.server.relation.initChannel
import nafos.server.socketServer
import org.springframework.context.annotation.ComponentScan

@ComponentScan(value = ["com", "nafos"])
class SocketRun

fun main() {
    socketServer().start(SocketRun::class.java)
}

@Controller
class TestSocketController {

    @Handle(code = 500)
    fun login(channel: Channel, map: Map<String, String>, clientCode: Int) {
        println(map)
        val token = "唯一字符串"
        initChannel(channel, token, "北京一区", User("小明", "123"))
        channel.writeAndFlush("$clientCode|{}")
    }

    @Handle(code = 501)
    fun hello(client: Client, map: Map<String, String>, clientCode: Int) {
        println(map)
        client.joinRoom("123456")
        client.sendMsg(clientCode, map)
    }
}

