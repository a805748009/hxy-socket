package tool.quasar

import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch


class FiberTest {
}


fun main(args: Array<String>) {
    GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出

    }
    GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("Worl2d!") // 在延迟后打印输出

    }
    println("Hello,") // 协程已在等待时主线程还在继续
    Thread.sleep(20000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
}
