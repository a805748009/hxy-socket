package nafos.server.handle.http

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion.HTTP_1_1
import io.netty.util.CharsetUtil
import nafos.server.CoroutineLocalHelper
import nafos.server.BizException


var headers: MutableMap<String, String>? = null

inline fun sendError(ctx: ChannelHandlerContext, status: HttpResponseStatus) {
    // 设置到response对象
    val response = DefaultFullHttpResponse(HTTP_1_1, status,
            Unpooled.copiedBuffer(nafos.server.BizException(status.code(), status.reasonPhrase()).toString(), CharsetUtil.UTF_8))
    resSetDefaultHead(response)
    nafos.server.CoroutineLocalHelper.coroutineLocalRemove()
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
}

inline fun sendError(ctx: ChannelHandlerContext, bizException: nafos.server.BizException) {
    // 设置到response对象
    val response = DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(bizException.status),
            Unpooled.copiedBuffer(bizException.toString(), CharsetUtil.UTF_8))
    resSetDefaultHead(response)
    nafos.server.CoroutineLocalHelper.coroutineLocalRemove()
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
}

inline fun sendOptions(ctx: ChannelHandlerContext, status: HttpResponseStatus) {
    // 设置到response对象
    val response = DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("{}", CharsetUtil.UTF_8))
    headers?.run {
        this.entries.forEach {
            response.headers().set(it.key, it.value)
        }
    } ?: resSetDefaultHead(response)
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
}

fun setCrossHeads(map: MutableMap<String, String>) {
    headers = map
}

fun setCrossHeads(name: CharSequence, value: String) {
    if (headers == null) {
        headers = mutableMapOf()
    }
    headers!![name.toString()] = value
}


inline fun resSetDefaultHead(response: FullHttpResponse) {
    response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
    response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*")
    response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "PUT,POST,GET,DELETE,OPTIONS")
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8")
    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())
}

inline fun OK(): Map<String, Any> {
    return hashMapOf()
}

