package nafos.network.bootStrap.netty.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;
import nafos.network.bootStrap.netty.handle.http.HttpServerHandler;
import nafos.network.bootStrap.netty.handle.websocket.BytebufToBinaryFrameHandle;
import nafos.network.bootStrap.netty.handle.websocket.WsHandShakeHandle;
import nafos.network.bootStrap.netty.handle.websocket.WsPacketHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/7/8 下午8:35
 * @Description pipeline添加处理handle
 **/
@Component
public class PipelineAdd {
    @Autowired
    HttpServerHandler httpServerHandler;
    @Autowired
    WsHandShakeHandle wsHandShakeServerHandle;
    @Autowired
    WsPacketHandle wsPacketHandle;
    @Autowired
    BytebufToBinaryFrameHandle bytebufToBinaryFrameHandle;



    /**
     * WEB SOCKET 添加处理器
     * @param ctx
     */
    public  void websocketAdd(ChannelHandlerContext ctx){

        // HttpServerCodec：将请求和应答消息解码为HTTP消息
        ctx.pipeline().addBefore("byteToBuf","http-codec",new HttpServerCodec());

        // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
        ctx.pipeline().addBefore("byteToBuf","aggregator",new HttpObjectAggregator(65535));

        // ChunkedWriteHandler：向客户端发送HTML5文件
        ctx.pipeline().addBefore("byteToBuf","http-chunked",new ChunkedWriteHandler());

        ctx.pipeline().addBefore("byteToBuf","WebSocketAggregator",new WebSocketFrameAggregator(65535));

        // 在管道中添加我们自己的接收数据实现方法
        ctx.pipeline().addBefore("byteToBuf","ws-handShake",wsHandShakeServerHandle);

        // 后续直接走消息处理
        ctx.pipeline().addBefore("byteToBuf","wsPack",wsPacketHandle);

        // 编码。将通用byteBuf编码成binaryWebSocketFrame.通过前面的编码器
        ctx.pipeline().addBefore("byteToBuf","bufToFrame",bytebufToBinaryFrameHandle);


    }


    /**
     * WEB SOCKET 添加处理器
     * @param pipeline
     */
    public  void httpAdd(ChannelPipeline pipeline){

        pipeline.addLast("http-decoder", new HttpRequestDecoder());

        pipeline.addLast("http-aggregator", new HttpObjectAggregator(65535));

        pipeline.addLast("http-encoder", new HttpResponseEncoder());

        //这个handler主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的,增加ChunkedWriteHandler 这个handler我们就不用考虑这个问题了,内部原理看源代码.
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());

        // 真正处理用户级业务逻辑的地方
        pipeline.addLast("http-user-defined",httpServerHandler);
    }

}
