package nafos.security

import nafos.NafosServer
import nafos.core.helper.SpringApplicationContextHolder
import nafos.security.currentLimiting.LimitEnum
import nafos.security.currentLimiting.SecurityHttpLimitingHandle

fun NafosServer.registLimiting(type: LimitEnum, timeOut: Int, limitCount: Int):NafosServer {
    var slh = SpringApplicationContextHolder.getSpringBeanForClass(SecurityHttpLimitingHandle::class.java)
    if (type.equals(LimitEnum.LOCALAll)) {
        slh.limitOnType = "LOCAL"
        slh.alllimitTimeout = timeOut
        slh.alllimitCount = limitCount
        SpringApplicationContextHolder.getSpringBeanForClass(ScurityRunner::class.java).registLimiting()
    }
    if (type.equals(LimitEnum.LOCALIP)) {
        slh.limitOnType = "LOCAL"
        slh.iplimitTimeout = timeOut
        slh.iplimitCount = limitCount
        SpringApplicationContextHolder.getSpringBeanForClass(ScurityRunner::class.java).registLimiting()
    }
    if (type.equals(LimitEnum.REDISALL)) {
        slh.limitOnType = "REDIS"
        slh.alllimitTimeout = timeOut
        slh.alllimitCount = limitCount
        SpringApplicationContextHolder.getSpringBeanForClass(ScurityRunner::class.java).registLimiting()
    }
    if (type.equals(LimitEnum.REDISIP)) {
        slh.limitOnType = "REDIS"
        slh.iplimitTimeout = timeOut
        slh.iplimitCount = limitCount
        SpringApplicationContextHolder.getSpringBeanForClass(ScurityRunner::class.java).registLimiting()
    }
    return SpringApplicationContextHolder.getSpringBeanForClass(NafosServer::class.java)
}

