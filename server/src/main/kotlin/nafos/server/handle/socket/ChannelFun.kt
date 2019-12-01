package nafos.server.handle.socket

import io.netty.channel.Channel
import nafos.server.util.JsonUtil

inline fun Channel.send(cliendCode: Int, msg: String) {
    this.writeAndFlush("$cliendCode|msg")
}

inline fun Channel.send(cliendCode: Int, any: Any) {
    this.writeAndFlush("$cliendCode|${JsonUtil.toJson(any)}")
}