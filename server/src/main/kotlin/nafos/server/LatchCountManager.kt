package nafos.server

import kotlinx.coroutines.*
import nafos.server.handle.http.ThreadInfo
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.LongAdder

/**
 * @Author 黄新宇
 * @Date 2018/8/6 上午11:57
 * @Description 协程计数管理，用作优雅关机的等待
 **/
object LatchCountManager {
    private val logger = LoggerFactory.getLogger(LatchCountManager::class.java)

    private val latchCount = LongAdder()

    init {
        log()
    }

    /**
     * @Desc  打印当前运行协程数
     * @Author   hxy
     * @Time   2019/8/6
     */
    private fun log() {
        GlobalScope.launch {
            while (true) {
                delay(5_000)
                logger.debug("协程当前进行数量-{} ", latchCount.sum())
            }
        }
    }

    /**
     * @Desc 启动协程方法，计数器+1，结束-1
     * @Param  method 协程内方法体
     * @Author   hxy
     * @Time   2019/8/6
     */
    fun start(method: suspend () -> Unit) {
        GlobalScope.launch {
            latchCount.increment()
            try {
                method()
            } finally {
                latchCount.decrement()
            }
        }
    }

    internal fun route(threadInfo: ThreadInfo, method: suspend () -> Unit) {
        GlobalScope.launch (Dispatchers.Default + ThreadLocalHelper.threadLocal.asContextElement(threadInfo) ){
            latchCount.increment()
            try {
                method()
            } finally {
                latchCount.decrement()
            }
        }
    }

    /**
     * @Desc 等待协程执行结束
     * @Author   hxy
     * @Time   2019/8/6
     */
    fun waitOver(timeOutSeconds: Long): Long {
        var surplusCount = latchCount.sum()
        var time = 0L
        while (surplusCount > 0L) {
            Thread.sleep(2000)
            surplusCount = latchCount.sum()

            //超时设定
            time += 2L
            if (time >= timeOutSeconds) {
                logger.info("剩余未执行完协程数-{}", surplusCount)
                break
            }
        }
        return surplusCount
    }


}