package nafos.server.handle.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName:MyWebSocketServerHandler Function: 过滤websocket不相关消息协议，并将bytebuf传输下节点
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
@ChannelHandler.Sharable
public class WsPacketHandle extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WsPacketHandle.class);

    private static WsPacketHandle wsPacketHandle = null;

    public static WsPacketHandle getInstance() {
        if (wsPacketHandle == null) {
            synchronized (WsHandShakeHandle.class) {
                if (wsPacketHandle == null) {
                    wsPacketHandle = new WsPacketHandle();
                }
            }
        }
        return wsPacketHandle;
    }

    /**
     * 接收客户端发送的消息 channel 通道 Read 读
     * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, WebSocketFrame frame) throws Exception {

        // 1.判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            context.close();
            return;
        }

        // 2.判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            context.channel().write(new PongWebSocketFrame(frame.content()));
            return;
        }

        // 3.如果是文本消息
        if (frame instanceof TextWebSocketFrame) {
            String msg = ((TextWebSocketFrame) frame).text();
            // 采用空格ping消息
            if (" ".equals(msg)) {
                logger.debug("收到心跳 " + context.channel().toString());
                return;
            }
            context.fireChannelRead(msg);
        }

        // 4.如果是字节消息
        if (frame instanceof BinaryWebSocketFrame) {
            byte[] contentBytes = new byte[frame.content().readableBytes()];
            frame.content().readBytes(contentBytes);
            context.fireChannelRead(contentBytes);
        }

    }


}

