package nafos.server

/**
 * @Description http服务启动注册配置
 * @Author      xinyu.huang
 * @Time        2019/11/24 1:26
 */
class HttpConfiguration(
        /**
         * 设置option头部信息，一般用来设置跨域请求允许通过的头部
         */
        var crossHeads: MutableMap<String, String> = mutableMapOf(),
        /**
         * HTTP启动端口号
         */
        port: Int = 8080


) : Configuration(httpPort = port) {
    /***
     *@Description 注册单个头部信息
     *@Author      xinyu.huang
     *@Time        2019/11/23 18:32
     */
    fun registerCrossHeader(name: CharSequence, value: String) {
        crossHeads[name.toString()] = value
    }

    fun setPort(value:Int){
        this.httpPort = value
    }
}