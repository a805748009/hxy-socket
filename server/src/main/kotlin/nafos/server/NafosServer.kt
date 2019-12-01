package nafos.server

import nafos.server.interceptors.InterceptorsFactory
import nafos.server.start.NafosRunnerExecute
import org.slf4j.LoggerFactory
import java.lang.Class as javaClass

/**
 * @Description 服务启动类
 * @Author      xinyu.huang
 * @Time        2019/11/23 18:41
 */
class NafosServer {
    private val logger = LoggerFactory.getLogger(NafosServer::class.java)

    companion object {
        @JvmField
        var configuration: Configuration = HttpConfiguration()
    }


    /***
     *@Description 启动服务
     *@Author      xinyu.huang
     *@Time        2019/11/23 18:59
     */
    fun start(mainClazz: javaClass<*>) {
        /**
         * 1.注册context
         */
        val context = SpringApplicationContextHolder.getOrInitContext(mainClazz)
        /**
         * 2.注册路由 和拦截器
         */
        RouteFactory(context)
        InterceptorsFactory(context)
        /**
         * 3.注册关机事件
         */
        Runtime.getRuntime().addShutdownHook(Thread(configuration.shutdownEvent))
        Runtime.getRuntime().addShutdownHook(Thread{
            NettyEngine.closeInputThreadGroup()
            LatchCountManager.waitOver(configuration.shutdownWaitOverTime)
            logger.info("======================>>>>>>>>>>>关闭服务<<<<<<<<<<<========================")
        })
        /**
         * 4.执行开机启动任务
         */
        NafosRunnerExecute().execute()
        /**
         * 5.启动netty
         */
        Thread{
            NettyEngine(configuration).startup()
        }.start()

    }
}




fun httpServer(configuration: HttpConfiguration): NafosServer {
    NafosServer.configuration = configuration
    return NafosServer()
}

fun httpServer(): NafosServer {
    NafosServer.configuration = HttpConfiguration()
    return NafosServer()
}

fun socketServer(configuration: SocketConfiguration): NafosServer {
    NafosServer.configuration = configuration
    return NafosServer()
}

fun socketServer(): NafosServer {
    NafosServer.configuration = SocketConfiguration()
    return NafosServer()
}


