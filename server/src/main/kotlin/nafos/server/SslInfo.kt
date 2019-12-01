package nafos.server

/**
 * @Description Ssl相关信息
 * @Author      xinyu.huang
 * @Time        2019/11/24 1:24
 */
class SslInfo(
        var open: Boolean = false,

        var certFilePath: String? = null,

        var keyFilePath: String? = null
)