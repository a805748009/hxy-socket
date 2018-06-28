package com.result.base.inits;

import com.result.base.config.ConfigForSSL;
import com.result.base.config.ConfigForSocketHeart;
import com.result.base.handle.WebSocketServerHandler;
import com.result.base.ssl.SslFactory;
import com.result.base.tools.ShowLogo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @author 作者 huangxinyu 
* @version 创建时间：2018年3月26日 下午2:54:29 
* 类说明 
*/
public class InitSocketNettyServer {
	private static final Logger logger = LoggerFactory.getLogger(InitSocketNettyServer.class);
	
	private static boolean ISRUNNETTY = false;
	
	
	
	/**
	* 
	* @author huangxinyu
	* @version 创建时间：2018年3月12日 下午8:44:02 
	* @Description: TODO(启动websocket项目)  
	* @param     port maxSize
	* @return void    返回类型  
	* @throws
	*/
	public static void runSocket(int port,int maxSize){
		if(ISRUNNETTY){
			logger.error("================Netty已经启动========");
			return;
		}
		
		ShowLogo.consoleout();
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			 // ServerBootstrap 启动NIO服务的辅助启动类,负责初始话netty服务器，并且开始监听端口的socket请求
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel e) throws Exception {
					
					//开启SSL验证
					if(ConfigForSSL.ISOPENSSL){
						e.pipeline().addLast("ssl", SslFactory.createSSLContext().newHandler(e.alloc()));
					}
					
					// 设置N秒没有读到数据，则触发一个READER_IDLE事件。
					e.pipeline().addLast(new IdleStateHandler(ConfigForSocketHeart.readerIdleTime,ConfigForSocketHeart.writerIdleTime,ConfigForSocketHeart.allIdleTime, ConfigForSocketHeart.unit));
					
					// HttpServerCodec：将请求和应答消息解码为HTTP消息
					e.pipeline().addLast("http-codec",new HttpServerCodec());
					
					// HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
					e.pipeline().addLast("aggregator",new HttpObjectAggregator(maxSize));
					
					// ChunkedWriteHandler：向客户端发送HTML5文件
					e.pipeline().addLast("http-chunked",new ChunkedWriteHandler());

					e.pipeline().addLast("WebSocketAggregator",new WebSocketFrameAggregator(maxSize));

					// 在管道中添加我们自己的接收数据实现方法
					e.pipeline().addLast("handler",new WebSocketServerHandler());
				}
			});
			
			logger.info("==========Netty-socket启动完成，端口："+port+",等待客户端连接 ... ...");
//			ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED); //检查内存泄露
			Channel ch = b.bind(port).sync().channel();
			
			ch.closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		ISRUNNETTY = true;
	}

}
