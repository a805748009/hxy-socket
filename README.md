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
nafos是一个基于netty和spring的轻量级高性能服务端应用框架，能同时支持http，tcp，websocket通信，Json和Protobuffer编码协议以及压缩和加密。

最重要的是，它使用非常简单，且非常轻，启动速度快，是微服务的不二选择。



## 文档
- [文档-请点击此处](https://gitee.com/huangxinyu/nafos/wikis)





## 特点
- 1、简单易用：无需繁杂的学习流程，仅仅需要简单的几步配置即可正确使用；
- 2、强兼容: 可单机同时支持HTTP,TCP,websocket等服务，小规模应用下不用多开占用资源；
- 3、高性能：经的起压测的超高并发实现；
- 4、易扩展：无缝添加springboot，可完美支持spring大家族系列；
- 5、可限流：自带单机和分布式限流器，多策略轻松抗压防崩溃；
- 6、房间策略：封装常见游戏的房间策略，开房，比赛，聊天可直接使用；
- 7、双编码：同时支持json和protobuffer格式编码，手游和应用一块搞定；
- 8、饿处理：设置了二级线程池处理请求，在阻塞下最大限度的接受请求防止丢失；
- 9、自单点：一键配置即可实现SSO单点登录，多机器共享登陆状态无需额外代码；
- 10、安全节流：简单开启数据压缩和加密，节约带宽又安全；

## 交流

- 邮箱：805748009@qq.com
- 交流群：54202911

## 快速入门
```java
package com;

import nafos.NafosServer;
import nafos.core.Enums.Protocol;
import nafos.core.annotation.Controller;
import nafos.core.annotation.http.Get;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;

@ComponentScan({"com","nafos"})
@Controller("/")
public class RunApp {
    public static void main(String[] args) {
        new NafosServer(RunApp.class)
                // 注册snowflakeId 非必要
                .registSnowFlake(12, 0)
                // 选定协议传输格式 不注册默认JSON
                .registDefaultProtocol(Protocol.JSON)
                .registShutDown(() -> {
                    // TODO kill -15 关机前做的事情
                })
                // 启动端口号
                .startupHttp(8050);
    }

    @Get//请求地址 127.0.0.1:8050/
    public Object httpGet(Map map){
        //打印get参数
        System.out.println(map);
        //返回参数，返回任意Object，自动转json
        return map;
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
