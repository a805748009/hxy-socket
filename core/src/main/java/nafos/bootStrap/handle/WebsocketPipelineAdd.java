package nafos.bootStrap.handle;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;
import nafos.bootStrap.handle.websocket.BytebufToBinaryFrameHandle;
import nafos.bootStrap.handle.websocket.WsHandShakeHandle;
import nafos.bootStrap.handle.websocket.WsPacketHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 黄新宇
 * @Date 2018/7/8 下午8:35
 * @Description pipeline添加处理handle
 **/
@Component
public class WebsocketPipelineAdd implements PipelineAdd {

    @Autowired
    WsHandShakeHandle wsHandShakeServerHandle;
    @Autowired
    WsPacketHandle wsPacketHandle;
    @Autowired
    BytebufToBinaryFrameHandle bytebufToBinaryFrameHandle;

    @Override
    public void handAdd(ChannelPipeline pipeline) {
        // HttpServerCodec：将请求和应答消息解码为HTTP消息
        pipeline.addBefore("out-byteToBuf", "http-codec", new HttpServerCodec());

        // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
        pipeline.addBefore("out-byteToBuf", "aggregator", new HttpObjectAggregator(65535));

        // ChunkedWriteHandler：向客户端发送HTML5文件
        pipeline.addBefore("out-byteToBuf", "http-chunked", new ChunkedWriteHandler());

        pipeline.addBefore("out-byteToBuf", "WebSocketAggregator", new WebSocketFrameAggregator(65535));

        // 在管道中添加我们自己的接收数据实现方法
        pipeline.addBefore("out-byteToBuf", "ws-handShake", wsHandShakeServerHandle);

        // 后续直接走消息处理
        pipeline.addBefore("out-byteToBuf", "in-wsPack", wsPacketHandle);

        // 编码。将通用byteBuf编码成binaryWebSocketFrame.通过前面的编码器
        pipeline.addBefore("out-byteToBuf", "out-bufToFrame", bytebufToBinaryFrameHandle);
    }
}
