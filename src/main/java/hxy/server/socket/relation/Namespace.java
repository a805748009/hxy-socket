package hxy.server.socket.relation;


import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName Namespace
 * @Description 命名空间操作
 * @Author hxy
 * @Date 2020/4/9 15:12
 */
public class Namespace extends ClientContext {

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>(1024);

    public Namespace(String id) {
        super(id);
        Global.INSTANCE.addNamespace(this);
    }

    public void addRoom(@NotNull Room room) {
        rooms.put(room.getId(), room);
    }

    public void removeRoom(@NotNull Room room) {
        rooms.remove(room.getId());
    }

    public int roomCount() {
        return rooms.size();
    }

    public Room getRoom(String id) {
        return rooms.get(id);
    }

    public boolean containsRoom(String id) {
        return rooms.containsKey(id);
    }

}
