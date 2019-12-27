package nafos.server.handle.socket

import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.util.AttributeKey
import io.netty.util.CharsetUtil
import nafos.server.*
import nafos.server.handle.http.ThreadInfo
import nafos.server.interceptors.interceptorDo
import nafos.server.thread.ExecutorPool
import nafos.server.util.JsonUtil
import org.slf4j.LoggerFactory

/**
 * @Description socket消息处理
 * @Author      xinyu.huang
 * @Time        2019/11/28 23:32
 */
@ChannelHandler.Sharable
class ProtocolResolveHandle : SimpleChannelInboundHandler<Any>() {

    private val logger = LoggerFactory.getLogger(ProtocolResolveHandle::class.java)


    companion object {
        private var protocolResolveHandle = ProtocolResolveHandle()

        fun getInstance(): ProtocolResolveHandle {
            return protocolResolveHandle
        }
    }

    /**
     * 接收客户端发送的消息 channel 通道 Read 读
     * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Throws(Exception::class)
    override fun channelRead0(context: ChannelHandlerContext, any: Any) {
        val contentStr = if (any is ByteBuf) {
            any.toString(CharsetUtil.UTF_8)
        } else {
            any as String
        }
        val clientCode = contentStr.substring(0, contentStr.indexOf("|")).toInt()
        val surplusStr = contentStr.substring(contentStr.indexOf("|") + 1)
        val serverCode = surplusStr.substring(0, contentStr.indexOf("|")).toInt()
        val contentJosnStr = surplusStr.substring(contentStr.indexOf("|") + 1)

        logger.debug("收到socket消息，id: $serverCode")

        val socketRouteClassAndMethod = nafos.server.RouteFactory.getSocketHandler(serverCode)
        if (socketRouteClassAndMethod == null) {
            logger.error("{} :找不到匹配的路由", serverCode)
            return
        }

        // 丢进协程处理
//        LatchCountManager.start {
//            route(context, serverCode, socketRouteClassAndMethod, contentJosnStr, clientCode)
//        }
        ExecutorPool.getInstance().execute {
            route(context, serverCode, socketRouteClassAndMethod, contentJosnStr, clientCode)
        }

    }


    private inline fun route(ctx: ChannelHandlerContext, serverCode: Int, route: RouteClassAndMethod, body: String, clientCode: Int) {
        // 拦截器
        if (!interceptorDo(ctx, serverCode, route)) {
            return
        }

        var obj: Any? = if (Map::class.java.isAssignableFrom(route.paramType!![1])) {
            JsonUtil.jsonToMap(body)
        } else {
            JsonUtil.json2Object(body, route.paramType!![1]!!)
        }

        val zeroParameterType = route.paramType!![0]
        val zeroParamter = if (Channel::class.java.isAssignableFrom(zeroParameterType)) {
            ctx.channel()
        } else {
            ctx.channel().attr<Any>(AttributeKey.valueOf<Any>("client")).get()
        }

        try {
            route.method.invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.clazz), route.index!!, zeroParamter, obj, clientCode)
        } catch (e: BizException){
            ctx.channel().writeAndFlush("${e.status}|${JsonUtil.toJson(e)}")
        }

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
