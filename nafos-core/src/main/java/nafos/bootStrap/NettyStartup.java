package nafos.bootStrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import nafos.bootStrap.handle.currency.ChannelActiveHandle;
import nafos.bootStrap.handle.currency.SocketChooseHandle;
import nafos.bootStrap.handle.socket.ByteArrayOutboundHandle;
import nafos.bootStrap.handle.socket.BytebufToByteHandle;
import nafos.bootStrap.handle.socket.ProtocolResolveHandle;
import nafos.core.Enums.Connect;
import nafos.bootStrap.handle.HttpPipelineAdd;
import nafos.bootStrap.handle.ssl.SslFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author 黄新宇
 * @Date 2018/10/8 下午5:04
 * @Description TODO
 **/
@Component
public class NettyStartup {
    private static final Logger logger = LoggerFactory.getLogger(NettyStartup.class);

    @Value("${nafos.isOpenSSL:false}")
    private boolean isOpenSSL;
    @Value("${nafos.certFilePath:}")
    private String certFilePath;
    @Value("${nafos.keyFilePath:}")
    private String keyFilePath;
    @Value("${nafos.readerIdleTime:5}")
    private long readerIdleTime;

    private static long heartTimeout = 0L;

    @Autowired
    HttpPipelineAdd httpAdd;
    @Autowired
    ProtocolResolveHandle protocolResolveHandle;
    @Autowired
    BytebufToByteHandle bytebufToByteHandle;
    @Autowired
    ByteArrayOutboundHandle byteToByteBufHandle;

    public static void setHeartTimeout(long second){
        heartTimeout = second;
    }

    public long getHeartTimeout(){
        if(heartTimeout == 0L){
            heartTimeout = readerIdleTime;
        }
        return heartTimeout;
    }

    /**
     * 启动对应服务器
     *
     * @param port
     */
    public void startup(int port, String connectType) {
        if (port <= 0) {
            logger.error("   {}: {} ,must more than 0, so startup failed", connectType, port);
            return;
        }
        // Boss线程：由这个线程池提供的线程是boss种类的，用于创建、连接、绑定socket， （有点像门卫）然后把这些socket传给worker线程池。
        // 在服务器端每个监听的socket都有一个boss线程来处理。在客户端，只有一个boss线程来处理所有的socket。
        EventLoopGroup bossGroup = new NioEventLoopGroup(2,new DefaultThreadFactory("boss-thread",true));
        // Worker线程：Worker线程执行所有的异步I/O，即处理操作
        EventLoopGroup workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2,new DefaultThreadFactory("worker-thread",true));
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 4096);
            b.group(bossGroup, workGroup)//
                    .channel(NioServerSocketChannel.class)//
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            buildChannelPipeline(ch, connectType);
                        }
                    });
            // 开始真正绑定端口进行监听
            ChannelFuture future = b.bind("0.0.0.0", port).sync();
            showLog();
            logger.info("================Nafos启动成功，通讯方式：{}， 端口号：{}========", connectType, port);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("server exit...");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    /**
     * 构建ChannelPipeline通道.
     *
     * @param ch SocketChannel
     */
    protected void buildChannelPipeline(SocketChannel ch, String connectType) {
        ChannelPipeline pipeline = ch.pipeline();

        // 1.开启SSL验证
        if (isOpenSSL) {
            pipeline.addLast("ssl", SslFactory.createSSLContext(certFilePath, keyFilePath).newHandler(ch.alloc()));
        }

        // 2.判断协议类型
        if (connectType.equals(Connect.HTTP.name())) {
            httpAdd.handAdd(pipeline);
        } else {
            // 1.socket方式服务
            // 设置N秒没有读到数据，则触发一个READER_IDLE事件。
            pipeline.addLast(new IdleStateHandler(getHeartTimeout(), 0, 0, TimeUnit.SECONDS));
            pipeline.addLast("active", new ChannelActiveHandle());

            pipeline.addLast("socketChoose", new SocketChooseHandle());


            //tcpsocket编码解码handle，如果是websocket链接，会将其删除
            pipeline.addLast("lengthEncode", new LengthFieldPrepender(4, false));

            pipeline.addLast("lengthDecoder", new LengthFieldBasedFrameDecoder(2000, 0, 4, 0, 4));

            pipeline.addLast(bytebufToByteHandle);


            //因为接收类型的泛型不对，所以在websocket握手的时候不会进入该handle
            //此handle为最后的socket消息分解，web和tcp通用
            pipeline.addLast("out-byteToBuf", byteToByteBufHandle);
            pipeline.addLast("protocolResolve", protocolResolveHandle);

        }

    }

    private static volatile boolean isShowLog = false;


    /**
     * 打印log
     */
    private void showLog() {
        if (isShowLog) return;
        System.out.println("  .      _   _              ______    ____     _____\n" +
                " /\\\\    | \\ | |     /\\     |  ____|  / __ \\   / ____|\n" +
                "( ( )   |  \\| |    /  \\    | |__    | |  | | | (___\n" +
                "( ( )   | . ` |   / /\\ \\   |  __|   | |  | |  \\___ \\\n" +
                " \\\\/    | |\\  |  / ____ \\  | |      | |__| |  ____) |\n" +
                "  '     |_| \\_| /_/    \\_\\ |_|       \\____/  |_____/   version:2.0.0    \n" +
                " =====================================================================");
        isShowLog = true;
    }

}
