package nafos.protocol

import io.netty.channel.Channel
import nafos.server.util.ArrayUtil

fun Channel.send(cliendCode: ByteArray, any: Any){
    this.writeAndFlush(ArrayUtil.concat(cliendCode,ProtoUtil.serializeToByte(any)))
}