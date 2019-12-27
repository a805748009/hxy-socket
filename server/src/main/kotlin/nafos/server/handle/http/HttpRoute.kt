package nafos.server.handle.http

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import nafos.server.SpringApplicationContextHolder
import nafos.server.ThreadLocalHelper
import nafos.server.interceptors.interceptorDo
import nafos.server.BizException
import nafos.server.HttpRouteClassAndMethod
import nafos.server.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress


private val logger: Logger = LoggerFactory.getLogger("HttpRoute.kt")

fun route(ctx: ChannelHandlerContext, request: NsRequest, httpRouteClassAndMethod: HttpRouteClassAndMethod) {
    try {
        // 拦截器
        if (!interceptorDo(ctx, request, httpRouteClassAndMethod)) {
            return
        }

        // 2.消息入口处理
        val contentObj: Array<Any?>
        try {
            contentObj = getRequestParams(request, httpRouteClassAndMethod)
        } catch (e: Exception) {
            sendError(ctx, HttpResponseStatus.NO_CONTENT)
            e.printStackTrace()
            return
        }

        val insocket = ctx.channel().remoteAddress() as? InetSocketAddress
        if (insocket != null) {
            val clientIP = insocket.address.hostAddress
            request.ip = clientIP
        }

        if(!ctx.channel().isActive){
            return
        }

        // 3.寻找路由成功,返回结果
        val returnObj = routeMethod(httpRouteClassAndMethod, *contentObj)
        sendMethod(returnObj, ctx, request)
    } finally {
        request.release()
    }
}


/**
 * @param
 * @return java.lang.Object
 * @Author 黄新宇
 * @date 2018/7/4 下午4:36
 * @Description(路由)
 */
private inline fun routeMethod(route: HttpRouteClassAndMethod, vararg any: Any?): Any {
    return try {
        route.method.invoke(SpringApplicationContextHolder.getSpringBeanForClass(route.clazz), route.index!!, *any)
    } catch (e: BizException) {
        e
    } catch (e: Exception) {
        logger.error("程序异常：{}", e.toString())
        e.printStackTrace()
        HttpResponseStatus.INTERNAL_SERVER_ERROR
    }

}

/**
 *@Description 发送后置处理器
 *@Author      xinyu.huang
 *@Time        2019/11/28 22:16
 */
private inline fun sendMethod(any: Any?, context: ChannelHandlerContext, request: FullHttpRequest) {
    //error处理
    if (any is BizException) {
        sendError(context, any)
        return
    }
    if (any is HttpResponseStatus) {
        sendError(context, any)
        return
    }

    if (any is NsRespone) {
        ThreadLocalHelper.threadLocalRemove()
        if (context.channel().isActive) {
            context.writeAndFlush(any).addListener(ChannelFutureListener.CLOSE)
        }
        return
    }

    send(context, JsonUtil.toJsonIsNotNull(any!!), request)
}


/**
 *@Description 发送的返回值
 *@Author      xinyu.huang
 *@Time        2019/11/28 22:26
 */
private inline fun <T> send(ctx: ChannelHandlerContext, context: T?, request: FullHttpRequest) {
    val response = ThreadLocalHelper.getRespone()
    //  head方法只需要状态，不需要body
    if (request.method() != HttpMethod.HEAD) {
        when (context) {
            is String -> {
                response.content().writeBytes((context as String).toByteArray())
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8")
            }
            else -> {
                //为null的时候
                response.content().writeBytes("".toByteArray())
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8")
            }
        }
    }
    ThreadLocalHelper.threadLocalRemove()

    if (ctx.channel().isActive) {
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }
}
