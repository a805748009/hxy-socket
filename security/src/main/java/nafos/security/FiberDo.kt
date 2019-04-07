package nafos.security

import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import nafos.core.monitor.RunWatch
import nafos.security.cache.CacheMapDao
import org.slf4j.LoggerFactory

class FiberDo {
    companion object {
        private val logger = LoggerFactory.getLogger(FiberDo::class.java)

        /**
         * 定时清除cacheMap中过期的session记录
         */
        fun doClearTimeOutSession(millisecond: Long) {
            GlobalScope.launch {
                while (true) {
                    delay(millisecond)
                    CacheMapDao.delTimeOut()
                    logger.debug("清除cacheMap中login信息, 【over】")
                }
            }
        }
    }
}

