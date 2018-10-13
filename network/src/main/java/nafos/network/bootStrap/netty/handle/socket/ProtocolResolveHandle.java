package nafos.network.bootStrap.netty.handle.socket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nafos.core.entry.ClassAndMethod;
import nafos.core.helper.ClassAndMethodHelper;
import nafos.core.mode.InitMothods;
import nafos.core.util.ObjectUtil;
import nafos.network.bootStrap.netty.handle.currency.Crc32MessageHandle;
import nafos.network.bootStrap.netty.handle.currency.ZlibMessageHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName:MyWebSocketServerHandler  把消息进行zlib和crc32相关处理，然后选择相关线程处理
 * 2017年10月10日 下午10:19:10
 *
 * @author hxy
 */
@Component
@ChannelHandler.Sharable
public class ProtocolResolveHandle extends SimpleChannelInboundHandler<byte[]> {
    /** WebSocket握手的协议前缀 */
    private static final String WEBSOCKET_PREFIX = "GET /";


    @Autowired
    ZlibMessageHandle zlibMessageHandle;
    @Autowired
    Crc32MessageHandle crc32MessageHandle;
    @Autowired
    SocketExecutorPoolChoose socketExecutorPoolChoose;


    /**
     * 接收客户端发送的消息 channel 通道 Read 读
     * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, byte[] contentBytes) throws Exception {

        if(contentBytes.length==0)return;

        contentBytes = zlibMessageHandle.unZlibByteMessage(contentBytes);//解压
        contentBytes = crc32MessageHandle.checkCrc32IntBefore(contentBytes);//CRC32校验

        if(ObjectUtil.isNull(contentBytes))return; //如果null，证明校验失败，直接返回不处理

        // 安全验证filter
        ClassAndMethod filter = InitMothods.getSocketSecurityFilter();
        if(!ClassAndMethodHelper.checkResultStatus(filter,context,contentBytes)) return;

        //选择线程池
        socketExecutorPoolChoose.choosePool(context,contentBytes);

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

