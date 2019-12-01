package nafos.server.relation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class IoCache {


    /**
     * RoomId--->>>Room
     */
    public static final Map<String, Room> roomMap = new ConcurrentHashMap<>(1024);

    /**
     * 根据nameSpace分组client
     */
    public static final Map<String, CopyOnWriteArraySet<Client>> spaceClientMap = new ConcurrentHashMap<>(1024);

    /**
     * 根据nameSpace分组room
     */
    public static final Map<String, CopyOnWriteArraySet<String>> spaceRoomMap = new ConcurrentHashMap<>(1024);

}