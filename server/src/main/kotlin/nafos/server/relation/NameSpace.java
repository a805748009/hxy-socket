package nafos.server.relation;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 作者 huangxinyu
 * @version 创建时间：2018年3月8日 下午6:05:07
 * 类说明
 */
public class NameSpace {

    public static Set<Client> getClients(String nameSpace) {
        return IoCache.spaceClientMap.get(nameSpace);
    }


    public static void addClient(String nameSpace, Client client) {
        if (IoCache.spaceClientMap.get(nameSpace) != null) {
            IoCache.spaceClientMap.get(nameSpace).add(client);
        } else {
            CopyOnWriteArraySet<Client> set = new CopyOnWriteArraySet();
            set.add(client);
            IoCache.spaceClientMap.put(nameSpace, set);
        }
    }

    public static void removeClient(String nameSpace, Client client) {
        if (getClients(nameSpace) != null) {
            IoCache.spaceClientMap.get(nameSpace).remove(client);
        }
    }


    public static void sendMsg(String nameSpace, int id ,Object obj) {
        Set<Client> clients = getClients(nameSpace);
        if (clients == null) {
            return;
        }
        for (Client client : clients) {
            client.sendMsg(id,obj);
        }
    }

    public static Set<String> getRoomIds(String nameSpace) {
        return IoCache.spaceRoomMap.get(nameSpace);
    }

    /**
     * 创建房间
     * @param nameSpace
     * @param roomId
     */
    public static void inviteRoom(String nameSpace, String roomId) {
        if (getRoomIds(nameSpace) != null) {
            IoCache.spaceRoomMap.get(nameSpace).add(roomId);
        } else {
            CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<String>();
            set.add(roomId);
            IoCache.spaceRoomMap.put(nameSpace, set);
        }
    }


    public static void removeRoom(String nameSpace, String roomId) {
        if (getRoomIds(nameSpace) != null) {
            IoCache.spaceRoomMap.get(nameSpace).remove(roomId);
        }
    }
}
