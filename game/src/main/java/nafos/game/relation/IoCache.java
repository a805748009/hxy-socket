package nafos.game.relation;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IoCache {

   
    //RoomId--->>>Room
    public static final Map<String,Room> roomMap = new ConcurrentHashMap<>(1024);
    
    //根据nameSpace分组client
    public static final Map<String,HashSet<Client>> spaceClientMap = new ConcurrentHashMap<>(1024);
	 
    //根据nameSpace分组room
    public static final Map<String,HashSet<String>> spaceRoomMap = new ConcurrentHashMap<>(1024);
    
}