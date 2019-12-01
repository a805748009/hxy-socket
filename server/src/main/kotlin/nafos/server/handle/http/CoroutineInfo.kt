package nafos.server.handle.http

import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion

/**
 * @Description 协程上下文bean
 * @Author      xinyu.huang
 * @Time        2019/11/30 19:04
 */
data class CoroutineInfo(
        var nafosRequest: NsRequest? = null,

        var nafosRespone: NsRespone? = null,

        var bizInfo: Any? = null


) {
    constructor(nafosRequest: NsRequest, nafosRespone: NsRespone) : this() {
        this.nafosRequest = nafosRequest
        this.nafosRespone = nafosRespone
    }

    constructor(nafosRequest: NsRequest) : this() {
        this.nafosRequest = nafosRequest
        this.nafosRespone = NsRespone(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
    }
}