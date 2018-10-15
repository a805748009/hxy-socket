package nafos.network.bootStrap.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import nafos.core.nafosEnum.ConnectEnum;
import nafos.core.ssl.SslFactory;
import nafos.core.util.ShowLogo;
import nafos.network.bootStrap.netty.handle.PipelineAdd;
import nafos.network.bootStrap.netty.handle.socket.ByteArrayOutboundHandle;
import nafos.network.bootStrap.netty.handle.currency.SocketChooseHandle;
import nafos.network.bootStrap.netty.handle.socket.BytebufToByteHandle;
import nafos.network.bootStrap.netty.handle.socket.ProtocolResolveHandle;
import nafos.network.bootStrap.netty.handle.currency.ChannelActiveHandle;
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
public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Value("${nafos.isOpenSSL:false}")
    private boolean isOpenSSL;
    @Value("${nafos.certFilePath:1}")
    private String certFilePath;
    @Value("${nafos.keyFilePath:1}")
    private String keyFilePath;
    @Value("${nafos.httpServerPort:0}")
    private int httpServerPort;
    @Value("${nafos.socketServerPort:9988}")
    private int socketServerPort;
    @Value("${nafos.readerIdleTime:5}")
    private long readerIdleTime;

    @Autowired
    PipelineAdd pipelineAdd;
    @Autowired
    ProtocolResolveHandle protocolResolveHandle;
    @Autowired
    BytebufToByteHandle bytebufToByteHandle;
    @Autowired
    ByteArrayOutboundHandle byteToByteBufHandle;







    /**
     * 开始启动
     */
    public void serverRun(){
        if(httpServerPort>0){
            startup(httpServerPort, ConnectEnum.HTTP.getType());
        }

        if(socketServerPort>0){
            startup(socketServerPort,ConnectEnum.SOCKET.getType());
        }
    }

    /**
     * 启动对应服务器
     * @param port
     */
    public void startup(int port,String connectType) {
        ShowLogo.consoleout();
        logger.info("================Netty端口启动========"+"port: " + port );

        // Boss线程：由这个线程池提供的线程是boss种类的，用于创建、连接、绑定socket， （有点像门卫）然后把这些socket传给worker线程池。
        // 在服务器端每个监听的socket都有一个boss线程来处理。在客户端，只有一个boss线程来处理所有的socket。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // Worker线程：Worker线程执行所有的异步I/O，即处理操作
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 4096);
            b.group(bossGroup, workGroup)//
                    .channel(NioServerSocketChannel.class)//
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            buildChannelPipeline(ch,connectType);
                        }
                    });
            // 开始真正绑定端口进行监听
            ChannelFuture future = b.bind("0.0.0.0", port).sync();

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
    protected void buildChannelPipeline(SocketChannel ch,String connectType) {
        ChannelPipeline pipeline = ch.pipeline();

        // 1.开启SSL验证
        if(isOpenSSL){
            pipeline.addLast("ssl", SslFactory.createSSLContext(certFilePath,keyFilePath).newHandler(ch.alloc()));
        }

        // 2.判断协议类型
        if(connectType.equals(ConnectEnum.HTTP.getType())){
            pipelineAdd.httpAdd(pipeline);
        }else{
            // 1.socket方式服务
            // 设置N秒没有读到数据，则触发一个READER_IDLE事件。
            pipeline.addLast(new IdleStateHandler(readerIdleTime,0,0, TimeUnit.SECONDS));
            pipeline.addLast("active",new ChannelActiveHandle());

            pipeline.addLast("socketChoose",new SocketChooseHandle());


            //tcpsocket编码解码handle，如果是websocket链接，会将其删除
            pipeline.addLast("lengthEncode",new LengthFieldPrepender(4, false));

            pipeline.addLast("lengthDecoder",new LengthFieldBasedFrameDecoder(2000, 0, 4,0, 4));

            pipeline.addLast(bytebufToByteHandle);



            //因为接收类型的泛型不对，所以在websocket握手的时候不会进入该handle
            //此handle为最后的socket消息分解，web和tcp通用
            pipeline.addLast("byteToBuf",byteToByteBufHandle);
            pipeline.addLast("protocolResolve",protocolResolveHandle);

        }


    }

}
