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
    /**
     * server可传SocketConfiguration参数  不传默认端口号9090
     */
    socketServer().start(SocketRun::class.java)
}

/**
 * server包中的socket连接同时支持websocket和tcpsocket的JSON协议
 * 但是不支持CRC32校验和zip压缩信息
 * 也不支持protobuffer消息传递。
 * 如果需要以上两条的支持，请导入 protocol模块，查看对应API
 */
@Controller
class TestSocketController {

    /**
     * @desc 接收路由为500的socket请求，三个参数为毕传，其中channel可在 initchannel() 方法后替换成高级的client
     *       客户端发送的协议格式为  [clientCode]|[serverCode]|[body的Json字符串]
     * @param channel  与客户端保持的channel通道
     * @param map      接收的JSON参数，也可以直接使用bean类接收，会自动转换
     * @param clientCode  客户端带过来的需要回传路由信息，用以给客户端精准反馈
     */
    @Handle(code = 500)
    fun login(channel: Channel, map: Map<String, String>, clientCode: Int) {
        println(map)
        val token = "唯一字符串"
        //将此链接加入到北京一区的区域划分，并绑定token和user，方便后续操作。
        initChannel(channel,  "北京一区", User("小明", "123"))
        //返回给此客户端一个空的JSON到clientCode路由
        channel.writeAndFlush("$clientCode|{}")
    }

    /**
     * @desc 在上面方法initChannel后，第一个参数可用client接收，有更丰富的API
     * @param client  channel的高级封装对象
     * @param map      接收的JSON参数，也可以直接使用bean类接收，会自动转换
     * @param clientCode  客户端带过来的需要回传路由信息，用以给客户端精准反馈
     */
    @Handle(code = 501)
    fun hello(client: Client, map: Map<String, String>, clientCode: Int) {
        println(map)
        // 将此客户端加入到房间号为123456的房间中，如果没有则自动创建
        client.joinRoom("123456")
        // 发送给客户端map的消息体到clientCode路由
        client.sendMsg(clientCode, map)
    }
}

