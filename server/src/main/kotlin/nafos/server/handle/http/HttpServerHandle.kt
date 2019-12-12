package nafos.server.handle.http

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpMethod.*
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpResponseStatus.*
import nafos.server.thread.ExecutorPool
import nafos.server.ThreadLocalHelper
import org.slf4j.LoggerFactory

/**
 * @Description http协议处理装置
 * @Author      xinyu.huang
 * @Time        2019/11/28 0:03
 */
@ChannelHandler.Sharable
class HttpServerHandle : SimpleChannelInboundHandler<Any>() {

    private val logger = LoggerFactory.getLogger(HttpServerHandle::class.java)

    private val allowedMethods = setOf(GET, POST, PUT, HEAD, DELETE, PATCH)

    companion object {
        private val httpServerHandle = HttpServerHandle()

        fun getInstance(): HttpServerHandle {
            return httpServerHandle
        }
    }


    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is FullHttpRequest) {
            sendError(ctx, BAD_REQUEST)
            return
        }

        /**
         * 校验request合法性
         */
        if (!checkRequest(ctx, msg)) {
            return
        }

        /**
         * 处理信息
         */
        val request = msg as NsRequest
        // 根据URI来查找object.method.invoke
        val uri = request.method().toString() + ":" + parseUri(request.uri())
        if ("GET:/favicon.ico" == uri) {
            return
        }
        val httpRouteClassAndMethod = nafos.server.RouteFactory.getHttpHandler(uri)
        // 1.寻找路由失败
        if (httpRouteClassAndMethod == null) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND)
            return
        }
        request.retain()

        // 2.丢进协程处理
        ExecutorPool.getInstance().execute {
            ThreadLocalHelper.threadLocal.set(ThreadInfo(request))
            route(ctx, request, httpRouteClassAndMethod)
        }
//        LatchCountManager.route(CoroutineInfo(request)) {
//
//        }
    }


    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.warn(cause.toString())
        ctx.close()
    }

    /***
     *@Description 截取uri
     *@Author      xinyu.huang
     *@Time        2019/11/28 21:45
     */
    private inline fun parseUri(uri: String?): String? {
        var uri: String? = uri ?: return null
        val index = uri!!.indexOf("?")
        if (-1 != index) {
            uri = uri.substring(0, index)
        }
        return uri
    }

    /***
     *@Description 校验request合法性
     *@Author      xinyu.huang
     *@Time        2019/11/28 0:21
     */
    private inline fun checkRequest(ctx: ChannelHandlerContext, request: FullHttpRequest): Boolean {
        if (!request.decoderResult().isSuccess) {
            sendError(ctx, BAD_REQUEST)
            return false
        }

        //1）跨域方法之前会先收到OPTIONS方法，直接确认
        if (request.method() === OPTIONS) {
            sendOptions(ctx, OK)
            return false
        }

        // 2)确保方法是我们需要的(目前只支持GET | POST  ,其它不支持)
        if (!allowedMethods.contains(request.method())) {
            sendError(ctx, METHOD_NOT_ALLOWED)
            return false
        }

        // 3)uri是有长度的
        val uri = request.uri()
        if (uri.isNullOrBlank()) {
            sendError(ctx, FORBIDDEN)
            return false
        }
        return true
    }

}