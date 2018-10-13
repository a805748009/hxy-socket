package nafos.network.bootStrap.netty.handle.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import nafos.core.util.SendUtil;
import nafos.network.bootStrap.netty.handle.socket.ProtocolResolveHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName:MyWebSocketServerHandler Function: TODO ADD FUNCTION. Date:
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
@Component
@ChannelHandler.Sharable
public class WsHandShakeHandle extends SimpleChannelInboundHandler<Object> {

    @Autowired
    WsPacketHandle wsPacketHandle;
    @Autowired
    ProtocolResolveHandle protocolResolveHandle;


    /**
     * 接收客户端发送的消息 channel 通道 Read 读
     * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, Object msg) throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            try {
                handleHttpRequest(context, ((FullHttpRequest) msg));
                context.fireChannelReadComplete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 移除自己
        context.pipeline().remove(this.getClass());


    }


    /**
     * @return void
     * @Author 黄新宇
     * <p>
     * websocket握手方法
     */

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        // 如果HTTP解码失败，返回HHTP异常
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            SendUtil.sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://" + req.headers().get(HttpHeaderNames.HOST) + req.uri(), null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            // 版本不支持
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

}

