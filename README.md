<p align="center">
</p>
<p align="center">
   <a >
          <img src="https://img.shields.io/badge/Hxy-socket-yeoll.svg?style=flat" />
      </a>
    <a >
        <img src="https://img.shields.io/badge/Build-Java8-red.svg?style=flat" />
    </a>
    <a >
        <img src="https://img.shields.io/badge/Netty-4.1.42.Final-blue.svg" alt="flat">
    </a>
    <a >
        <img src="https://img.shields.io/badge/Licence-GPL3.0-green.svg?style=flat" />
    </a>
</p>

#### 介绍
    基于springboot和netty的socket通信框架,方便扩展springboot各类插件,test中也有丰富的客户端联调demo。
    同时，框架本身
    1.支持websocket,tcp-socket连接
    2.json和protocolbuffer编码协议，以及其他自定义扩展协议
    3.超高性能和超简易的特性
    
#### 快速启动
    @SpringBootApplication
    @EnableWebsocket
    public class RunApp {
    
        public static void main(String[] args) {
            new SpringApplicationBuilder(RunApp.class)
                    .web(WebApplicationType.NONE)
                    .bannerMode(Banner.Mode.OFF)
                    .run(args);
        }
    }
    
    @Socket
    public class SimpleSocketMsgHandler implements SocketMsgHandler<String> {
        @Override
        public void onConnect(ChannelHandlerContext ctx, HttpRequest req) {
            System.out.println(ctx.channel().toString());
        }
    
        @Override
        public void onMessage(ChannelHandlerContext ctx, String msg) {
            System.out.println("收到消息=" + msg);
            ctx.writeAndFlush(msg);
        }
    
        @Override
        public void disConnect(ChannelHandlerContext ctx) {
            System.out.println("断开连接=" + ctx.channel().toString());
        }
    }