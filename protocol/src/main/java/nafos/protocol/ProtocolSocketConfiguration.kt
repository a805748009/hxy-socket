package nafos.protocol

import nafos.server.SocketConfiguration

/**
 * @Description socket服务启动配置
 * @Author      xinyu.huang
 * @Time        2019/11/24 1:30
 */
class ProtocolSocketConfiguration(
        override var port: Int = 9090,
        /**
         * 是否开启CRC32出信息加密
         */
        var isCrc32Out: Boolean = false,
        /**
         * 是否开启CRC32入信息校验
         */
        var isCrc32In: Boolean = false,
        /**
         * 进入流信息是否解密
         */
        var isZlibIn: Boolean = false,
        /**
         * 出去流信息是否加密
         */
        var isZlibOut: Boolean = false,
        /**
         * 出去流加密的最小长度，小于则不加密
         */
        var zlibOutMinLength: Int = 50
) : SocketConfiguration(port = port)