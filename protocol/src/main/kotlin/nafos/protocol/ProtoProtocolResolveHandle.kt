package nafos.protocol

import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.util.AttributeKey
import nafos.server.LatchCountManager
import nafos.server.RouteClassAndMethod
import nafos.server.SpringApplicationContextHolder
import nafos.server.interceptors.interceptorDo
import nafos.server.util.ArrayUtil
import org.slf4j.LoggerFactory

/**
 * @Description socket消息处理
 * @Author      xinyu.huang
 * @Time        2019/11/28 23:32
 */
@ChannelHandler.Sharable
class ProtoProtocolResolveHandle : SimpleChannelInboundHandler<ByteArray>() {

    private val logger = LoggerFactory.getLogger(ProtoProtocolResolveHandle::class.java)


    companion object {
        private var protocolResolveHandle = ProtoProtocolResolveHandle()

        fun getInstance(): ProtoProtocolResolveHandle {
            return protocolResolveHandle
        }
    }

    /**
     * 接收客户端发送的消息 channel 通道 Read 读
     * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Throws(Exception::class)
    override fun channelRead0(context: ChannelHandlerContext, contentBytes: ByteArray) {
        if (contentBytes.isEmpty()) {
            return
        }
        var contentBytes = contentBytes

        contentBytes = ZlibMessageHandle.getInstance().unZlibByteMessage(contentBytes)//解压
        contentBytes = Crc32MessageHandle.getInstance().checkCrc32IntBefore(contentBytes)//CRC32校验

        if (contentBytes == null) {
            return  //如果null，证明校验失败，直接返回不处理
        }

        val idByte = ByteArray(4)//前端传过来的ID，原样返回
        System.arraycopy(contentBytes, 0, idByte, 0, 4)

        val uriByte = ByteArray(4)//解析路由的uri
        System.arraycopy(contentBytes, 4, uriByte, 0, 4)
        val serverCode = ArrayUtil.byteArrayToInt(uriByte)

        val messageBody = ByteArray(contentBytes.size - 8)
        if (contentBytes.size > 8) {
            System.arraycopy(contentBytes, 8, messageBody, 0, contentBytes.size - 8)
        }

        logger.debug("收到socket消息，id: $serverCode")

        val socketRouteClassAndMethod = nafos.server.RouteFactory.getSocketHandler(serverCode)
        if (socketRouteClassAndMethod == null) {
            logger.error("{} :找不到匹配的路由", serverCode)
            return
        }

        // 丢进协程处理
        LatchCountManager.start {
            route(context, serverCode, socketRouteClassAndMethod, messageBody, idByte)
        }

    }


    private inline fun route(ctx: ChannelHandlerContext, serverCode: Int, route: RouteClassAndMethod, body: ByteArray, clientCode: ByteArray) {
        // 拦截器
        if (!interceptorDo(ctx, serverCode, route)) {
            return
        }

        val obj = ProtoUtil.deserializeFromByte(body, route.paramType!![0]);

        val zeroParameterType = route.paramType!![0]
        val zeroParamter = if (Channel::class.java.isAssignableFrom(zeroParameterType)) {
            ctx.channel()
        } else {
            ctx.channel().attr<Any>(AttributeKey.valueOf<Any>("client")).get()
        }

        route.method.invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.clazz), route.index!!, zeroParamter, obj!!, clientCode)
    }


    /**
     * exception 异常 Caught 抓住 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.warn(cause.toString())
        ctx.channel().disconnect()
    }

    /**
     * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
     */
    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

}
