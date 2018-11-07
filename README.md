<p align="center">
    <img src="http://thyrsi.com/t6/394/1540300684x-1404795810.png" width="150">
    <h3 align="center">NAFOS</h3>
    <p align="center">
        一个基于netty的轻量级高性能服务端框架。
        <br>
</p>


## 简介
nafos是一个基于netty的高性能服务器框架，其目的在于易上手，易扩展，让开发人员更致力于业务开发。
在前后端分离的web架构上，或者APP,手游，nafos都是一个很不错的选择。

除此之外，sample中也给出了超简单的扩展方案，使得nafos在分布式扩展上能更胜一筹。


## 文档
- [文档](https://www.showdoc.cc/nafos?page_id=1033780133131417)



## 特点
- 1、简单易用：通过简单的配置文件即可建立完善的启动方案，然后就可以开心的关注业务代码了；
- 2、串行设计 ：单用户的所有请求都是串行进行，完美解决单用户并发问题，减少锁的使用；
- 3、高性能：网络层采用netty作为中间件，同等配置及优化条件下，相比tomcat性能可提升一倍；
- 4、易扩展：整合了springBoot，可完美支持spring大家族系列；
- 5、强兼容: 可单机同时支持HTTP,TCP,websocket等服务，小规模应用下不用多开占用资源；
- 6、工具类丰富：封装所有开发中常见工具类可直接调用；
- 7、房间策略：封装常见游戏的房间策略，开房，比赛，聊天可直接调用，无需多写；
- 8、模块化：多个模块之间相互解耦，喜欢哪个用哪个，不喜欢直接丢弃。
- 9、脚本支持：内有现成的shell脚本可以直接使用，开关机，数据库备份等；
- 10、自带分布式限流器，有IP策略和总流量策略等漏桶限流，抵御攻击。
- 11、自带protostuff的feign编解码器，操作简单的同时可以极大程度优化feign端对端的通信问题。
- 12、封装了kafa和rabbitMQ,工具类一般超简单使用，无需关注内部业务；
- 13、丰富教程：除了详细文档外，在sample模块中还有多模块使用案例，开发无忧~


## 交流

- 邮箱：805748009@qq.com


## 参与
Contributions are welcome! Open a pull request to fix a bug, or open an [Issue](https://gitee.com/huangxinyu/BC-NETTYGO/issues) to discuss a new feature or change.

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://gitee.com/huangxinyu/BC-NETTYGO/issues) 讨论新特性或者变更。


## Copyright and License
This product is open source and free, and will continue to provide free community technical support. Individual or enterprise users are free to access and use.

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。


