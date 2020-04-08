package hxy.server.socket.engine;

import hxy.server.socket.configuration.SocketConfiguration;
import hxy.server.socket.entity.SslInfo;
import hxy.server.socket.util.OSInfo;
import hxy.server.socket.util.SpringApplicationContextHolder;
import hxy.server.socket.util.SslFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName EngineStarter
 * @Description TODO
 * @Author hxy
 * @Date 2020/4/8 18:32
 */
public class EngineStarter {

    public EngineStarter(ApplicationContext ac) {
        SpringApplicationContextHolder.setAc(ac);
    }

    private Logger logger = LoggerFactory.getLogger(WebsocketInitHandler.class);

    private static SocketConfiguration config = SpringApplicationContextHolder.getBean(SocketConfiguration.class);

    EventLoopGroup bossGroup = null;
    EventLoopGroup workGroup = null;

    public void run() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        if (OSInfo.isLinux()) {
            bossGroup = new EpollEventLoopGroup(config.getBossThreadCount(), new DefaultThreadFactory("boss-thread", true));
            workGroup = new EpollEventLoopGroup(config.getWorkThreadCount(), new DefaultThreadFactory("worker-thread", true));
            bootstrap.channel(EpollServerSocketChannel.class)
                    .group(bossGroup, workGroup)
                    .option(EpollChannelOption.TCP_CORK, true);
        } else {
            bossGroup = new NioEventLoopGroup(config.getBossThreadCount(), new DefaultThreadFactory("boss-thread", true));
            workGroup = new NioEventLoopGroup(config.getWorkThreadCount(), new DefaultThreadFactory("worker-thread", true));
            bootstrap.channel(NioServerSocketChannel.class)
                    .group(bossGroup, workGroup);
        }
        bootstrap.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 1024);
        logger.info("os:{},Bootstrap configuration: {}", OSInfo.getOSname(), bootstrap.toString());

        try {
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    SslInfo sslInfo = config.getSslInfo();
                    // 开启SSL验证
                    if (sslInfo.isOpen()) {
                        pipeline.addLast("ssl", SslFactory.createSslContext(sslInfo.getCertFilePath(), sslInfo.getKeyFilePath()).newHandler(socketChannel.alloc()));
                    }

                    // 设置N秒没有读到数据，则触发一个READER_IDLE事件。
                    pipeline.addLast(new IdleStateHandler(config.getHeartTimeout(), 0, 0, TimeUnit.SECONDS));
                    pipeline.addLast(new HeartBeatServerHandler());

                    //选择服务启动
                    SocketInitHandler sc = SpringApplicationContextHolder.getBean(SocketInitHandler.class);
                    sc.buildChannelPipeline(pipeline);
                }
            });
            // 开始真正绑定端口进行监听
            ChannelFuture future = bootstrap.bind("0.0.0.0", config.getPort()).sync();
            showLog();
            logger.info("================Hxy-socket启动成功， 端口号：{}========",  config.getPort());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            logger.info("server exit...");
            shutdown();
        }
    }

    public void shutdown(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }


    private void showLog() {
        System.out.println("  .      _   _              ______    ____     _____\n" +
                " /\\\\    | \\ | |     /\\     |  ____|  / __ \\   / ____|\n" +
                "( ( )   |  \\| |    /  \\    | |__    | |  | | | (___\n" +
                "( ( )   | . ` |   / /\\ \\   |  __|   | |  | |  \\___ \\\n" +
                " \\\\/    | |\\  |  / ____ \\  | |      | |__| |  ____) |\n" +
                "  '     |_| \\_| /_/    \\_\\ |_|       \\____/  |_____/   version:1.3.0-release    \n" +
                " =============================================================================================");
    }
}
