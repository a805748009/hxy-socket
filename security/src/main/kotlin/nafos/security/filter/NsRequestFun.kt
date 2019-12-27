package nafos.security.filter

import io.netty.handler.codec.http.cookie.DefaultCookie
import nafos.server.BizException
import nafos.server.ThreadLocalHelper
import nafos.server.handle.http.NsRequest
import nafos.server.handle.http.NsRespone
import nafos.server.util.AESUtil
import java.net.URLDecoder
import java.net.URLEncoder

internal const val cookieStart = "nafosCookie"

fun NsRequest.nafosCookieId(): String? {
    if (headers().contains(cookieStart)) {
        return AESUtil.decrypt(URLDecoder.decode(headers().get(cookieStart)))
    }
    val nafosCookieAes = getCookie(cookieStart) ?: return null
    return AESUtil.decrypt(URLDecoder.decode(nafosCookieAes))
}

inline fun currentNafosCookie(): String {
    return ThreadLocalHelper.getRequest().nafosCookieId() ?: throw BizException.LOGIN_SESSION_TIME_OUT
}

fun NsRespone.setNafosCookieId(cookieId: String) {
    setCookie("nafosCookie", URLEncoder.encode(AESUtil.encrypt(cookieId)))
}