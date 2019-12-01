package com

import nafos.server.handle.http.NsRequest
import nafos.server.handle.http.NsRespone
import nafos.server.httpServer
import org.springframework.context.annotation.ComponentScan

@ComponentScan(value = ["com", "nafos"])
class HttpRun

fun main() {
    httpServer().start(HttpRun::class.java)
}

@nafos.server.annotation.Controller("/test")
class TestController {

    @nafos.server.annotation.http.Get("/hello")
    fun hello(map: Map<String, String>, nsRequest: NsRequest, nsRespone: NsRespone): Any {
        println(map)
        println(nsRequest.uri)
        println(nsRespone)
        return map
    }
}

