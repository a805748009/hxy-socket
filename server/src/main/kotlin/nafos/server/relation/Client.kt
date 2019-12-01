package nafos.server.relation

import io.netty.channel.Channel
import io.netty.util.AttributeKey
import nafos.server.util.JsonUtil



data class Client(
        var channel: Channel,
        var user: BaseUser<*>? = null,
        var hideTime: Int = 0,
        var hide: Boolean = false
) {
    /**
     * --------------------client挂到后台计时用----------------
     */
    fun subCurrtTime() {
        if (hide) {
            this.hideTime -= 1
            if (this.hideTime <= 0) {
                this.channel.close()
            }
        }
    }

    fun setHide(hide: Boolean, timeOut: Int) {
        this.hideTime = timeOut
        this.hide = hide
    }

    /**
     * --------------------------channel操作方法-------------------------------------
     */
    fun disConnect() {
        channel.disconnect()
    }

    fun getToken(): String {
        return channel.attr(AttributeKey.valueOf<Any>(TOKEN)).get() as String
    }

    fun setToken(token: String) {
        channel.attr(AttributeKey.valueOf<Any>(TOKEN)).set(token)
    }

    fun getUserId(): Any? {
        return this.user?.getUserId()
    }

    fun getRoomId(): String? {
        return getRoom()?.roomId
    }

    fun sendMsg(id: Int, any: Any) {
        wirteAndFlush("$id|${JsonUtil.toJson(any)}")
    }

    /**
     * --------------------------房间操作方法-------------------------------------
     */

    /**
     * @Desc     加入房间
     * @Author   hxy
     * @Time     2019/9/18 18:16
     */
    fun joinRoom(roomId: String) {
        val room: Room = IoCache.roomMap[roomId] ?: let {
            val room = Room(roomId)
            NameSpace.inviteRoom(channel.attr(AttributeKey.valueOf<Any>(NAMESPACE)).get() as String, roomId)
            room.setNameSpace(channel.attr(AttributeKey.valueOf<Any>(NAMESPACE)).get() as String)
            room
        }
        room.addClient(this)
        room.addUser(this.user)
        channel.attr(AttributeKey.valueOf<Any>(ROOM)).set(room)
    }

    fun joinRoom(room: Room?) {
        if (room != null) {
            room.addClient(this)
            room.addUser(this.user)
            channel.attr(AttributeKey.valueOf<Any>(ROOM)).set(room)
        }
    }

    fun getRoom(): Room? {
        return channel.attr(AttributeKey.valueOf<Any>(ROOM)).get() as Room
    }


    /**
     * @return boolean
     * @Author 黄新宇
     * @date 2018/2/29 下午9:23
     * @Description(是否加入房间)
     */
    fun isJoinRoom(): Boolean {
        val room = getRoom()
        return room != null
    }


    /**
     * @Author 黄新宇
     * @date 2018/2/29 下午9:23
     * @Description(离开房间,T掉)
     */
    fun leaveRoom() {
        val room = getRoom() ?: return
        room.removeUser(this)
        channel.attr(AttributeKey.valueOf<Any>(ROOM)).set(null)
    }

    /**
     * @Author 黄新宇
     * @date 2018/2/29 下午9:23
     * @Description(离线房间，不删除用户信息，只是断开连接，可以重连)
     */
    fun offLineRoom() {
        val room = getRoom() ?: return
        room.offLineClient(this)
    }

    fun offLineRoomNotDelRoom() {
        val room = getRoom() ?: return
        room.offLineClientNotDel(this)
    }

    fun leaveRoomNotDelRoom() {
        val room = getRoom() ?: return
        room.removeUserNotDelRoom(this)
    }

    fun roomBroadcast(id: Int, obj: Any) {
        getRoom()?.sendMsg(id, obj)
    }

    fun getRoomClients(): List<Client>? {
        val room = getRoom()
        return room?.getClients()
    }

    fun getRoomDataOnkey(key: String): Any {
        return getRoom()!!.getRoomDataOnKey(key)
    }

    fun setRoomData(key: String, `object`: Any) {
        getRoom()!!.setRoomData(key, `object`)
    }

    fun getUserDataInRoomOnKey(key: String): Any {
        return getRoom()!!.getUserDataOnKey(this, key)
    }

    fun setUserDataInRoom(key: String, `object`: Any) {
        getRoom()!!.setUserDataOnkey(this, key, `object`)
    }


    /**
     * --------------------------命名空间操作方法-------------------------------------
     */
    fun joinNameSpace(nameSpace: String) {
        channel.attr(AttributeKey.valueOf<Any>(NAMESPACE)).set(nameSpace)
        NameSpace.addClient(nameSpace, this)
    }

    fun leaveNameSpace() {
        NameSpace.removeClient(getNameSpace(), this)
    }

    fun nameSpaceBroadcast(id: Int, obj: Any) {
        NameSpace.sendMsg(getNameSpace(), id, obj)
    }

    fun getNameSpace(): String {
        return channel.attr(AttributeKey.valueOf<Any>(NAMESPACE)).get() as String
    }


    private inline fun wirteAndFlush(str: String) {
        if (!channel.isActive) {
            return
        }
        channel.writeAndFlush(str)
    }
}
