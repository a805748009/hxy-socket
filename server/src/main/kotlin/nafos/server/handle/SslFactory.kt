package nafos.server.handle

import io.netty.handler.ssl.*
import org.slf4j.LoggerFactory
import java.io.File
import javax.net.ssl.SSLException

/**
 * @Description ssl处理器
 * @Author      xinyu.huang
 * @Time        2019/11/24 19:24
 */
object SslFactory {

    private val logger = LoggerFactory.getLogger(SslFactory::class.java)


    private var sslContext: SslContext? = null

    private val cipherList = listOf("ECDHE-RSA-AES128-SHA", "ECDHE-RSA-AES256-SHA", "AES128-SHA", "AES256-SHA", "DES-CBC3-SHA")

    fun createSslContext(certFilePath: String, keyFilePath: String): SslContext {
        if (null == sslContext) {
            synchronized(SslFactory::class.java) {
                if (null == sslContext) {
                    val certFile = File(certFilePath)
                    val keyFile = File(keyFilePath)//此处需要PKS8编码的.key后缀文件
                    try {
                        sslContext = SslContextBuilder.forServer(certFile, keyFile)
                                .sslProvider(SslProvider.OPENSSL)
                                .clientAuth(ClientAuth.NONE)
                                .ciphers(cipherList)
                                .build()
                    } catch (e: SSLException) {
                        logger.error("SSL错误：$e")
                    }
                }
            }
        }
        return sslContext!!
    }
}