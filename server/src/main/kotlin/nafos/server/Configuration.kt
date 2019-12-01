package nafos.server

import nafos.server.enums.Protocol


/**
 * @Description 服务启动注册配置
 * @Author      xinyu.huang
 * @Time        2019/11/23 18:05
 */
abstract class Configuration(
        /**
         * HTTP启动端口号
         */
        var httpPort: Int = 8080,

        /**
         * SOCKET启动端口号
         */
        var socketPort: Int = 9090,
        /**
         * cpu核心数
         */
        val parallelism: Int = Runtime.getRuntime().availableProcessors(),

        /**
         * netty的boss线程，用于接收网络请求交给work线程
         */
        var bossThreadSize: Int = if (parallelism > 4) 2 else 1,

        /**
         * netty的work线程，用于处理boss传递过来的网络请求
         */
        var workerThreadSize: Int = parallelism / 2 + 1,

        /**
         * 默认通信协议类型，controller参数将根据此进行自动转码
         */
        var defaultProtocol: Protocol = Protocol.JSON,
        /**
         * 消息最大长度
         */
        var maxContentLength:Int = 65535,
        /**
         * 是否开启SSL
         */
        var sslInfo: SslInfo = SslInfo(),

        /**
         * 收到关机指令 kill-15 优先做的操作
         * 处理完之后关闭服务。 会自动等待业务协程处理完毕
         */
        var shutdownEvent: Runnable? = null,

        /**
         * 收到关机指令后，业务最多可等待N毫秒后将强制退出
         */
        var shutdownWaitOverTime:Long = 10000
) {


    fun openSsl(open: Boolean, certFilePath: String, keyFilePath: String) {
        sslInfo = SslInfo(open, certFilePath, keyFilePath)
    }
}


