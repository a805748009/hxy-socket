package nafos.server.relation

import io.netty.channel.Channel
import io.netty.util.AttributeKey


const val TOKEN = "token"
const val NAMESPACE = "nameSpace"
const val ROOM = "room"

/**
 * 登录后初始化channel信息，才能使用room，client等
 *
 * @param channel
 * @param token
 * @param nameSpace
 * @param gameUserInfo
 */
inline fun initChannel(channel: Channel, token: String, nameSpace: String, gameUserInfo: BaseUser<*>) {
    channel.attr(AttributeKey.valueOf<Any>("client")).set(Client(channel, gameUserInfo))
    channel.attr(AttributeKey.valueOf<Any>("nameSpace")).set(nameSpace)
    channel.attr(AttributeKey.valueOf<Any>("token")).set(token)
    UserClient.setClient(gameUserInfo.userId, channel.attr(AttributeKey.valueOf<Any>("client")).get() as Client)
}