package com.result.base.inits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.result.base.config.ConfigForSSL;
import com.result.base.handle.HttpServerHandler;
import com.result.base.ssl.SslFactory;
import com.result.base.tools.ShowLogo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月26日 下午2:54:10 
* 类说明 
*/
public class InitHttpNettyServer {
	private static final Logger logger = LoggerFactory.getLogger(InitHttpNettyServer.class);
	
	private static boolean ISRUNNETTY = false;
	
	/**
	 * 启动http
	 * @param port
	 * @param maxSize
	 */
	public static void runHttp(int port,int maxSize) {
		if(ISRUNNETTY){
			logger.error("================Netty已经启动========");
			return;
		}
		ShowLogo.consoleout();
		logger.info("================Netty端口启动========"+"port: " + port + " maxSize: " + maxSize);
	
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
							// 这几个都是框架需要，完成HTTP协议的编解码所用
							logger.debug( "one connection " + ch);
							
							//开启SSL验证
							if(ConfigForSSL.ISOPENSSL){
								ch.pipeline().addLast("ssl", SslFactory.createSSLContext().newHandler(ch.alloc()));
							}
							
							ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
							ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(maxSize));// 默认1M
							ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
							//这个handler主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的,增加ChunkedWriteHandler 这个handler我们就不用考虑这个问题了,内部原理看源代码.
							ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
							// 真正处理用户级业务逻辑的地方
							ch.pipeline().addLast("http-user-defined",new HttpServerHandler());
						}
					});
			// 开始真正绑定端口进行监听
			ChannelFuture future = b.bind("0.0.0.0", port).sync();
			
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
			logger.info("server exit...");
		}
		
		ISRUNNETTY = true;
	}
	
	
	
	
	
	
	
}
