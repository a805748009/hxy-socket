package com

import nafos.server.HttpConfiguration
import nafos.server.annotation.Controller
import nafos.server.annotation.http.Get
import nafos.server.annotation.http.Post
import nafos.server.handle.http.NsRequest
import nafos.server.handle.http.NsRespone
import nafos.server.handle.http.OK
import nafos.server.httpServer
import org.springframework.context.annotation.ComponentScan

@ComponentScan(value = ["com", "nafos"])
class HttpRun

fun main() {
    httpServer(HttpConfiguration(port = 8665)).start(HttpRun::class.java)
}

@Controller("/test")
class TestController {

    /**
     * 用get方式请求  ip:port/test/hello   即可进入一下方法。
     * @param: map 接收get参数的map类型 可不接受此参数，如果没有get参数
     * @param: nsRequest request，可不接受此参数，如果不需要处理文件，IP等信息
     * @param: nsRespone respone，可不接受此参数，此方式一般用来设置头部信息或者返回参数，如果是普通参数返回，直接return即可
     */
    @Get("/hello")
    fun hello(map: Map<String, String>, nsRequest: NsRequest, nsRespone: NsRespone): Any {
        println(map)
        println(nsRequest.uri)
        nsRespone.setCookie("jessionId","d1654aw6d489w74d")
        return map
    }

    /**
     * 用post方式请求  ip:port/test/hello   即可进入一下方法。
     * OK() 返回的是一个空JSON
     */
    @Post("/hello")
    fun helloPost(): Any {
        return OK()
    }


}

