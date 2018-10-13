package nafos.network.bootStrap.netty.handle.currency;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import nafos.core.util.SpringApplicationContextHolder;
import nafos.network.bootStrap.netty.handle.PipelineAdd;
import nafos.network.bootStrap.netty.handle.socket.BytebufToByteHandle;

import java.util.List;

/**
 * 协议初始化解码器.
 *
 * 用来判定实际使用什么协议.</b>
 *
 */
public class SocketChooseHandle extends ByteToMessageDecoder {
	/** 默认暗号长度为23 */
	private static final int MAX_LENGTH = 23;
	/** WebSocket握手的协议前缀 */
	private static final String WEBSOCKET_PREFIX = "GET /";


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		String protocol = getBufStart(in);
		if (protocol.startsWith(WEBSOCKET_PREFIX)) {
			SpringApplicationContextHolder.getSpringBeanForClass(PipelineAdd.class).websocketAdd(ctx);

			ctx.pipeline().remove(LengthFieldBasedFrameDecoder.class);
			ctx.pipeline().remove(LengthFieldPrepender.class);
			ctx.pipeline().remove(BytebufToByteHandle.class);
		}

		in.resetReaderIndex();
		ctx.pipeline().remove(this.getClass());

	}





	private String getBufStart(ByteBuf in){
		int length = in.readableBytes();
		if (length > MAX_LENGTH) {
			length = MAX_LENGTH;
		}

		// 标记读位置
		in.markReaderIndex();
		byte[] content = new byte[length];
		in.readBytes(content);
		return new String(content);
	}
}