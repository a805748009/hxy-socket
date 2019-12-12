package com

import nafos.security.Security
import nafos.security.SecurityUtil
import nafos.security.config.SecurityConfig
import nafos.security.filter.currentNafosCookie
import nafos.security.filter.setNafosCookieId
import nafos.server.ThreadLocalHelper
import nafos.server.annotation.Controller
import nafos.server.annotation.http.Get
import nafos.server.handle.http.OK
import nafos.server.httpServer
import org.springframework.context.annotation.ComponentScan

@ComponentScan(value = ["com", "nafos"])
class HttpRun

fun main() {
    /**
     * 标记开启redis单点登录，超时时间86400
     */
    SecurityConfig.init(false,86400)

    /**
     * 开启redisson配置
     */
//    val config = Config()
//    val singleConfig = config.useSingleServer()
//    singleConfig.apply {
//        address = "127.0.0.1"
//        database = 0
//        password = "password"
//        connectionPoolSize = 20
//    }
//    config.codec = RedissonJacksonCodec()
//    RedissonManager.init(config)

    /**
     * 启动服务
     */
    httpServer().start(HttpRun::class.java)
}

@Controller("/test")
class TestController {

    /***
     *@Description Security标记，必须要登录才能访问，否则返回400code
     *@Author      xinyu.huang
     *@Time        2019/12/1 18:22
     */
    @Security
    @Get("/hello")
    fun hello(map: Map<String, String>): Any {
        val nafosCookieId = currentNafosCookie()
        val user = SecurityUtil.getLoginUser<User>(nafosCookieId)
        println(user)
        return user
    }

    /***
     *@Description 不需要登录即可访问。可以进行登录炒作
     *@Author      xinyu.huang
     *@Time        2019/12/1 18:22
     */
    @Get("/login")
    fun login(): Any {
        val sessionId = "唯一ID"
        val user = User("小明","密码")
        SecurityUtil.setLoginUser(sessionId,user)
        ThreadLocalHelper.getRespone().setNafosCookieId(sessionId)
        return OK()
    }
}