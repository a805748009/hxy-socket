
package com.hxy.nettygo.result.base.handle;

import com.hxy.nettygo.result.base.config.ConfigForSystemMode;
import com.hxy.nettygo.result.base.entry.SocketRouteClassAndMethod;
import com.hxy.nettygo.result.base.inits.InitMothods;
import com.hxy.nettygo.result.base.pool.ExecutorPool;
import com.hxy.nettygo.result.base.pool.MyHttpRunnable;
import com.hxy.nettygo.result.base.pool.MySocketRunnable;
import com.hxy.nettygo.result.base.tools.ObjectUtil;
import com.hxy.nettygo.result.base.tools.SpringApplicationContextHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName:MyWebSocketServerHandler Function: TODO ADD FUNCTION. Date:
 * 2017年10月10日 下午10:19:10
 * 
 * @author hxy
 */

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);
	int activeCount = 0;

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (activeCount != 0) {
			// 3S一字节的心跳，收不到就干掉
			ctx.close();
			activeCount = 0;
		}
		activeCount++;
	}

	/**
	 * channel 通道 action 活跃的
	 * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SocketRouteClassAndMethod route = InitMothods.getTaskHandler("Connect");
		if (ObjectUtil.isNotNull(route)) {
			try {
				route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),
						route.getIndex(), new Object[] { ctx.channel().attr(AttributeKey.valueOf("client")).get() });
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * channel 通道 Inactive 不活跃的
	 * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SocketRouteClassAndMethod route = InitMothods.getTaskHandler("Disconnect");
		if (ObjectUtil.isNotNull(route)) {
			try {
				route.getMethod().invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.getClazz()),
						route.getIndex(), new Object[] { ctx.channel().attr(AttributeKey.valueOf("client")).get() });
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 接收客户端发送的消息 channel 通道 Read 读
	 * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 1)如果是bc-remote调用，走http
		if (msg instanceof FullHttpRequest){
			FullHttpRequest request = (FullHttpRequest) msg;
			String uri = request.uri();
			if (uri.length()>13&&uri.substring(0,13).equals(ConfigForSystemMode.REMOTE_CALL_URI)) {
				// springcloud远程调用
				MyHttpRunnable runnable = new MyHttpRunnable(ctx, request);
				ExecutorPool.getInstance().execute(runnable);
				return;
			}
		}

		// 2)一切就绪，准备抛给线程池,这里将线程池交给spring管理，可以共享spring容器
		MySocketRunnable runnable = new MySocketRunnable(ctx, msg);
		ExecutorPool.getInstance().execute(runnable);
	}




	/**
	 * exception 异常 Caught 抓住 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.channel().disconnect();
	}

	/**
	 * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
