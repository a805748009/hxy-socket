package nafos.server.interceptors

import io.netty.channel.ChannelHandlerContext
import nafos.server.SpringApplicationContextHolder
import nafos.server.handle.http.sendError
import nafos.server.ClassAndMethod
import nafos.server.RouteClassAndMethod
import java.lang.IllegalArgumentException

/***
 *@Description 校验拦截器的返回参数
 *@Author      xinyu.huang
 *@Time        2019/11/28 23:52
 */
inline fun checkResultStatus(filter: ClassAndMethod, ctx: ChannelHandlerContext, any: Any, param: String): Boolean {
    if (filter != null) {
        val resultStatus = filter.method.invoke(
                SpringApplicationContextHolder.getSpringBeanForClass(filter.clazz), filter.index!!, ctx, any, param) as ResultStatus
        if (!resultStatus.isSuccess) {
            sendError(ctx, resultStatus.bizException)
            return false
        }
    }
    return true
}

/***
 *@Description 通用拦截器处理
 *@Author      xinyu.huang
 *@Time        2019/11/28 23:52
 */
inline fun interceptorDo(ctx: ChannelHandlerContext, any: Any, routeClassAndMethod: RouteClassAndMethod): Boolean {
    var filter: ClassAndMethod?
    //  1.前置filter 拦截器
    for (i in 0 until routeClassAndMethod.interceptors.size) {
        val interceptor = routeClassAndMethod.interceptors[i]
        filter = InterceptorsFactory.getInterceptor(interceptor)
        if (filter == null) {
            throw IllegalArgumentException("$interceptor : 拦截器没有实现InterceptorInterface,或者继承AbstractHttpInterceptor. 拦截无效")
            continue
        }
        filter.index = 0
        if(!checkResultStatus(filter, ctx, any, routeClassAndMethod.interceptorParams[i])){
            return false
        }
    }
    return true
}