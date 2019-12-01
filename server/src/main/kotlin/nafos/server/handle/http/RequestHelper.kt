package nafos.server.handle.http

import io.netty.handler.codec.http.HttpMethod
import io.netty.util.CharsetUtil
import nafos.server.CoroutineLocalHelper
import nafos.server.annotation.http.RequestParam
import nafos.server.BizException
import nafos.server.HttpRouteClassAndMethod
import nafos.server.util.BeanToMapUtil
import nafos.server.util.JsonUtil
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("RequestHelper.kt")

fun getRequestParams(nsRequest: NsRequest, route: HttpRouteClassAndMethod): Array<Any?> {
    val requestParams = nsRequest.requestParams
    val list = mutableListOf<Any?>()
    for (parameter in route.parameters) {
        /**
         *  @RequestParam 标记参数
         */
        var fieldObj: Any? = null
        val requestParam = parameter.getDeclaredAnnotation(nafos.server.annotation.http.RequestParam::class.java!!)
        if (requestParam != null) {
            val any = nsRequest.requestParams[if ("" == requestParam.value) requestParam.name else requestParam.value]
            if (any == null && requestParam.required) {
                if (requestParam.required) {
                    logger.error("======{},参数{}不能为空 ", route.method.toString(), requestParam.value)
                    throw nafos.server.BizException.PARAM_NOT_NULL(requestParam.value)
                } else {
                    list.add(null)
                    continue
                }
            }
            fieldObj = castClass(any!!, parameter.type)
            list.add(fieldObj)
            continue
        }

        /**
         * NsRequest参数
         */
        if (NsRequest::class.java.isAssignableFrom(parameter.type)) {
            list.add(nsRequest)
            continue
        }

        /**
         * NsRespone参数
         */
        if (NsRespone::class.java.isAssignableFrom(parameter.type)) {
            list.add(nafos.server.CoroutineLocalHelper.getRespone())
            continue
        }

        /**
         * 其他参数，比如Map，实体类来接收
         */
        if (nsRequest.method() === HttpMethod.GET && Map::class.java.isAssignableFrom(parameter.type)) {
            list.add(requestParams)
        } else if (Map::class.java.isAssignableFrom(parameter.type)) {
            list.add(nsRequest.bodyParams)
        } else {
            //接受参数为实体类，json下直接json转（否则map无法转嵌套），fromdata用map转。
            var strContentType = nsRequest.headers().get("Content-Type")
            strContentType = if (strContentType.isNullOrBlank()) "" else strContentType.trim()
            if (strContentType.contains("application/json")) {
                val jsonBuf = nsRequest.content()
                val jsonStr = jsonBuf.toString(CharsetUtil.UTF_8)
                list.add(JsonUtil.json2Object(jsonStr, parameter.type))
            } else {
                list.add(nafos.server.util.BeanToMapUtil.mapToObject(nsRequest.getFormParams(), parameter.type))
            }
        }

    }
    return list.toTypedArray()!!
}


/***
 *@Description 转换类型
 *@Author      xinyu.huang
 *@Time        2019/11/28 22:40
 */
private inline fun castClass(any: Any, clazz: Class<*>): Any {
    var any = any.toString()

    if (clazz == String::class.java) {
        return any
    }

    if (clazz == Int::class.javaPrimitiveType || clazz == Int::class.java) {
        return any.toInt()
    }

    if (clazz == Boolean::class.javaPrimitiveType) {
        return any.toBoolean()
    }

    if (clazz == Double::class.javaPrimitiveType) {
        return any.toDouble()
    }

    if (clazz == Long::class.javaPrimitiveType) {
        return any.toLong()
    }
    return any
}
