package nafos.game.relation;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import nafos.core.mode.InitMothods;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SendUtil;
import nafos.game.entry.BaseUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年3月12日 下午1:09:09 类说明
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    protected Channel channel;

    protected int hideTime;

    protected boolean isHide;

    public Client(Channel channel) {
        super();
        this.channel = channel;
    }

    /**
     * --------------------client挂到后台计时用----------------
     */
    public void subCurrtTime() {
        if (isHide) {
            this.hideTime -= 1;
            if (this.hideTime <= 0) {
                this.channel.close();
            }
        }
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean isHide) {
        setHide(isHide,10);
    }

    public void setHide(boolean isHide,int timeOut) {
        this.hideTime = timeOut;
        this.isHide = isHide;
    }

    /**
     * --------------------------channel操作方法-------------------------------------
     */
    public void disConnect() {
        channel.disconnect();
    }

    public String getToken() {
        return (String) channel.attr(AttributeKey.valueOf("token")).get();
    }

    public void setToken(String token) {
        channel.attr(AttributeKey.valueOf("token")).set(token);
    }

    public String getUserId() {
        return (String) channel.attr(AttributeKey.valueOf("userId")).get();
    }

    public void setUserId(String userId) {
        channel.attr(AttributeKey.valueOf("userId")).set(userId);
    }

    public String getRoomId() {
        return getRoom().getRoomId();
    }

    public void sendMsg(Object obj) {
        wirteAndFlush(obj);
    }

    public void sendMsg(Object obj, byte[] id) {
        obj = SendUtil.castSendMsg(id, obj);
        wirteAndFlush(obj);
    }

    public Channel getChannel() {
        return channel;
    }

    /**
     * --------------------------房间操作方法-------------------------------------
     */

    // 加入房间
    public void joinRoom(String roomId,BaseUser userInfo) {
        Room room;
        if (ObjectUtil.isNull(IoCache.roomMap.get(roomId))) {
            room = new Room(roomId);
            NameSpace.inviteRoom((String) channel.attr(AttributeKey.valueOf("nameSpace")).get(), roomId);
            room.setNameSpace((String) channel.attr(AttributeKey.valueOf("nameSpace")).get());
        } else {
            room = IoCache.roomMap.get(roomId);
        }
        room.addClient(this);
        room.addUser(userInfo);
        channel.attr(AttributeKey.valueOf("room")).set(room);
    }

    public void joinRoom(Room room,BaseUser userInfo) {
        if (ObjectUtil.isNotNull(room)) {
            room.addClient(this);
            room.addUser(userInfo);
            channel.attr(AttributeKey.valueOf("room")).set(room);
        }
    }

    public Room getRoom() {
        return (Room) channel.attr(AttributeKey.valueOf("room")).get();
    }



    /**
    * @Author 黄新宇
    * @date 2018/2/29 下午9:23
    * @Description(是否加入房间)
    * @return boolean
    */
    public boolean isJoinRoom() {
        Room room = getRoom();
        return ObjectUtil.isNotNull(room);
    }


    /**
     * @Author 黄新宇
     * @date 2018/2/29 下午9:23
     * @Description(离开房间,T掉)
     */
    public void leaveRoom() {
        Room room = getRoom();
        if (ObjectUtil.isNull(room))
            return;
        room.removeUser(this);
        channel.attr(AttributeKey.valueOf("room")).set(null);
    }

    /**
     * @Author 黄新宇
     * @date 2018/2/29 下午9:23
     * @Description(离线房间，不删除用户信息，只是断开连接，可以重连)
     */
    public void offLineRoom() {
        Room room = getRoom();
        if (ObjectUtil.isNull(room))
            return;
        room.offLineClient(this);
    }

    public void offLineRoomNotDelRoom() {
        Room room = getRoom();
        if (ObjectUtil.isNull(room))
            return;
        room.offLineClientNotDel(this);
    }

    public void leaveRoomNotDelRoom() {
        Room room = getRoom();
        if (ObjectUtil.isNull(room))
            return;
        room.removeUserNotDelRoom(this);
    }


    public void roomBroadcast(Object obj) {
        Room room = getRoom();
        room.sendMsg(obj);
    }

    public void roomBroadcast(Object obj, Object id) {
        Room room = getRoom();
        obj = SendUtil.castSendMsg(id, obj);
        room.sendMsg(obj);
    }

    public List<Client> getRoomClients() {
        Room room = getRoom();
        return room.getClients();
    }

    public Object getRoomDataOnkey(String key) {
        return getRoom().getRoomDataOnKey(key);
    }

    public void setRoomData(String key, Object object) {
        getRoom().setRoomData(key, object);
    }

    public Object getUserDataInRoomOnKey(String key) {
        return getRoom().getUserDataOnKey(this, key);
    }

    public void setUserDataInRoom(String key, Object object) {
        getRoom().setUserDataOnkey(this, key, object);
    }


    /**
     * --------------------------命名空间操作方法-------------------------------------
     */
    public void joinNameSpace(String nameSpace) {
        channel.attr(AttributeKey.valueOf("nameSpace")).set(nameSpace);
        NameSpace.inviteClient(nameSpace, this);
    }

    public void leaveNameSpace() {
        NameSpace.removeClient(getNameSpace(), this);
    }

    public void nameSpaceBroadcast(Object obj) {
        NameSpace.sendMsg(getNameSpace(), obj);
    }

    public void nameSpaceBroadcast(Object obj, byte[] id) {
        obj = SendUtil.castSendMsg(id, obj);
        NameSpace.sendMsg(getNameSpace(), obj);
    }

    public String getNameSpace() {
        return (String) channel.attr(AttributeKey.valueOf("nameSpace")).get();
    }

    /**
     * --------------------------私有方法-------------------------------------
     */

    protected void wirteAndFlush(Object obj) {
        if (!channel.isActive())
            return;

        if (obj instanceof byte[]) {
            channel.writeAndFlush((byte[]) obj);
        } else {
            logger.error("==============>>>>>消息暂不支持传入WebSocketFrame对象，请传基础对象。此次发送失败");
        }
    }

}
