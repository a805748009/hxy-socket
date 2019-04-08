package tool.quasar

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nafos.bootStrap.handle.http.HttpRouteHandle
import java.util.concurrent.CountDownLatch


class FiberTest {
}


fun main(args: Array<String>) {
//    GlobalScope.launch { // 在后台启动一个新的协程并继续
//            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
//            println("World!") // 在延迟后打印输出
//
//    }
//    GlobalScope.launch { // 在后台启动一个新的协程并继续
//            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
//            println("Worl2d!") // 在延迟后打印输出
//
//    }
//    println("Hello,") // 协程已在等待时主线程还在继续
//    Thread.sleep(20000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
//

    runBlocking<Unit> {
        HttpRouteHandle().route(null,null,null)
        launch {
            // 运行在父协程的上下文中，即 runBlocking 主协程
            println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")

        }

        val start = System.currentTimeMillis()
        for (i in 1..50) {
            var op = launch {
                // 在后台启动一个新的协程并继续
                delay(1000)
                println(1)
            }
        }
        delay(5000)
        println(System.currentTimeMillis() - start)
    }

}

suspend fun ok(){
    delay(5000)
}

