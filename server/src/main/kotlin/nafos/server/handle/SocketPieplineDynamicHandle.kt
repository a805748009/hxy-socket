package nafos.server.handle

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator
import io.netty.handler.stream.ChunkedWriteHandler
import nafos.server.NafosServer
import nafos.server.handle.socket.ProtocolResolveHandle
import nafos.server.handle.websocket.BytebufToTextFrameHandle
import nafos.server.handle.websocket.WsHandShakeHandle
import nafos.server.handle.websocket.WsPacketHandle

/***
 * @Description 动态handle添加处理，根据类型选择相关协议解析器
 * @Author      xinyu.huang
 * @Time        2019/11/30 22:49
 */
open class SocketPieplineDynamicHandle : ByteToMessageDecoder() {

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: List<Any>) {
        val configuration = NafosServer.configuration
        ctx.pipeline().apply {
            val protocol = getBufStart(`in`)
            if (protocol.startsWith(WEBSOCKET_PREFIX)) {
                // HttpServerCodec：将请求和应答消息解码为HTTP消息
                addLast("http-codec", HttpServerCodec())
                // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
                addLast("aggregator", HttpObjectAggregator(configuration.maxContentLength))
                // ChunkedWriteHandler：向客户端发送HTML5文件
                addLast("http-chunked", ChunkedWriteHandler())
                addLast("WebSocketAggregator", WebSocketFrameAggregator(configuration.maxContentLength))
                // 在管道中添加我们自己的接收数据实现方法
                addLast("ws-handShake", WsHandShakeHandle.getInstance())
                // 后续直接走消息处理
                addLast("in-wsPack", WsPacketHandle.getInstance())
                // 编码。将通用byteBuf编码成binaryWebSocketFrame.通过前面的编码器
                addLast("out-bufToFrame", BytebufToTextFrameHandle.getInstance())
            } else {
                //tcpsocket编码解码handle,用来防止沾包拆包的问题
                addLast("lengthEncode", LengthFieldPrepender(4, false))
                addLast("lengthDecoder", LengthFieldBasedFrameDecoder(2000, 0, 4, 0, 4))
            }
        }

        //因为接收类型的泛型不对，所以在websocket握手的时候不会进入该handle
        //此handle为最后的socket消息分解，web和tcp通用
        ctx.pipeline().addLast("out-byteToBuf", nafos.server.handle.socket.ByteArrayOutboundHandle.getInstance())
        ctx.pipeline().addLast("protocolResolve", ProtocolResolveHandle.getInstance())

        `in`.resetReaderIndex()
        ctx.pipeline().remove(this.javaClass)
    }


    protected fun getBufStart(`in`: ByteBuf): String {
        var length = `in`.readableBytes()
        if (length > MAX_LENGTH) {
            length = MAX_LENGTH
        }
        // 标记读位置
        `in`.markReaderIndex()
        val content = ByteArray(length)
        `in`.readBytes(content)
        return String(content)
    }


    companion object {
        var clazz:Class<*> = SocketPieplineDynamicHandle::class.java

        /**
         * 默认暗号长度为23
         */
        const val MAX_LENGTH = 23
        /**
         * WebSocket握手的协议前缀
         */
        const val WEBSOCKET_PREFIX = "GET /"

    }
}