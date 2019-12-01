package nafos.server

/**
 * @Description socket服务启动配置
 * @Author      xinyu.huang
 * @Time        2019/11/24 1:30
 */
open class SocketConfiguration(
        /**
         * Socket启动端口号
         */
        open var port: Int = 9090,
        /**
         * 心跳事件 - 秒为计时单位
         */
        open var heartTimeout: Long = 5

) : Configuration(socketPort = port)