<p align="center">  
   <h3 align="center">NAFOS</h3>
   <p align="center">
     never ask for our savior，he's busier than you.
     <br>
     <a href="https://gitee.com/huangxinyu/nafos" >
                 <img  height="50" width="50"  src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555864133489&di=1d10a230c0925ce98139316d1ba992c7&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160824%2F53427121c2f64eb492430b2849e9c0c4.jpg" >
             </a>
</p>
 <br>
     


## 简介
nafos是一个轻量的网络通讯协程框架。它能同时支持HTTP,WEBSOCKET,TCP通信，并以非常友好的方式连接到大家的业务代码。

哦~对了，他还是协程处理大家写的业务代码。让在IO密集处理的业务代码中，省下更多的上下文切换消耗和系统内存，在高并发冲击下变的更加可靠和快速。

而且，它很轻哦，启动十分快速的它，却能无缝兼容springboot，以及嵌入到其他任何系统中。

Nafos is a lightweight network communication protocol framework. It can support HTTP, websocket and TCP communication at the same time, and connect to your business code in a very friendly way.

oh ~ by the way, he ucoupled the code written by you with Coroutine . in the io intensive business code, it saves more context switching consumption and system memory, and becomes more reliable and fast under the impact of high concurrency.

Moreover, it's very light, very fast to start, but it can be seamlessly compatible with spring boot, as well as embedded in any other system.



## 文档
- 正在赶来的路上
On the way





## 特点
- 1、简单易用：无需繁杂的学习流程，仅仅需要简单的几步配置即可正确使用；
- 2、强兼容: 可单机同时支持HTTP,TCP,websocket等服务，小规模应用下不用多开占用资源；
- 3、高性能：经的起压测的超高并发实现；
- 4、易扩展：无缝添加springboot，可完美支持spring大家族系列；
- 5、可限流：自带单机和分布式限流器，多策略轻松抗压防崩溃；
- 6、房间策略：封装常见游戏的房间策略，开房，比赛，聊天可直接使用；
- 7、双编码：同时支持json和protobuffer格式编码，手游和应用一块搞定；
- 8、协程：采用协程模式取代传统的线程模式，在高IO的业务代码下更加可靠和高效；
- 9、自单点：一键配置即可实现SSO单点登录，多机器共享登陆状态无需额外代码；
- 10、安全节流：简单开启数据压缩和加密，节约带宽又安全；

## 交流

- 邮箱：805748009@qq.com
- 作者QQ：805748009

## 快速入门
```java
@ComponentScan(value = ["com", "nafos"])
class HttpRun

fun main() {
    httpServer().start(HttpRun::class.java)
	socketServer().start(SocketRun::class.java)
}

/**
 * HTTP模式的controller
 */
@Controller("/test")
class TestController {

    @Get("/hello")
    fun hello(map: Map<String, String>, nsRequest: NsRequest, nsRespone: NsRespone): Any {
        println(map)
        println(nsRequest.uri)
        println(nsRespone)
        return map
    }
}
/**
 * TCP或WEBSOCKET模式的controller
 */
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

```


## 参与
Contributions are welcome! Open a pull request to fix a bug, or open an [Issue](https://gitee.com/huangxinyu/nafos/issues) to discuss a new feature or change.

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://gitee.com/huangxinyu/nafos/issues) 讨论新特性或者变更。


## Copyright and License
This product is open source and free, and will continue to provide free community technical support. Individual or enterprise users are free to access and use.

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

## Who is using
 <img  height="80" width="250"  src="http://www.rinzz.com/wp-content/uploads/2017/01/logo.png" >
 <img  height="90" width="120"  src="https://h5-1254229806.cos.ap-guangzhou.myqcloud.com/yuyue.png" >
