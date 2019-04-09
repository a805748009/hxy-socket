//package nafos.core.monitor
//
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//
//class FiberDo {
//
//    companion object {
//        /**
//         * 采用协程打印runwatch
//         */
//        fun doPrintRunwatch(millisecond: Long) {
//            GlobalScope.launch {
//                while (true) {
//                    delay(millisecond)
//                    RunWatch.print()
//                }
//            }
//        }
//
//        /**
//         * 采用协程打印系统信息
//         */
//        fun doPrintSystemMonitor(millisecond: Long) {
//            GlobalScope.launch {
//                while (true) {
//                    delay(millisecond)
//                    SystemMonitor.allLog()
//                }
//            }
//        }
//    }
//}
//
