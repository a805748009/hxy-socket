package nafos.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.stream.ChunkedWriteHandler
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.concurrent.DefaultThreadFactory
import nafos.server.enums.Connect
import nafos.server.handle.SocketPieplineDynamicHandle
import nafos.server.handle.http.BuildHttpObjectAggregator
import nafos.server.handle.SslFactory
import nafos.server.handle.http.HttpServerHandle
import nafos.server.handle.socket.ByteArrayOutboundHandle
import nafos.server.handle.socket.ChannelActiveHandle
import nafos.server.handle.socket.ProtocolResolveHandle
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * @Description netty启动装置
 * @Author      xinyu.huang
 * @Time        2019/11/24 1:08
 */
class NettyEngine {
    private val logger = LoggerFactory.getLogger(NettyEngine::class.java)

    /**
     * 连接类型
     */
    private lateinit var connectType: String

    /**
     * 配置
     */
    private var configuration: Configuration

    companion object {
        val nettyInputThreadGroup: MutableList<NioEventLoopGroup> = mutableListOf()

        /***
         *@Description 关闭消息入口
         *@Author      xinyu.huang
         *@Time        2019/11/30 21:31
         */
        fun closeInputThreadGroup() = run { nettyInputThreadGroup.forEach { it.shutdownGracefully() } }
    }

    constructor(configuration: Configuration) {
        this.configuration = configuration
    }


    /***
     *@Description 启动对应服务器
     *@Author      xinyu.huang
     *@Time        2019/11/24 1:41
     */
    fun startup() {
        val port = if (configuration is SocketConfiguration) {
            connectType = Connect.SOCKET.name
            configuration.socketPort
        } else {
            connectType = Connect.HTTP.name
            configuration.httpPort
        }

        if (port <= 0) {
            logger.error("   {}: {} ,must more than 0, so startup failed", connectType, port)
            return
        }

        // Boss线程：由这个线程池提供的线程是boss种类的，用于创建、连接、绑定socket。
        // 在服务器端每个监听的socket都有一个boss线程来处理。在客户端，只有一个boss线程来处理所有的socket。
        val bossGroup = NioEventLoopGroup(configuration.bossThreadSize, DefaultThreadFactory("boss-thread", true))
        // Worker线程：Worker线程执行所有的异步I/O，即处理操作
        val workGroup = NioEventLoopGroup(configuration.workerThreadSize, DefaultThreadFactory("worker-thread", true))
        nettyInputThreadGroup.add(bossGroup)
        try {
            val b = ServerBootstrap()
            b.option(ChannelOption.SO_BACKLOG, 4096)
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(object : ChannelInitializer<SocketChannel>() {
                        @Throws(Exception::class)
                        override fun initChannel(ch: SocketChannel) {
                            buildChannelPipeline(ch, connectType)
                        }
                    })
            // 开始真正绑定端口进行监听
            val future = b.bind("0.0.0.0", port).sync()
            showLog()
            logger.info("================Nafos启动成功，通讯方式：{}， 端口号：{}========", connectType, port)

            future.channel().closeFuture().sync()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            logger.info("server exit...")
            bossGroup.shutdownGracefully()
            workGroup.shutdownGracefully()
        }
    }


    /**
     * 构建ChannelPipeline通道.
     *
     * @param ch SocketChannel
     */
    private fun buildChannelPipeline(ch: SocketChannel, connectType: String) {
        val pipeline = ch.pipeline()
        val sslInfo = configuration.sslInfo

        // 1.开启SSL验证
        if (sslInfo.open) {
            pipeline.addLast("ssl", SslFactory.createSslContext(sslInfo.certFilePath!!, sslInfo.keyFilePath!!).newHandler(ch.alloc()))
        }

        // 2.判断协议类型
        if (connectType == Connect.HTTP.name) {
            pipeline.addLast("http-decoder", HttpRequestDecoder())
            pipeline.addLast("http-aggregator", BuildHttpObjectAggregator(configuration.maxContentLength))
            pipeline.addLast("http-encoder", HttpResponseEncoder())
            //这个handler主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的,增加ChunkedWriteHandler 这个handler我们就不用考虑这个问题了,内部原理看源代码.
            pipeline.addLast("http-chunked", ChunkedWriteHandler())
            // 真正处理用户级业务逻辑的地方
            pipeline.addLast("http-user-defined", HttpServerHandle.getInstance())

        } else {
            // 1.socket方式服务
            // 设置N秒没有读到数据，则触发一个READER_IDLE事件。
            pipeline.addLast(IdleStateHandler((configuration as SocketConfiguration).heartTimeout, 0, 0, TimeUnit.SECONDS))

            // 连接活跃状态处理器
            pipeline.addLast("active", ChannelActiveHandle.getInstance())

            //websocket和socket动态handle添加
            pipeline.addLast("socketChoose", SocketPieplineDynamicHandle.clazz.newInstance() as SocketPieplineDynamicHandle)
        }
    }

    @Volatile
    private var isShowLog = false


    /**
     * 打印log
     */
    private fun showLog() {
        if (isShowLog) {
            return
        }
        println("  .      _   _              ______    ____     _____\n" +
                " /\\\\    | \\ | |     /\\     |  ____|  / __ \\   / ____|\n" +
                "( ( )   |  \\| |    /  \\    | |__    | |  | | | (___\n" +
                "( ( )   | . ` |   / /\\ \\   |  __|   | |  | |  \\___ \\\n" +
                " \\\\/    | |\\  |  / ____ \\  | |      | |__| |  ____) |\n" +
                "  '     |_| \\_| /_/    \\_\\ |_|       \\____/  |_____/   version:1.1.0-release    \n" +
                " =============================================================================================")
        isShowLog = true
    }
}