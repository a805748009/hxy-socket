package nafos.core.monitor

import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class FiberDo {

    companion object {
        /**
         * 采用协程打印runwatch
         */
        fun doPrintRunwatch(millisecond: Long) {
            GlobalScope.launch {
                while (true) {
                    delay(millisecond)
                    RunWatch.print()
                }
            }
        }

        /**
         * 采用协程打印系统信息
         */
        fun doPrintSystemMonitor(millisecond: Long) {
            GlobalScope.launch {
                while (true) {
                    delay(millisecond)
                    SystemMonitor.allLog()
                }
            }
        }
    }
}

