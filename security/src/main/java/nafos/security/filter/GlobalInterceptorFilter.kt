package nafos.security.filter

import io.netty.channel.ChannelHandlerContext
import io.netty.util.AttributeKey
import nafos.security.SecurityUtil
import nafos.server.BizException
import nafos.server.handle.http.NsRequest
import nafos.server.interceptors.InterceptorInterface
import nafos.server.interceptors.ResultStatus
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

/**
 * @Description 校验登录状态
 * @Author      xinyu.huang
 * @Time        2019/12/1 17:46
 */
@Component
class GlobalInterceptorFilter : InterceptorInterface {
    override fun interptor(ctx: ChannelHandlerContext, any: Any, param: String?): ResultStatus {
        if (any is NsRequest) {
            val resultStatus = ResultStatus()
            val cookieId = any.nafosCookieId()
            //	1)登录验证
            if (!SecurityUtil.isLogin(cookieId)) {
                resultStatus.isSuccess = false
                resultStatus.bizException = BizException.LOGIN_SESSION_TIME_OUT
                return resultStatus
            }
            //	1.1)更新session存活时间
            SecurityUtil.updateSessionTime(cookieId)
            resultStatus.isSuccess = true
            return resultStatus
        }

        if (any is Int) {
            val resultStatus = ResultStatus()
            val cookieId = ctx.channel().attr(AttributeKey.valueOf<Any>("token")).get() as String

            //	1)登录验证
            if (!SecurityUtil.isLogin(cookieId)) {
                resultStatus.isSuccess = false
                resultStatus.bizException = BizException.LOGIN_SESSION_TIME_OUT
                return resultStatus
            }
            //	1.1)更新session存活时间
            SecurityUtil.updateSessionTime(cookieId)
            resultStatus.isSuccess = true
            return resultStatus
        }
        throw IllegalArgumentException("GlobalInterceptorFilter param [any]  is not code or nsrequest")
    }

}