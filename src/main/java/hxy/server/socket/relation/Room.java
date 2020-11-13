package hxy.server.socket.relation;


/**
 * @ClassName Room
 * @Description 房间操作
 * @Author hxy
 * @Date 2020/4/9 15:12
 */
public class Room extends ClientContext {

    public Room(String namespace, String id) {
        super(id);
        Global.INSTANCE.getNamespace(namespace).addRoom(this);
    }

}
